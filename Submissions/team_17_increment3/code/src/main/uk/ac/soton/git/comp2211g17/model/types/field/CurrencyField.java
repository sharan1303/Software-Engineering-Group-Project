package uk.ac.soton.git.comp2211g17.model.types.field;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * No, banks don't handle currency as floats. Please don't do this in a real world situation,
 * otherwise you'll get sued for floating point inaccuracies when someone tries to put 10p and 20p into their bank account.
 * Values here are in pence.
 */
public class CurrencyField extends FloatField {
	public CurrencyField(String name) {
		super(name);
	}

	@Override
	public String format(Float value) {
		return NumberFormat.getCurrencyInstance(Locale.UK).format(value / 100);
	}
}
