package uk.ac.soton.git.comp2211g17.model.types;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Column is a column of data that can be queried in its native data type or as a formatted string, with an associated
 * field containing metadata about it.<br>
 *
 * @param <V> The data type that this column contains data for
 */
public class Column<V> {
	private final Field<V> field;
	protected final List<V> data;

	public Column(Field<V> field, List<V> data) {
		this.field = field;
		this.data = data;
	}

	public Field<V> getField() {
		return field;
	}

	public List<V> getData() {
		return data;
	}

	public int getLength() {
		return data.size();
	}

	/**
	 * A human readable representation of the data in this column - e.g. for displaying in a table
	 */
	public List<String> getDataAsFormatted() {
		return data.stream().map(field::format).collect(Collectors.toList());
	}
}
