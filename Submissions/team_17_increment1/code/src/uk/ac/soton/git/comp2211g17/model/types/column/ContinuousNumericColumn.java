package uk.ac.soton.git.comp2211g17.model.types.column;

import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.ContinuousNumericField;

/**
 * Column that provides continuous single-precision floating-point numeric data
 */
public class ContinuousNumericColumn extends Column<ContinuousNumericField> {
	private final float[] data;

	public ContinuousNumericColumn(ContinuousNumericField field, float[] data) {
		super(field, data.length);
		this.data = data;
	}

	public float[] getDataAsContinuousNumeric() {
		return data;
	}

	@Override
	public String[] getDataAsFormatted() {
		String[] formatted = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			formatted[i] = Float.toString(data[i]);
		}
		return formatted;
	}
}
