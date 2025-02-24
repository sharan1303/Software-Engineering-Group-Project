package uk.ac.soton.git.comp2211g17.view.components.graph;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.util.StringConverter;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeGraph extends LineChart<Number, Number> {
    private final Property<String> xlabel = getXAxis().labelProperty();
    private final Property<String> ylabel = getYAxis().labelProperty();
    private final Property<DateTimeFormatter> formatter = new SimpleObjectProperty<>(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public TimeGraph() {
        super(new NumberAxis(), new NumberAxis());
        // Hack to fix sizes on focus
        this.getXAxis().setStyle("-fx-font-size: 10px");
        this.getYAxis().setStyle("-fx-font-size: 10px");
        ((NumberAxis) this.getXAxis()).setForceZeroInRange(false);
        ((NumberAxis) this.getXAxis()).setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return formatter.getValue().format(Instant.ofEpochSecond(object.longValue()).atZone(ZoneId.systemDefault()));
            }

            @Override
            public Number fromString(String string) {
                return Instant.from(formatter.getValue().parse(string)).getEpochSecond();
            }
        });
        setCreateSymbols(false);
        setAnimated(false);

        Utils.addSaveChartMenu(this);
    }

    public String getXlabel() {
        return xlabel.getValue();
    }

    public void setXlabel(String value) {
        xlabel.setValue(value);
    }

    public Property<String> xlabelProperty() {
        return xlabel;
    }

    public String getYlabel() {
        return ylabel.getValue();
    }

    public void setYlabel(String value) {
        ylabel.setValue(value);
    }

    public Property<String> ylabelProperty() {
        return ylabel;
    }

    public DateTimeFormatter getFormatter() {
        return formatter.getValue();
    }

    public Property<DateTimeFormatter> formatterProperty() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter.setValue(formatter);
    }
}