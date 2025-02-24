package uk.ac.soton.git.comp2211g17.model.types.field;

/**
 * Field for a column that provides user ID data as long integers
 */
public class IDField extends LongIntegerField {
	public IDField(String name) {
		super(name);
	}

	@Override
	public boolean excludeFromGraph() {
		return true;
	}
}
