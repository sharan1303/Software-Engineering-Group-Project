package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;

/**
 * Field for a column that provides continuous single-precision floating-point numeric data
 */
public class FloatField extends Field<Float> {
	public FloatField(String name) {
		super(name);
	}
}