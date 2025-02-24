package uk.ac.soton.git.comp2211g17.view.main;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.easybind.EasyBind;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.KeyMetrics;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.view.components.table.DataTable;
import uk.ac.soton.git.comp2211g17.viewmodel.filters.FilterGroupViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.filters.TimeGranularityViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.graph.TimeGraphViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.table.DataTableViewModel;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailViewPage implements Initializable {
	public Navigation.Page root;
	public Label label;
	public Property<KeyMetrics> metric = new SimpleObjectProperty<>();
	public final TimeGraphViewModel timeGraphViewModel = new TimeGraphViewModel();

	public FilterGroupViewModel filterGroupController;
	public TimeGranularityViewModel timeGranularityController;

	public DataTable table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timeGraphViewModel.timeGranularity.bind(timeGranularityController.granularityValue);
		metric.bind(EasyBind.map(root.metadataProperty(), metadata -> {
			if (metadata == null) {
				return null;
			}
			for (KeyMetrics metric : KeyMetrics.values()) {
				if (metadata.equals(metric.toString())) {
					return metric;
				}
			}
			return null;
		}));
		metric.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				timeGraphViewModel.setQuery(newValue.construct(DatabaseManager.getInstance()));
				table.dataTableViewModelProperty.setValue(new DataTableViewModel(newValue.constructRawData(DatabaseManager.getInstance())));
			}
		});
		filterGroupController.filterButton.addEventHandler(ActionEvent.ACTION, e -> {
			timeGraphViewModel.filter.setValue(filterGroupController.getFilter());
		});
	}

	public KeyMetrics getMetric() {
		return metric.getValue();
	}

	public Property<KeyMetrics> metricProperty() {
		return metric;
	}

	public TimeGraphViewModel getTimeGraphViewModel() {
		return timeGraphViewModel;
	}

	public void audienceButtonClicked(){
		var a = Utils.loadFXMLWithController("fxml/Filters/FilterGroup.fxml");
		AnchorPane audience = (AnchorPane) a.getKey();
		FilterGroupViewModel fgvm = (FilterGroupViewModel) a.getValue();
		VBox detailViewPage = (VBox) root.getContent();
		HBox hb = (HBox) detailViewPage.getChildren().get(0);
		BorderPane bp = (BorderPane) hb.getChildren().get(1);
		bp.setCenter(audience);
	}

	public void contextButtonClicked() {
//		root.getContent().

	}

	public void dateButtonClicked() {

	}
}
