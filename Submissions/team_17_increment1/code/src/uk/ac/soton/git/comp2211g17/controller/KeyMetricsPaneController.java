package uk.ac.soton.git.comp2211g17.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import uk.ac.soton.git.comp2211g17.model.query.impl.KeyMetricsQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class KeyMetricsPaneController {
	private final KeyMetricsQuery query;

	// Properties read by the view
	private final ObjectProperty<List<Column<?>>> dataInternal = new SimpleObjectProperty<>();
	public final ReadOnlyProperty<List<Column<?>>> data = dataInternal;

	public KeyMetricsPaneController(KeyMetricsQuery query) {
		this.query = query;

		try {
			dataInternal.set(Arrays.asList(query.execute()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
