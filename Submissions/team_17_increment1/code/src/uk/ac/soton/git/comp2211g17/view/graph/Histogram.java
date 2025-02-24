package uk.ac.soton.git.comp2211g17.view.graph;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import uk.ac.soton.git.comp2211g17.controller.ChartController;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.column.LongIntegerColumn;

public class Histogram extends BarChart<String, Number> {

    public Histogram(String title, ChartController<Column<?>, LongIntegerColumn> controller) {
        super(new CategoryAxis(), new NumberAxis());
        setTitle(title);
        setAnimated(true);
        setBarGap(0);
        setCategoryGap(0);
        setData(controller.getChartDataProperty().getValue());
        controller.getChartDataProperty().addListener((observable, oldValue, newValue) -> setData(newValue));
    }

    public void setData(ChartController.ChartData<Column<?>, LongIntegerColumn> data) {
        getData().clear();
        Column<?> intervalColumn = data.horizontalAxis;
        LongIntegerColumn frequencyColumn = data.verticalAxis;
        getXAxis().setLabel(intervalColumn.getField().getName());
        getYAxis().setLabel(frequencyColumn.getField().getName());
        Series<String, Number> series = new Series<>();
        series.setName(intervalColumn.getField().getName() + " Frequency");
        for (int i = 0; i < intervalColumn.getLength(); i++)
            series.getData().add(
                new Data<>(intervalColumn.getDataAsFormatted()[i], frequencyColumn.getDataAsLongInteger()[i]));
        getData().add(series);
    }
}