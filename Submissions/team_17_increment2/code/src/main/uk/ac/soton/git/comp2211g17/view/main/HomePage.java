package uk.ac.soton.git.comp2211g17.view.main;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.viewmodel.KeyStatisticsViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.OpenedGraphsViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.ClickCostHistogramViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.HistogramViewModel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomePage implements Initializable {
	public Navigation.Page root;
	public VBox mainContent;

	public KeyStatisticsViewModel keyStatisticsViewModel = new KeyStatisticsViewModel();
	public HistogramViewModel clickCostHistogramViewModel = new ClickCostHistogramViewModel();
	public OpenedGraphsViewModel openedGraphsViewModel = new OpenedGraphsViewModel();

	public VBox keyMetricsViews;

	public KeyStatisticsViewModel getKeyStatisticsViewModel() {
		return keyStatisticsViewModel;
	}

	public HistogramViewModel getClickCostHistogramViewModel() {
		return clickCostHistogramViewModel;
	}

	public OpenedGraphsViewModel getOpenedGraphsViewModel() {
		return openedGraphsViewModel;
	}

	@FXML
	ChoiceBox<KeyMetrics> keyMetricsDropdown;

	@SuppressWarnings("CodeBlock2Expr")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		openedGraphsViewModel.openedGraphs.addListener((ListChangeListener<? super KeyMetrics>) c -> {
			keyMetricsViews.getChildren().setAll(openedGraphsViewModel.openedGraphs.stream().map(metric -> {
				Pair<HBox, FXMLLoader> loaderPair = Utils.loadFXMLWithLoader("fxml/MainWindow/KeyMetricsView.fxml");

				KeyMetricsView view = loaderPair.getValue().getController();
				view.metric.setValue(metric);
				view.openedGraphsViewModel = openedGraphsViewModel;
				view.navigation = root.getParentNavigation();

				return loaderPair.getKey();
			}).collect(Collectors.toList()));
		});
	}

	public void onAddGraph(ActionEvent actionEvent) {
		if (keyMetricsDropdown.getValue() != null) {
			openedGraphsViewModel.openedGraphs.add(keyMetricsDropdown.getValue());
		}
	}

}
