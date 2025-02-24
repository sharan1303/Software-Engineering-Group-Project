package uk.ac.soton.git.comp2211g17.controller;

import javafx.beans.property.SimpleObjectProperty;
import uk.ac.soton.git.comp2211g17.model.query.impl.CategoryFrequencyQuery;
import uk.ac.soton.git.comp2211g17.model.query.impl.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.impl.ImpressionLogQuery;
import uk.ac.soton.git.comp2211g17.model.query.impl.KeyMetricsQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.column.ContinuousNumericColumn;
import uk.ac.soton.git.comp2211g17.model.types.column.DateTimeColumn;
import uk.ac.soton.git.comp2211g17.model.types.column.LongIntegerColumn;
import uk.ac.soton.git.comp2211g17.model.types.field.ContinuousNumericField;
import uk.ac.soton.git.comp2211g17.model.types.field.DateTimeField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class MainWindowController {
	private final SimpleObjectProperty<ChartController.ChartData<DateTimeColumn, ContinuousNumericColumn>> impressionCostGraphData;
	public final ChartController<DateTimeColumn, ContinuousNumericColumn> impressionCostGraphController;

	private final SimpleObjectProperty<ChartController.ChartData<Column<?>, LongIntegerColumn>> contextFrequenciesData;
	public final ChartController<Column<?>, LongIntegerColumn> contextFrequencies;

	public final DataTableController testTableController;

	public final KeyMetricsPaneController keyMetricsPaneController;

	public MainWindowController() {
		DatabaseManager dbm = new DatabaseManager();

		DateTimeField date = new DateTimeField("Date");
		ContinuousNumericField impressionCost = new ContinuousNumericField("Impression Cost");

		impressionCostGraphData = new SimpleObjectProperty<>(new ChartController.ChartData<>(
			date.makeColumn(new LocalDateTime[]{LocalDateTime.now(),
				LocalDateTime.of(2016, 12, 25, 7, 0),
				LocalDateTime.of(2020, 3, 26, 17, 0)}),
			impressionCost.makeColumn(new float[]{0.05f, 0.001f, 0.1f})));
		impressionCostGraphController = () -> impressionCostGraphData;

		Column<?>[] cols;
		try {
			cols = new CategoryFrequencyQuery(dbm).execute();
		} catch (SQLException e) {
			e.printStackTrace();
			cols = new Column<?>[]{};
		}
		contextFrequenciesData = new SimpleObjectProperty<>(new ChartController.ChartData<>(
			cols[0],
			(LongIntegerColumn) cols[1]
		));
		contextFrequencies = () -> contextFrequenciesData;

		testTableController = new DataTableController(new ImpressionLogQuery(dbm), 10);

		keyMetricsPaneController = new KeyMetricsPaneController(new KeyMetricsQuery(dbm));
	}

}
