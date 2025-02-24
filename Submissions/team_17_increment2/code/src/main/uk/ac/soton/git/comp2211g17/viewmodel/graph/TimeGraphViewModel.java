package uk.ac.soton.git.comp2211g17.viewmodel.graph;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;
import uk.ac.soton.git.comp2211g17.model.query.metrics.KeyMetricQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.sql.SQLException;
import java.util.function.Function;

public class TimeGraphViewModel {
	public final Property<KeyMetricQuery> query = new SimpleObjectProperty<>();
	public final Property<String> xlabel = new SimpleStringProperty();
	public final Property<String> ylabel = new SimpleStringProperty();
	public final ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();
	// TODO: add formatter?
	public final Property<Function<Table<?>, Filter>> filter = new SimpleObjectProperty<>();

	public final IntegerProperty timeGranularity = new SimpleIntegerProperty(3600);

	public TimeGraphViewModel() {
		query.addListener((observable) -> update());
		DatabaseManager.getInstance().reload.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				update();
			}
		}));
		filter.addListener((observable, oldValue, newValue) -> update());
		timeGranularity.addListener((observable, oldValue, newValue) -> update());
	}

	private void update() {
		if (query.getValue() == null) {
			// TODO: add error handling and empty data message?
			return;
		}
		query.getValue().setInterval(timeGranularity.get());
		query.getValue().setFilter(filter.getValue());
		// Execute the query
		Column<? extends Number> xCol;
		Column<? extends Number> yCol;
		try {
			Column<?>[] cols = query.getValue().execute();
			if (cols.length < 2) {
				throw new SQLException("Incorrect number of fields");
			}
			//noinspection unchecked
			xCol = (Column<? extends Number>) cols[0];
			//noinspection unchecked
			yCol = (Column<? extends Number>) cols[1];
		} catch (SQLException e) {
			Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
			e.printStackTrace();
			return;
		}

		xlabel.setValue(xCol.getField().getName());
		ylabel.setValue(yCol.getField().getName());

		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		series.setName(yCol.getField().getName());
		series.getData().setAll(ChartModelUtils.toChartDataTime(xCol, yCol));

		data.clear();
		data.add(series);
	}

	public ObservableList<XYChart.Series<Number, Number>> getData() {
		return data;
	}

	public String getXlabel() {
		return xlabel.getValue();
	}

	public void setXlabel(String value) {
		xlabel.setValue(value);
	}

	public Property<String> xlabelProperty() {
		return xlabel;
	}

	public String getYlabel() {
		return ylabel.getValue();
	}

	public void setYlabel(String value) {
		ylabel.setValue(value);
	}

	public Property<String> ylabelProperty() {
		return ylabel;
	}

	public KeyMetricQuery getQuery() {
		return query.getValue();
	}

	public Property<KeyMetricQuery> queryProperty() {
		return query;
	}

	public void setQuery(KeyMetricQuery query) {
		this.query.setValue(query);
	}

	public int getTimeGranularity() {
		return timeGranularity.get();
	}

	public IntegerProperty timeGranularityProperty() {
		return timeGranularity;
	}

	public void setTimeGranularity(int timeGranularity) {
		this.timeGranularity.set(timeGranularity);
	}

	public Function<Table<?>, Filter> getFilter() {
		return filter.getValue();
	}

	public Property<Function<Table<?>, Filter>> filterProperty() {
		return filter;
	}

	public void setFilter(Function<Table<?>, Filter> filter) {
		this.filter.setValue(filter);
	}
}
