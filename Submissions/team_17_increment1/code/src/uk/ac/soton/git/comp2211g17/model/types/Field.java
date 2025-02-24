package uk.ac.soton.git.comp2211g17.model.types;

/**
 * Field is an abstract class for a field of data, that has a name and associated data type.<br>
 * Data described by this interface can be queried in a {@link Column} of this data.<br>
 * <p>
 * The associated data type is inherent in the subclass - if a field is an instance of ContinuousNumericField, numeric data can
 * be queried from its associated column.
 */
public abstract class Field {
	private final String name;

	public Field(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return True if this field should not ever be visible in graphs, or selectable for use in graphs
	 */
	public boolean excludeFromGraph() {
		return false;
	}
}
