package uk.ac.soton.git.comp2211g17.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.git.comp2211g17.controller.KeyMetricsPaneController;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.util.List;

public class KeyMetricsPane extends VBox {
	private final GridPane grid = new GridPane();

	public KeyMetricsPane(KeyMetricsPaneController controller) {
		Label title = new Label("Key Metrics");
		title.setStyle("-fx-font-weight: bold");
		getChildren().add(title);
		getChildren().add(grid);

		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		setData(controller.data.getValue());
		controller.data.addListener(((observable, oldValue, newValue) -> {
			setData(newValue);
		}));
	}

	private void setData(List<Column<?>> data) {
		grid.getChildren().clear();
		for (int i = 0; i < data.size(); i++) {
			Label nameLabel = new Label(data.get(i).getField().getName());
			GridPane.setConstraints(nameLabel, 0, i);
			Label dataLabel = new Label(data.get(i).getDataAsFormatted()[0]);
			GridPane.setConstraints(dataLabel, 1, i);
			grid.getChildren().addAll(nameLabel, dataLabel);
		}
	}
}
