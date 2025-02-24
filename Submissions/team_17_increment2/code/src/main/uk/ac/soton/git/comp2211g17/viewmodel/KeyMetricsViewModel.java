package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.TimeGraphViewModel;

import java.sql.SQLException;

public class KeyMetricsViewModel {
	private final Property<KeyMetrics> metric;
	public final Property<String> metricValue = new SimpleStringProperty();
	public final TimeGraphViewModel timeGraphViewModel = new TimeGraphViewModel();

	public KeyMetricsViewModel(Property<KeyMetrics> metric) {
		this.metric = metric;
		update();
		DatabaseManager.getInstance().reload.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				update();
			}
		}));
		metric.addListener(observable -> update());
	}

	private void update() {
		if (metric.getValue() == null) {
			return;
		}

		Query query = metric.getValue().construct(DatabaseManager.getInstance());
		// Execute the query
		Column<Integer> col;
		try {
			Column<?>[] cols = query.execute();
			if (cols.length != 1) {
				throw new SQLException("Incorrect number of fields");
			}
			//noinspection unchecked
			col = (Column<Integer>) cols[0];
		} catch (SQLException e) {
			Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
			e.printStackTrace();
			return;
		}

		// TODO: check for when value does not exist
		metricValue.setValue(col.getDataAsFormatted().get(0));

		timeGraphViewModel.setQuery(metric.getValue().construct(DatabaseManager.getInstance()));
	}

	public String getMetricValue() {
		return metricValue.getValue();
	}

	public Property<String> metricValueProperty() {
		return metricValue;
	}

	public TimeGraphViewModel getTimeGraphViewModel() {
		return timeGraphViewModel;
	}
}
