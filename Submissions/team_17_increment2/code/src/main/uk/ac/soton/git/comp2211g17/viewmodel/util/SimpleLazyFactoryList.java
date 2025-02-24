package uk.ac.soton.git.comp2211g17.viewmodel.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntFunction;

public class SimpleLazyFactoryList<T> implements List<T> {
	private final IntFunction<T> factory;
	private final int size;
	private final int offset;

	public SimpleLazyFactoryList(IntFunction<T> factory, int size, int offset) {
		this.factory = factory;
		this.size = size;
		this.offset = offset;
	}

	@Override
	public T get(int i) {
		return factory.apply(i + offset);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) > -1;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object obj : collection) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[size]);
	}

	@Override
	public <T1> T1[] toArray(T1[] arr) {
		return arr;
	}

	@Override
	public int indexOf(Object o) {
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		return 0;
	}

	private class Iter implements ListIterator<T> {
		private int i;

		public Iter() {
			this(0);
		}

		public Iter(int startingPos) {
			i = startingPos;
		}

		@Override
		public boolean hasNext() {
			return size < i;
		}

		@Override
		public T next() {
			return get(i++);
		}

		@Override
		public boolean hasPrevious() {
			return i > 0;
		}

		@Override
		public T previous() {
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
		public void set(T t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(T t) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		return new Iter();
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		return new Iter(i);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iter();
	}

	@Override
	public List<T> subList(int i, int i1) {
		return new SimpleLazyFactoryList<>(factory, i1 - i, offset + i);
	}

	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> collection) {
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
	public T set(int i, T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int i, T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int i) {
		throw new UnsupportedOperationException();
	}
}
