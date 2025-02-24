package uk.ac.soton.git.comp2211g17.viewmodel.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

class VirtualListCacher<T> {
	private int cachedLength;
	private final VirtualListProvider<T> provider;
	// LRU cache, storing data queried from the VirtualListProvider
	private final Int2ObjectLinkedOpenHashMap<T> cache = new Int2ObjectLinkedOpenHashMap<>();

	private static final int READ_AHEAD_SIZE = 20;
	private static final int READ_BEHIND_SIZE = 20;
	private static final int CACHE_SIZE = 300;

	public VirtualListCacher(VirtualListProvider<T> provider) {
		this.provider = provider;
		cachedLength = provider.getLength();
	}

	protected void invalidate() {
		cache.clear();
		cachedLength = provider.getLength();
	}

	protected T get(int i) {
		// First, try the cache
		T value = cache.getAndMoveToFirst(i);
		if (value != null) {
			return value;
		}
		// Then determine how many elements we should query
		// Query to 0 or read behind size, whichever comes first
		int start = Math.max(i - READ_BEHIND_SIZE, 0);
		// Query to the end of the source or read ahead size, whichever comes first
		int end = Math.min(i + READ_AHEAD_SIZE, cachedLength);
		// Search ahead to determine if we already have cached data
		for (int j = i + 1; j < end; j++) {
			if (cache.getAndMoveToFirst(j) != null) {
				// When there is a value in the cache, we stop our query batch here - no need to query this data again
				end = j;
				break;
			}
		}
		// Search behind to determine if we already have cached data
		for (int j = i - 1; j >= start; j--) {
			if (cache.getAndMoveToFirst(j) != null) {
				start = j + 1;
				break;
			}
		}
		// Query the data
		T[] data = provider.getData(start, end - start);
		if (data.length == 0) {
			return null;
		}
		if (data.length != end - start) {
			// Data changed beneath us! TODO: could invalidate? needs to be propagated upwards to VirtualObservableList
			return getNoBatch(i);
		}
		// Remove the last elements in the cache to fit the new data
		int remainingSpace = CACHE_SIZE - cache.size();
		int elementsToRemove = data.length - remainingSpace;
		for (int j = 0; j < elementsToRemove; j++) {
			cache.removeLast();
		}
		// Add the new data in reverse order (so in the actual list it will be in order)
		for (int j = data.length - 1; j >= 0; j--) {
			cache.putAndMoveToFirst(start + j, data[j]);
		}
		return data[i - start];
	}

	// Fallback method for when batching fails
	private T getNoBatch(int i) {
		// First, try the cache
		T value = cache.getAndMoveToFirst(i);
		if (value != null) {
			return value;
		}
		// Query the data
		T[] data = provider.getData(i, 1);
		if (data.length != 1) {
			return null;
		}
		// Remove the last element in the cache to fit the new data
		int remainingSpace = CACHE_SIZE - cache.size();
		if (remainingSpace < 1) {
			cache.removeLast();
		}
		// Add the new data
		cache.putAndMoveToFirst(i, data[0]);
		return data[i];
	}

	protected int getLength() {
		return cachedLength;
	}
}
