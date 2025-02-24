package uk.ac.soton.git.comp2211g17.model.query.async;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Semaphore;

public class AsyncQueryScheduler {
	private final WeakHashMap<QueryPlan, WeakReference<AsyncQueryFuture>> futures = new WeakHashMap<>();
	private final HashMap<Observable, List<WeakReference<AsyncQueryFuture>>> observedValues = new HashMap<>();

	private final Semaphore taskSem = new Semaphore(0);
	private volatile Task<?> executingTask = null;
	private final InvalidationListener observerInvalidationListener = this::observerInvalidationHandler;

	public AsyncQueryScheduler() {
		Thread executor = new Thread(() -> {
			try {
				while (true) {
					// Wait for a task to be queued
					taskSem.acquire();
					// Attempt to run the task
					if (executingTask != null) {
						executingTask.run();
					}
					executingTask = null;
					// Enqueue another task (if there are any waiting)
					Platform.runLater(this::pushFutures);
				}
			} catch (InterruptedException ignored) {
			}
		}, "AsyncQueryScheduler execution thread");
		executor.setDaemon(true);
		executor.start();
	}

	protected AsyncQueryFuture getBoundFuture(QueryPlan queryPlan, AsyncQueryHandler handler) {
		WeakReference<AsyncQueryFuture> ref = futures.get(queryPlan);
		AsyncQueryFuture future = null;
		if (ref != null) {
			future = ref.get();
		}
		if (future == null) {
			future = new AsyncQueryFuture(queryPlan);
			ref = new WeakReference<>(future);
			futures.put(queryPlan, ref);
			for (Observable observable : queryPlan.dependencies) {
				List<WeakReference<AsyncQueryFuture>> list = observedValues.get(observable);
				if (list == null) {
					list = new ArrayList<>();
					list.add(ref);
					observedValues.put(observable, list);
					observable.addListener(observerInvalidationListener);
				} else {
					list.add(ref);
				}
			}
		}
		future.bind(handler);
		pushFutures();
		return future;
	}

	private void pushFutures() {
		// Attempt to schedule another task, if none are currently executing
		if (executingTask == null) {
			AsyncQueryFuture futureToQueue = null;
			for (WeakReference<AsyncQueryFuture> futureRef : futures.values()) {
				AsyncQueryFuture future = futureRef.get();
				if (future == null) {
					continue;
				}
				if (future.shouldSchedule()) {
					// Prioritise visible futures
					if (future.isVisible()) {
						futureToQueue = future;
						break;
					}
					if (futureToQueue == null) {
						futureToQueue = future;
					}
				}
			}

			if (futureToQueue != null) {
				executingTask = futureToQueue.schedule();
				// Signal the processing thread to continue
				taskSem.release();
			}
		}
	}

	public void reload() {
		for (WeakReference<AsyncQueryFuture> futureRef : futures.values()) {
			AsyncQueryFuture future = futureRef.get();
			if (future != null) {
				future.invalidate();
			}
		}
		pushFutures();
	}

	public void observerInvalidationHandler(Observable observable) {
		List<WeakReference<AsyncQueryFuture>> observerFutures = observedValues.get(observable);
		if (observerFutures != null && observerFutures.size() > 0) {
			Iterator<WeakReference<AsyncQueryFuture>> iter = observerFutures.iterator();
			while (iter.hasNext()) {
				WeakReference<AsyncQueryFuture> futureRef = iter.next();
				AsyncQueryFuture future = futureRef.get();
				if (future == null) {
					iter.remove();
				} else {
					future.invalidate();
				}
			}
		} else {
			observable.removeListener(observerInvalidationListener);
			observedValues.remove(observable);
		}
		pushFutures();
	}
}
