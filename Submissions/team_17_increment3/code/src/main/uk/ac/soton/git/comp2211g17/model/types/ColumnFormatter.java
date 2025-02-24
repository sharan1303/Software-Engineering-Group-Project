package uk.ac.soton.git.comp2211g17.model.types;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper of {@link Column} that returns formatted String data from getData using getDataAsFormatted
 */
public class ColumnFormatter<T> extends Column<String> {
	public ColumnFormatter(Field<T> field, List<? extends T> data) {
		super(new Field<>(field.getName()) {
			@Override
			public boolean excludeFromGraph() {
				return field.excludeFromGraph();
			}
		}, format(field, data));
	}

	private static <T> List<String> format(Field<T> field, List<? extends T> data) {
		List<String> formatted = new ArrayList<>();
		for (T value : data) {
			formatted.add(field.format(value));
		}
		return formatted;
	}
}
