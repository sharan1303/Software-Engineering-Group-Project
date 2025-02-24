package uk.ac.soton.git.comp2211g17.model.types.column;

import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.LongIntegerField;

/**
 * Column that provides long integer data
 */
public class LongIntegerColumn extends Column<LongIntegerField> {
	private final long[] data;

	public LongIntegerColumn(LongIntegerField field, long[] data) {
		super(field, data.length);
		this.data = data;
	}

	public long[] getDataAsLongInteger() {
		return data;
	}

	@Override
	public String[] getDataAsFormatted() {
		String[] formatted = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			formatted[i] = Long.toString(data[i]);
		}
		return formatted;
	}
}
