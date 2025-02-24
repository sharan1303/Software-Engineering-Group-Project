package uk.ac.soton.git.comp2211g17.model.types;

/**
 * Column is an interface for a column of data that can be queried in several forms, with an associated field.<br>
 * <p>
 * The associated data type is inherent in the subclass - if a column is an instance of ContinuousNumericColumn, numeric data can
 * be queried from it.
 *
 * @param <T> The field type that this column contains data for
 */
public abstract class Column<T extends Field> {

	private final T field;
	private int length;

	public Column(T field, int length) {
		this.field = field;
		this.length = length;
	}

	public T getField() {
		return field;
	}

	public int getLength() {
		return length;
	}

	/**
	 * A human readable representation of the data in this column - e.g. for displaying in a table
	 */
	public abstract String[] getDataAsFormatted();
}
