package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.misc.KeyStatisticsQuery;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.table.StatisticsTable;

import java.sql.SQLException;
import java.util.Arrays;

public class KeyStatisticsViewModel {
	private final KeyStatisticsQuery query;

	// Properties read by the view
	public final ObservableList<StatisticsTable.StatisticsTableRow> data = FXCollections.observableArrayList();

	public ObservableList<StatisticsTable.StatisticsTableRow> getData() {
		return data;
	}

	public KeyStatisticsViewModel() {
		query = new KeyStatisticsQuery(DatabaseManager.getInstance());

		try {
			data.setAll(StatisticsTable.StatisticsTableRow.fromColumns(Arrays.asList(query.execute())));
		} catch (SQLException e) {
			Utils.openErrorDialog("Database error", "An error occurred inside of the database", e.toString());
			e.printStackTrace();
		}

		DatabaseManager.getInstance().reload.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				try {
					data.setAll(StatisticsTable.StatisticsTableRow.fromColumns(Arrays.asList(query.execute())));
				} catch (SQLException throwables) {
					Utils.openErrorDialog("Database error", "An error occurred inside of the database", throwables.toString());
					throwables.printStackTrace();
				}
			}
		}));
	}
}
