package uk.ac.soton.git.comp2211g17.view.graph;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import uk.ac.soton.git.comp2211g17.controller.ChartController;
import uk.ac.soton.git.comp2211g17.model.types.column.ContinuousNumericColumn;
import uk.ac.soton.git.comp2211g17.model.types.column.DateTimeColumn;

public class TimeGraph extends LineChart<String, Number> {

    public TimeGraph(String title, ChartController<DateTimeColumn, ContinuousNumericColumn> controller) {
        super(new CategoryAxis(), new NumberAxis());
        setTitle(title);
        setAnimated(true);
        setData(controller.getChartDataProperty().getValue());
        controller.getChartDataProperty().addListener((observable, oldValue, newValue) -> setData(newValue));
    }

    public void setData(ChartController.ChartData<DateTimeColumn, ContinuousNumericColumn> data) {
        getData().clear();
        DateTimeColumn timeColumn = data.horizontalAxis;
        ContinuousNumericColumn dataColumn = data.verticalAxis;
        getXAxis().setLabel(timeColumn.getField().getName());
        getYAxis().setLabel(dataColumn.getField().getName());
        Series<String, Number> series = new Series<>();
        series.setName(dataColumn.getField().getName() + " over Time");
        for (int i = 0; i < timeColumn.getLength(); i++)
            series.getData().add(
                new Data<>(timeColumn.getDataAsFormatted()[i], dataColumn.getDataAsContinuousNumeric()[i]));
        getData().add(series);
    }
}