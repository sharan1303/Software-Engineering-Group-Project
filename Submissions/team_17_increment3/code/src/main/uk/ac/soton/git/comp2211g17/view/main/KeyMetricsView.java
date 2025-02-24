package uk.ac.soton.git.comp2211g17.view.main;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.view.components.graph.TimeGraph;
import uk.ac.soton.git.comp2211g17.viewmodel.KeyMetricsViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.OpenedGraphsViewModel;

public class KeyMetricsView {
	public Property<KeyMetrics> metric = new SimpleObjectProperty<>();
	public OpenedGraphsViewModel openedGraphsViewModel;

	public KeyMetricsViewModel keyMetricsViewModel = new KeyMetricsViewModel(metric);
	public TimeGraph chart;

	public Navigation navigation;

	public KeyMetricsViewModel getKeyMetricsViewModel() {
		return keyMetricsViewModel;
	}

	public KeyMetrics getMetric() {
		return metric.getValue();
	}

	public Property<KeyMetrics> metricProperty() {
		return metric;
	}

	public void openDetail(ActionEvent actionEvent) {
		navigation.navigate("detail#" + metric.getValue());
	}

	public void saveChart(ActionEvent actionEvent) {
		Utils.saveChart((Stage) chart.getScene().getWindow(), chart);
	}

	public void removeSelf(ActionEvent actionEvent) {
		openedGraphsViewModel.openedGraphs.remove(metric.getValue());
	}
}
