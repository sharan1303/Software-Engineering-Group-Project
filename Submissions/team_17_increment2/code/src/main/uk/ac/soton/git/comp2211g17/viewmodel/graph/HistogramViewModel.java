package uk.ac.soton.git.comp2211g17.viewmodel.graph;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.ColumnFormatter;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.sql.SQLException;

public class HistogramViewModel {
	public final Property<Query> query = new SimpleObjectProperty<>();
	public final Property<String> xlabel = new SimpleStringProperty();
	public final Property<String> ylabel = new SimpleStringProperty();
	public final ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
	// TODO: add filters, time granularity

	public HistogramViewModel() {
		query.addListener((observable) -> update());
		DatabaseManager.getInstance().reload.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				update();
			}
		}));
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
		// Execute the query
		Column<String> xCol;
		Column<? extends Number> yCol;
		try {
			Column<?>[] cols = query.getValue().execute();
			if (cols.length < 2) {
				throw new SQLException("Incorrect number of fields");
			}
			//noinspection unchecked
			xCol = new ColumnFormatter<>((Field<Object>) cols[0].getField(), cols[0].getData());
			//noinspection unchecked
			yCol = (Column<? extends Number>) cols[1];
		} catch (SQLException e) {
			Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
			e.printStackTrace();
			return;
		}

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
}
