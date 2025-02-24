package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.column.DateTimeColumn;

import java.time.LocalDateTime;

/**
 * Field for a column that provides date and time data
 */
public class DateTimeField extends Field {
	public DateTimeField(String name) {
		super(name);
	}

	public DateTimeColumn makeColumn(LocalDateTime[] data) {
		return new DateTimeColumn(this, data);
	}
}
