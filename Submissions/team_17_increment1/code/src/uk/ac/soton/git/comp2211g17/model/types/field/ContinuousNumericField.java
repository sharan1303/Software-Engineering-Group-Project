package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.column.ContinuousNumericColumn;

/**
 * Field for a column that provides continuous single-precision floating-point numeric data
 */
public class ContinuousNumericField extends Field {
	public ContinuousNumericField(String name) {
		super(name);
	}

	public ContinuousNumericColumn makeColumn(float[] data) {
		return new ContinuousNumericColumn(this, data);
	}
}
