package uk.ac.soton.git.comp2211g17.viewmodel.util;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Like EasyBind.map lists but caching the results so the mapper is only called once for identical input
 */
public class IdempotentMappedObservableList<E, F> extends TransformationList<E, F> {
	private final Function<F, E> mapper;
	private final List<E> results;
	private final ObservableList<? extends F> source;

	/**
	 * Constructor for IdempotentMappedObservableList
	 * @param source The wrapped list
	 * @param mapper The transformation to apply to each value in the list - must return a non-null value
	 */
	public IdempotentMappedObservableList(ObservableList<? extends F> source, Function<F, E> mapper) {
		super(source);
		this.mapper = mapper;
		this.source = source;
		results = new ArrayList<>(source.size());
	}

	@Override
	protected void sourceChanged(ListChangeListener.Change<? extends F> c) {
		while (c.next()) {
			if (c.wasReplaced()) {
				for (int i = c.getFrom(); i < c.getTo(); i++) {
					results.set(i, null);
				}
			} else {
				if (c.wasAdded()) {
					for (int i = c.getFrom(); i < c.getTo(); i++) {
						results.add(i, null);
					}
				}
				if (c.wasRemoved()) {
					results.subList(c.getFrom(), c.getTo()).clear();
				}
			}
		}
		c.reset();
		fireChange(new ListChangeListener.Change<>(this) {
			@Override
			public boolean wasAdded() {
				return c.wasAdded();
			}

			@Override
			public boolean wasRemoved() {
				return c.wasRemoved();
			}

			@Override
			public boolean wasReplaced() {
				return c.wasReplaced();
			}

			@Override
			public boolean wasUpdated() {
				return c.wasUpdated();
			}

			@Override
			public boolean wasPermutated() {
				return c.wasPermutated();
			}

			@Override
			public int getPermutation(int i) {
				return c.getPermutation(i);
			}

			@Override
			protected int[] getPermutation() {
				return null;
			}

			@Override
			public int getRemovedSize() {
				return c.getRemovedSize();
			}

			@Override
			public List<E> getRemoved() {
				// Kinda breaks idempotence a bit but thankfully nothing actually uses this directly (only getRemovedSize!)
				ArrayList<E> removedResults = new ArrayList<>(c.getRemovedSize());
				for (F element : c.getRemoved()) {
					removedResults.add(mapper.apply(element));
				}
				return removedResults;
			}

			@Override
			public int getFrom() {
				return c.getFrom();
			}

			@Override
			public int getTo() {
				return c.getTo();
			}

			@Override
			public boolean next() {
				return c.next();
			}

			@Override
			public void reset() {
				c.reset();
			}
		});
	}

	@Override
	public int getSourceIndex(int index) {
		return index;
	}

	@Override
	public int getViewIndex(int index) {
		return index;
	}

	@Override
	public E get(int i) {
		E result = results.get(i);
		if (result == null) {
			result = mapper.apply(source.get(i));
			results.set(i, result);
		}
		return result;
	}

	@Override
	public int size() {
		return source.size();
	}
}
