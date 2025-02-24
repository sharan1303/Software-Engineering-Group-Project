package uk.ac.soton.git.comp2211g17.model.types.column;

import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.DateTimeField;

import java.time.LocalDateTime;

/**
 * Column that provides date/time data
 */
public class DateTimeColumn extends Column<DateTimeField> {
	private final LocalDateTime[] data;

	public DateTimeColumn(DateTimeField field, LocalDateTime[] data) {
		super(field, data.length);
		this.data = data;
	}

	public LocalDateTime[] getDataAsDateTime() {
		return data;
	}

	@Override
	public String[] getDataAsFormatted() {
		String[] formatted = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			formatted[i] = data[i].toString();
		}
		return formatted;
	}
}
