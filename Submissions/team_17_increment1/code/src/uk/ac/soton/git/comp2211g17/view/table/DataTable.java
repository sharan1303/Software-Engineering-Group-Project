package uk.ac.soton.git.comp2211g17.view.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uk.ac.soton.git.comp2211g17.controller.DataTableController;
import uk.ac.soton.git.comp2211g17.model.types.Column;

import java.util.List;

public class DataTable extends TableView<DataTable.DataTableRow> {
	public DataTable(DataTableController controller) {
		setData(controller.data.getValue());
		controller.data.addListener((observable, oldValue, newValue) -> setData(newValue));
	}

	private void setData(List<Column<?>> data) {
		getColumns().clear();

		String[][] colsData = new String[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			TableColumn<DataTableRow, String> tableCol = new TableColumn<>(data.get(i).getField().getName());
			int finalIndex = i;
			tableCol.setCellValueFactory(
				(features) -> new SimpleStringProperty(features.getValue().data[finalIndex]));
			getColumns().add(tableCol);

			colsData[i] = data.get(i).getDataAsFormatted();
		}

		getItems().clear();
		// Iterate over each row
		for (int i = 0; i < colsData[0].length; i++) {
			String[] rowData = new String[data.size()];
			// Get the data from each column
			for (int j = 0; j < data.size(); j++) {
				rowData[j] = colsData[j][i];
			}

			getItems().add(new DataTableRow(rowData));
		}
	}

	public static class DataTableRow {
		public final String[] data;

		private DataTableRow(String[] data) {
			this.data = data;
		}
	}
}
