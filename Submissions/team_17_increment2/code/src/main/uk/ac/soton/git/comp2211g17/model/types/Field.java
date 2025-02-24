package uk.ac.soton.git.comp2211g17.model.types;

import java.util.List;

/**
 * Field is an abstract class for a field of data, that has a name and associated data type.<br>
 * Data described by this interface can be queried in a {@link Column} of this data.<br>
 *
 * @param <V> The data type that columns of this field contain data for
 */
public abstract class Field<V> {
	private final String name;

	public Field(String name) {
		this.name = name;
	}

	/**
	 * @return The human-readable name of this Field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return True if this field should not ever be visible in graphs, or selectable for use in graphs
	 */
	public boolean excludeFromGraph() {
		return false;
	}

	/**
	 * Format the given value as a String.
	 * Default implementation returns "None" on null values, override to change this behaviour.
	 */
	public String format(V value) {
		if (value == null) {
			return "None";
		}
		return value.toString();
	}

	public Column<V> makeColumn(List<V> data) {
		return new Column<>(this, data);
	}

	public Column<V> fromJooqField(org.jooq.Result<? extends org.jooq.Record> result, org.jooq.Field<V> field) {
		return new Column<>(this, result.getValues(field));
	}
}
