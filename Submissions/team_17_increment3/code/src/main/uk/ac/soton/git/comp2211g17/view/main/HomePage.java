package uk.ac.soton.git.comp2211g17.view.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.Subscription;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.viewmodel.KeyStatisticsViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.OpenedGraphsViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.ClickCostHistogramViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.HistogramViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.util.IdempotentMappedObservableList;

import java.net.URL;
import java.util.ResourceBundle;

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

	Subscription sub;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sub = EasyBind.listBind(keyMetricsViews.getChildren(), new IdempotentMappedObservableList<>(openedGraphsViewModel.openedGraphs, metric -> {
			Pair<HBox, FXMLLoader> loaderPair = Utils.loadFXMLWithLoader("fxml/MainWindow/KeyMetricsView.fxml");

			KeyMetricsView view = loaderPair.getValue().getController();
			view.metric.setValue(metric);
			view.openedGraphsViewModel = openedGraphsViewModel;
			view.navigation = root.getParentNavigation();

			return loaderPair.getKey();
		}));
	}

	public void onAddGraph(ActionEvent actionEvent) {
		if (keyMetricsDropdown.getValue() != null) {
			openedGraphsViewModel.openedGraphs.add(keyMetricsDropdown.getValue());
		}
	}

}
