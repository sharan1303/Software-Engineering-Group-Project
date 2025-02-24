package uk.ac.soton.git.comp2211g17.viewmodel.util;

public interface VirtualListProvider<T> {
	/**
	 * Query the length of the data in the source. May be expensive, so the result is cached.
	 */
	int getLength();

	/**
	 * Query the data at a given offset and length. May be expensive, so it is queried in batches and cached.
	 */
	T[] getData(int offset, int length);
}
