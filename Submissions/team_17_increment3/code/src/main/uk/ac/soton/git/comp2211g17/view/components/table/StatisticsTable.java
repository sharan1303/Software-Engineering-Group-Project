package uk.ac.soton.git.comp2211g17.view.components.table;

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends VBox {
	@FXML
	private Label title;
	@FXML
	private GridPane contents;

	private final ListProperty<StatisticsTableRow> data = new SimpleListProperty<>();

	public StatisticsTable() {
		Utils.loadFXMLAsComponent("fxml/components/StatisticsTable.fxml", this);
		data.addListener((observable, oldValue, newValue) -> {
			newValue.addListener((ListChangeListener<? super StatisticsTableRow>) change -> {
				contents.getChildren().clear();
				for (int i = 0; i < change.getList().size(); i++) {
					StatisticsTableRow row = change.getList().get(i);
					Label nameLabel = new Label(row.name);
					GridPane.setConstraints(nameLabel, 0, i);
					Label dataLabel = new Label(row.data);
					GridPane.setConstraints(dataLabel, 1, i);
					contents.getChildren().addAll(nameLabel, dataLabel);
				}
			});
		});
	}

	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public Property<String> titleProperty() {
		return this.title.textProperty();
	}

	public ObservableList<StatisticsTableRow> getData() {
		return data.get();
	}

	public void setData(ObservableList<StatisticsTableRow> data) {
		this.data.set(data);
	}

	public ListProperty<StatisticsTableRow> dataProperty() {
		return data;
	}

	public static class StatisticsTableRow {
		public final String name;
		public final String data;

		public StatisticsTableRow(String name, String data) {
			this.name = name;
			this.data = data;
		}

		public static List<StatisticsTableRow> fromColumns(List<Column<?>> data) {
			List<StatisticsTableRow> rows = new ArrayList<>();
			for (Column<?> col : data) {
				List<String> colData = col.getDataAsFormatted();
				if (colData.size() > 0) {
					rows.add(new StatisticsTableRow(col.getField().getName(), colData.get(0)));
				}
			}
			return rows;
		}
	}
}
