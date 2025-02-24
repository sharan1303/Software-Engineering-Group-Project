package uk.ac.soton.git.comp2211g17.model.types.field;

import java.text.NumberFormat;

public class PercentageField extends FloatField {
	public PercentageField(String name) {
		super(name);
	}

	@Override
	public String format(Float value) {
		if (value == null) return "";
		NumberFormat format = NumberFormat.getPercentInstance();
		format.setMinimumFractionDigits(2);
		return format.format(value);
	}
}
