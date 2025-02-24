package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.column.LongIntegerColumn;

/**
 * Field for a column that provides long integer data
 */
public class LongIntegerField extends Field {
	public LongIntegerField(String name) {
		super(name);
	}

	public LongIntegerColumn makeColumn(long[] data) {
		return new LongIntegerColumn(this, data);
	}
}
