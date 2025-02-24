package uk.ac.soton.git.comp2211g17.viewmodel.graph;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.query.async.AsyncQueryHandler;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.ColumnFormatter;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;

public class HistogramViewModel {
	public final Property<Query> query = new SimpleObjectProperty<>();
	public final Property<String> xlabel = new SimpleStringProperty();
	public final Property<String> ylabel = new SimpleStringProperty();
	public final ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
	// TODO: add filters, time granularity

	private final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler();

	public HistogramViewModel() {
		asyncQueryHandler.await(this::onResult);
		query.addListener((observable) -> update());
	}

	public HistogramViewModel(Query query) {
		this();
		this.query.setValue(query);
	}

	private void update() {
		if (query.getValue() == null) {
			// TODO: add error handling and empty data message?
			return;
		}
		asyncQueryHandler.setQuery(query.getValue());
	}

	private void onResult(Column<?>[] result) {
		Column<String> xCol;
		Column<? extends Number> yCol;
		//noinspection unchecked
		xCol = new ColumnFormatter<>((Field<Object>) result[0].getField(), result[0].getData());
		//noinspection unchecked
		yCol = (Column<? extends Number>) result[1];

		xlabel.setValue(xCol.getField().getName());
		ylabel.setValue(yCol.getField().getName());

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(yCol.getField().getName());
		series.getData().setAll(ChartModelUtils.toChartDataHistogram(xCol, yCol));

		data.clear();
		data.add(series);
	}

	public ObservableList<XYChart.Series<String, Number>> getData() {
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

	public FutureIndicatorViewModel getFutureIndicator() {
		return asyncQueryHandler;
	}
}
