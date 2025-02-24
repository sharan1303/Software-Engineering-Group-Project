package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;

/**
 * Field for a column that provides boolean data
 */
public class BooleanField extends Field<Boolean> {
	public BooleanField(String name) {
		super(name);
	}

	@Override
	public String format(Boolean value) {
		if (value == null) {
			return "";
		} else if (value) {
			return "Yes";
		} else {
			return "No";
		}
	}
}