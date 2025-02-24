package uk.ac.soton.git.comp2211g17.view.components.table;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uk.ac.soton.git.comp2211g17.viewmodel.table.DataTableViewModel;
import uk.ac.soton.git.comp2211g17.viewmodel.util.VirtualListEntry;

public class DataTable extends TableView<VirtualListEntry<DataTableViewModel.DataTableRow>> {
	public final Property<DataTableViewModel> dataTableViewModelProperty = new SimpleObjectProperty<>();

	public DataTable() {
		setTableMenuButtonVisible(true);
		dataTableViewModelProperty.addListener((observable, oldValue, newValue) -> {
			getColumns().clear();
			for (int i = 0; i < newValue.fields.length; i++) {
				TableColumn<VirtualListEntry<DataTableViewModel.DataTableRow>, String> column =
					new TableColumn<>(newValue.fields[i].getName());
				int columnIndexFinal = i;
				column.setCellValueFactory(features -> new ObjectBinding<>() {
					{
						bind(features.getValue());
					}

					@Override
					protected String computeValue() {
						return features.getValue().getValue().data[columnIndexFinal];
					}
				});
				// TODO: have a configuration for default visibility
				getColumns().add(column);
			}
			setItems(newValue.data);
		});
	}
}
