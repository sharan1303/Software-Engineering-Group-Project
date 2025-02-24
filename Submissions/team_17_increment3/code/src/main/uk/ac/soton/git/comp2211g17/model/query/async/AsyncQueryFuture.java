package uk.ac.soton.git.comp2211g17.model.query.async;

import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages the processing of a single QueryPlan
 */
class AsyncQueryFuture {
	private final QueryPlan plan;
	private final Set<AsyncQueryHandler> boundHandlers = new HashSet<>();

	private final BooleanProperty processingInternal = new SimpleBooleanProperty(true);
	public final ReadOnlyBooleanProperty processing = processingInternal;

	private final ObjectProperty<Column<?>[]> resultInternal = new SimpleObjectProperty<>();
	public final ReadOnlyObjectProperty<Column<?>[]> result = resultInternal;

	private final ObjectProperty<Throwable> exceptionInternal = new SimpleObjectProperty<>();
	public final ReadOnlyObjectProperty<Throwable> exception = exceptionInternal;

	private final AtomicBoolean invalid = new AtomicBoolean(true);
	private final AtomicBoolean invalidatedInProgress = new AtomicBoolean(false);

	protected AsyncQueryFuture(QueryPlan plan) {
		this.plan = plan;
	}

	public boolean shouldSchedule() {
		return boundHandlers.size() > 0 && invalid.get();
	}

	public boolean isVisible() {
		for (AsyncQueryHandler handler : boundHandlers) {
			if (handler.visible.get()) {
				return true;
			}
		}
		return false;
	}

	public Task<Column<?>[]> schedule() {
		exceptionInternal.set(null);
		processingInternal.set(true);
		Task<Column<?>[]> task = new Task<>() {
			@Override
			protected Column<?>[] call() throws Exception {
				try {
					invalidatedInProgress.set(false);
					return plan.executor.execute();
				} finally {
					if (!invalidatedInProgress.get()) {
						invalid.set(false);
					}
				}
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, evt -> {
			System.out.println("Failed to execute async query: " + evt.getSource().getException());
			evt.getSource().getException().printStackTrace();
			exceptionInternal.set(evt.getSource().getException());
			processingInternal.set(false);
		});
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, evt -> {
			resultInternal.set((Column<?>[]) evt.getSource().getValue());
			processingInternal.set(false);
		});
		return task;
	}

	public void bind(AsyncQueryHandler handler) {
		boundHandlers.add(handler);
	}

	public void unbind(AsyncQueryHandler handler) {
		boundHandlers.remove(handler);
	}

	public void invalidate() {
		invalid.set(true);
		invalidatedInProgress.set(true);
		exceptionInternal.set(null);
		processingInternal.set(true);
	}

}
