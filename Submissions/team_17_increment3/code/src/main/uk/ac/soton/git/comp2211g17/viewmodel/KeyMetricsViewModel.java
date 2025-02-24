package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.query.async.AsyncQueryHandler;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.TimeGraphViewModel;

public class KeyMetricsViewModel {
	private final Property<KeyMetrics> metric;
	public final Property<String> metricValue = new SimpleStringProperty();
	public final TimeGraphViewModel timeGraphViewModel = new TimeGraphViewModel();

	private final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler();

	public KeyMetricsViewModel(Property<KeyMetrics> metric) {
		this.metric = metric;
		asyncQueryHandler.await(this::onResult);
		update();
		metric.addListener(observable -> update());
	}

	private void update() {
		if (metric.getValue() == null) {
			return;
		}

		Query query = metric.getValue().construct(DatabaseManager.getInstance());
		asyncQueryHandler.setQuery(query);
		timeGraphViewModel.setQuery(metric.getValue().construct(DatabaseManager.getInstance()));
	}

	private void onResult(Column<?>[] cols) {
		//noinspection unchecked
		Column<Integer> col = (Column<Integer>) cols[0];
		metricValue.setValue(col.getDataAsFormatted().get(0));
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

	public FutureIndicatorViewModel getFutureIndicator() {
		return asyncQueryHandler;
	}
}
