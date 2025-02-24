package uk.ac.soton.git.comp2211g17.view.components.graph;

import javafx.beans.property.Property;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import uk.ac.soton.git.comp2211g17.view.Utils;

public class Histogram extends BarChart<String, Number> {
	private final Property<String> xlabel = getXAxis().labelProperty();
	private final Property<String> ylabel = getYAxis().labelProperty();

	public Histogram() {
		super(new CategoryAxis(), new NumberAxis());
		// Hack to fix sizes on focus
		this.getXAxis().setStyle("-fx-font-size: 10px");
		this.getYAxis().setStyle("-fx-font-size: 10px");
		setBarGap(1);
		setCategoryGap(1);
		setAnimated(false);

		Utils.addSaveChartMenu(this);
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
