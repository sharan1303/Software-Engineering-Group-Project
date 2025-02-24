package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.async.AsyncQueryHandler;
import uk.ac.soton.git.comp2211g17.model.query.misc.KeyStatisticsQuery;
import uk.ac.soton.git.comp2211g17.view.components.table.StatisticsTable;
import uk.ac.soton.git.comp2211g17.viewmodel.async.FutureIndicatorViewModel;

import java.util.Arrays;

public class KeyStatisticsViewModel {
	// Properties read by the view
	public final ObservableList<StatisticsTable.StatisticsTableRow> data = FXCollections.observableArrayList();

	private final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler();

	public ObservableList<StatisticsTable.StatisticsTableRow> getData() {
		return data;
	}

	public KeyStatisticsViewModel() {
		asyncQueryHandler.await(result ->
			data.setAll(StatisticsTable.StatisticsTableRow.fromColumns(Arrays.asList(result))));
		asyncQueryHandler.setQuery(new KeyStatisticsQuery(DatabaseManager.getInstance()));
	}

	public FutureIndicatorViewModel getFutureIndicator() {
		return asyncQueryHandler;
	}
}
