package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Field for a column that provides date and time data
 */
public class DateTimeField extends Field<LocalDateTime> {
	public DateTimeField(String name) {
		super(name);
	}

	@Override
	public String format(LocalDateTime value) {
		if (value == null) return "";
		return value.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}
}
