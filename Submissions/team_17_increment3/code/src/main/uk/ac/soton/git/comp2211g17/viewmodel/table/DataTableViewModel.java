package uk.ac.soton.git.comp2211g17.viewmodel.table;

import javafx.collections.ObservableList;
import uk.ac.soton.git.comp2211g17.model.query.PaginatableQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.viewmodel.util.VirtualListEntry;
import uk.ac.soton.git.comp2211g17.viewmodel.util.VirtualListProvider;
import uk.ac.soton.git.comp2211g17.viewmodel.util.VirtualObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataTableViewModel {
	private final PaginatableQuery query;

	// Properties read by the view
	public final ObservableList<VirtualListEntry<DataTableRow>> data;
	public final Field<?>[] fields;

	// Properties set by the view
	// None yet, virtual pagination sorts out a ton for us

	// TODO: sorting, filtering of columns

	public DataTableViewModel(PaginatableQuery query) {
		this.query = query;
		this.fields = query.getFields();

		this.data = new VirtualObservableList<>(new VirtualListProvider<>() {
			@Override
			public int getLength() {
				try {
					return query.getLength();
				} catch (SQLException e) {
					Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
					e.printStackTrace();
					// TODO: handle properly
				}
				return 0;
			}

			@Override
			public DataTableRow[] getData(int offset, int length) {
				query.setOffset(offset);
				query.setLimit(length);
				try {
					Column<?>[] data = query.execute();
					if (data.length > 0) {
						List<List<String>> strData = new ArrayList<>();
						for (Column<?> col : data) {
							strData.add(col.getDataAsFormatted());
						}

						DataTableRow[] rows = new DataTableRow[strData.get(0).size()];
						for (int i = 0; i < rows.length; i++) {
							String[] rowData = new String[strData.size()];
							// Get the data from each column
							for (int j = 0; j < strData.size(); j++) {
								rowData[j] = strData.get(j).get(i);
							}
							rows[i] = new DataTableRow(rowData);
						}
						return rows;
					}
				} catch (SQLException e) {
					Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
					e.printStackTrace();
					// TODO: handle properly
				}
				// Failed to retrieve data
				return new DataTableRow[0];
			}
		});
	}

	public static class DataTableRow {
		public final String[] data;

		private DataTableRow(String[] data) {
			this.data = data;
		}
	}
}
