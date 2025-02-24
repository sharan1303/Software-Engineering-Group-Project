package uk.ac.soton.git.comp2211g17.viewmodel.graph;

import javafx.scene.chart.XYChart;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.util.ArrayList;
import java.util.List;

public class ChartModelUtils {
	public static List<XYChart.Data<String, Number>> toChartDataHistogram(Column<String> xColumn, Column<? extends Number> yColumn) {
		List<XYChart.Data<String, Number>> data = new ArrayList<>();

		List<String> xData = xColumn.getData();
		List<? extends Number> yData = yColumn.getData();
		for (int i = 0; i < xColumn.getLength(); i++) {
			data.add(new XYChart.Data<>(xData.get(i), yData.get(i)));
		}

		return data;
	}

	public static List<XYChart.Data<Number, Number>> toChartDataTime(Column<? extends Number> xColumn, Column<? extends Number> yColumn) {
		List<XYChart.Data<Number, Number>> data = new ArrayList<>();

		List<? extends Number> xData = xColumn.getData();
		List<? extends Number> yData = yColumn.getData();
		for (int i = 0; i < xColumn.getLength(); i++) {
			data.add(new XYChart.Data<>(xData.get(i), yData.get(i)));
		}

		return data;
	}
}
