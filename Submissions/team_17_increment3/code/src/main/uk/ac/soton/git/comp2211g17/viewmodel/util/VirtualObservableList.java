package uk.ac.soton.git.comp2211g17.viewmodel.util;

import com.sun.javafx.collections.NonIterableChange;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * ObservableList implementation that queries data from the given Provider, with built in batching and caching, and can be invalidated
 */
public class VirtualObservableList<T> implements ObservableList<VirtualListEntry<T>> {
	private final VirtualListCacher<T> cachedList;
	private final WeakHashMap<Integer, VirtualListEntry<T>> virtualEntries = new WeakHashMap<>();

	private final List<ListChangeListener<? super VirtualListEntry<T>>> changeListeners = new ArrayList<>();
	private final List<InvalidationListener> invalidationListeners = new ArrayList<>();

	public VirtualObservableList(VirtualListProvider<T> provider) {
		this.cachedList = new VirtualListCacher<>(provider);
	}

	public void invalidate() {
		int prevLength = cachedList.getLength();
		cachedList.invalidate();

		// Update all entries
		for (VirtualListEntry<T> entry : virtualEntries.values()) {
			entry.invalidate();
		}

		// Run all change listeners
		if (!changeListeners.isEmpty()) {
			// Construct a Change object describing what has changed in this list
			ListChangeListener.Change<VirtualListEntry<T>> change;
			if (prevLength == cachedList.getLength()) {
				change = new NonIterableChange.SimpleUpdateChange<>(0, prevLength, this);
			} else if (prevLength > cachedList.getLength()) {
				// Lazily construct the list of removed elements
				// These can be equated with the existing elements by reference - as we're using a WeakHashMap this won't leak memory
				change = new NonIterableChange.GenericAddRemoveChange<>(cachedList.getLength(), prevLength,
					new SimpleLazyFactoryList<>(this::get, prevLength - cachedList.getLength(), prevLength), this);
			} else {
				change = new NonIterableChange.SimpleAddChange<>(prevLength, cachedList.getLength(), this);
			}
			for (ListChangeListener<? super VirtualListEntry<T>> listener : changeListeners) {
				listener.onChanged(change);
			}
		}

		// Run all invalidation listeners
		for (InvalidationListener listener : invalidationListeners) {
			listener.invalidated(this);
		}
	}

	/* Retrieval/query operations */

	@Override
	public VirtualListEntry<T> get(int i) {
		VirtualListEntry<T> entry = virtualEntries.get(i);
		if (entry != null) {
			return entry;
		}
		VirtualListEntry<T> newEntry = new VirtualListEntry<>(i, cachedList);
		virtualEntries.put(i, newEntry);
		return newEntry;
	}

	@Override
	public int size() {
		return cachedList.getLength();
	}

	@Override
	public boolean isEmpty() {
		return cachedList.getLength() == 0;
	}

	private class Iter implements ListIterator<VirtualListEntry<T>> {
		private int i;

		public Iter() {
			this(0);
		}

		public Iter(int startingPos) {
			i = startingPos;
		}

		@Override
		public boolean hasNext() {
			return cachedList.getLength() < i;
		}

		@Override
		public VirtualListEntry<T> next() {
			return get(i++);
		}

		@Override
		public boolean hasPrevious() {
			return i > 0;
		}

		@Override
		public VirtualListEntry<T> previous() {
			return get(--i);
		}

		@Override
		public int nextIndex() {
			return i;
		}

		@Override
		public int previousIndex() {
			return i - 1;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(VirtualListEntry<T> t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(VirtualListEntry<T> t) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Iterator<VirtualListEntry<T>> iterator() {
		return new Iter();
	}

	@Override
	public ListIterator<VirtualListEntry<T>> listIterator() {
		return new Iter();
	}

	@Override
	public ListIterator<VirtualListEntry<T>> listIterator(int i) {
		return new Iter(i);
	}

	/* Unsupported bulk query operations */

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T1> T1[] toArray(T1[] t1s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<VirtualListEntry<T>> subList(int i, int i1) {
		throw new UnsupportedOperationException();
	}

	/* Unsupported mutation operations */

	@SafeVarargs
	@Override
	public final boolean addAll(VirtualListEntry<T>... elements) {
		throw new UnsupportedOperationException();
	}

	@SafeVarargs
	@Override
	public final boolean setAll(VirtualListEntry<T>... elements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setAll(Collection<? extends VirtualListEntry<T>> col) {
		throw new UnsupportedOperationException();
	}

	@SafeVarargs
	@Override
	public final boolean removeAll(VirtualListEntry<T>... elements) {
		throw new UnsupportedOperationException();
	}

	@SafeVarargs
	@Override
	public final boolean retainAll(VirtualListEntry<T>... elements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(int from, int to) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(VirtualListEntry<T> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends VirtualListEntry<T>> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int i, Collection<? extends VirtualListEntry<T>> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public VirtualListEntry<T> set(int i, VirtualListEntry<T> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int i, VirtualListEntry<T> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public VirtualListEntry<T> remove(int i) {
		throw new UnsupportedOperationException();
	}

	/* Listeners */

	@Override
	public void addListener(ListChangeListener<? super VirtualListEntry<T>> listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeListener(ListChangeListener<? super VirtualListEntry<T>> listener) {
		changeListeners.remove(listener);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}
}
