package uk.ac.soton.git.comp2211g17.view.main;

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.model.query.metrics.KeyMetricQuery;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.viewmodel.filters.DuoFilterGroupViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.filters.TimeGranularityViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.TimeGraphViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.util.FutureIndicatorJoinWrapper;

import java.net.URL;
import java.util.ResourceBundle;

public class SideBySidePage implements Initializable {
	public Navigation.Page root;
	public Label label;
	public ListProperty<KeyMetrics> metricList = new SimpleListProperty<>(FXCollections.observableArrayList(KeyMetrics.values()));

	public Property<KeyMetrics> metricLeft = new SimpleObjectProperty<>();
	public final TimeGraphViewModel timeGraphViewModelLeft = new TimeGraphViewModel();
	public ChoiceBox<KeyMetrics> keyMetricsDropdownLeft;

	public Property<KeyMetrics> metricRight = new SimpleObjectProperty<>();
	public final TimeGraphViewModel timeGraphViewModelRight = new TimeGraphViewModel();
	public ChoiceBox<KeyMetrics> keyMetricsDropdownRight;

	public DuoFilterGroupViewModel filterGroupController;
	public TimeGranularityViewModel timeGranularityController;

	public final FutureIndicatorJoinWrapper futureIndicator = new FutureIndicatorJoinWrapper(timeGraphViewModelLeft.getFutureIndicator(), timeGraphViewModelRight.getFutureIndicator());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		keyMetricsDropdownLeft.valueProperty().bindBidirectional(metricLeft);
		keyMetricsDropdownRight.valueProperty().bindBidirectional(metricRight);

		timeGraphViewModelLeft.timeGranularity.bind(timeGranularityController.granularityValue);
		timeGraphViewModelRight.timeGranularity.bind(timeGranularityController.granularityValue);

		filterGroupController.filterButton.addEventHandler(ActionEvent.ACTION, e -> {
			timeGraphViewModelLeft.filter.setValue(filterGroupController.getFilterLeft());
			timeGraphViewModelRight.filter.setValue(filterGroupController.getFilterRight());
		});

		metricLeft.addListener((observable, oldValue, newValue) -> {
			KeyMetricQuery query = newValue.construct(DatabaseManager.getInstance());
			query.setInterval(3600);
			timeGraphViewModelLeft.setQuery(query);
		});
		metricRight.addListener((observable, oldValue, newValue) -> {
			KeyMetricQuery query = newValue.construct(DatabaseManager.getInstance());
			query.setInterval(3600);
			timeGraphViewModelRight.setQuery(query);
		});
		metricLeft.setValue(KeyMetrics.IMPRESSIONS);
		metricRight.setValue(KeyMetrics.CPM);
	}

	public KeyMetrics getMetricLeft() {
		return metricLeft.getValue();
	}

	public Property<KeyMetrics> metricLeftProperty() {
		return metricLeft;
	}

	public TimeGraphViewModel getTimeGraphViewModelLeft() {
		return timeGraphViewModelLeft;
	}

	public KeyMetrics getMetricRight() {
		return metricRight.getValue();
	}

	public Property<KeyMetrics> metricRightProperty() {
		return metricRight;
	}

	public void setMetricRight(KeyMetrics metricRight) {
		this.metricRight.setValue(metricRight);
	}

	public TimeGraphViewModel getTimeGraphViewModelRight() {
		return timeGraphViewModelRight;
	}

	public ObservableList<KeyMetrics> getMetricList() {
		return metricList.get();
	}

	public ListProperty<KeyMetrics> metricListProperty() {
		return metricList;
	}

	public void setMetricList(ObservableList<KeyMetrics> metricList) {
		this.metricList.set(metricList);
	}

	public FutureIndicatorJoinWrapper getFutureIndicator() {
		return futureIndicator;
	}
}
