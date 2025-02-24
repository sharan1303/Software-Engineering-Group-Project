package uk.ac.soton.git.comp2211g17.view.components.graph;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.easybind.EasyBind;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static uk.ac.soton.git.comp2211g17.view.Utils.propBound;

public class DuoGraph extends VBox implements Initializable {

    /**Stores the text to switch to combined mode.*/
    public static final String COMBINED_TEXT = "Combined";
    /**Stores the text to switch to Duo mode.*/
    public static final String DUO_TEXT = "Duo";

    private final BooleanProperty duoMode = new SimpleBooleanProperty(true);
    private final ReadOnlyStringProperty viewToggleText =
        propBound(new SimpleStringProperty(), EasyBind.map(duoMode, duo -> duo ? COMBINED_TEXT : DUO_TEXT));

    private final Property<DateTimeFormatter> formatter = new SimpleObjectProperty<>(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    private final ListProperty<XYChart.Series<Number, Number>> dataLeft = new SimpleListProperty<>(FXCollections.emptyObservableList());
    private final ListProperty<XYChart.Series<Number, Number>> dataLeftInternal = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<XYChart.Series<Number, Number>> dataRight = new SimpleListProperty<>(FXCollections.emptyObservableList());
    private final ListProperty<XYChart.Series<Number, Number>> dataRightInternal = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final Property<String> xlabelLeft = new SimpleStringProperty();
    private final Property<String> ylabelLeft = new SimpleStringProperty();
    private final Property<String> xlabelRight = new SimpleStringProperty();
    private final Property<String> ylabelRight = new SimpleStringProperty();
    private final Property<String> titleLeft = new SimpleStringProperty();
    private final Property<String> titleRight = new SimpleStringProperty();

    private final ListProperty<XYChart.Series<Number, Number>> dataCombined = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ReadOnlyStringProperty xlabelCombined = propBound(new SimpleStringProperty(),
        EasyBind.combine(xlabelLeft, xlabelRight, DuoGraph::combineAmpersand));
    private final ReadOnlyStringProperty ylabelCombined = propBound(new SimpleStringProperty(),
        EasyBind.combine(ylabelLeft, ylabelRight, DuoGraph::combineAmpersand));
    private final ReadOnlyStringProperty titleCombined = propBound(new SimpleStringProperty(),
        EasyBind.combine(titleLeft, titleRight, DuoGraph::combineAmpersand));

    private static String combineAmpersand(String left, String right) {
        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }
        if (left.equals(right)) {
            return left;
        } else {
            return left + " & " + right;
        }
    }

    @FXML
    private HBox graphBox;
    @FXML
    private TimeGraph firstGraph;
    @FXML
    private TimeGraph secondGraph;
    @FXML
    private TimeGraph combinedGraph;

    public DuoGraph() {
        Utils.loadFXMLAsComponent("fxml/components/DuoGraph.fxml", this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EasyBind.subscribe(duoMode, duo -> {
            update();
            if (duo) {
                graphBox.getChildren().setAll(firstGraph, secondGraph);
            } else {
                graphBox.getChildren().setAll(combinedGraph);
            }
        });

        dataLeft.addListener((InvalidationListener) observable -> {
            update();
        });
        dataRight.addListener((InvalidationListener) observable -> {
            update();
        });
    }

    private void update() {
        dataCombined.clear();
        dataLeftInternal.clear();
        dataRightInternal.clear();
        if (duoMode.get()) {
            dataLeftInternal.addAll(dataLeft);
            dataRightInternal.addAll(dataRight);
        } else {
            dataCombined.addAll(dataLeft);
            dataCombined.addAll(dataRight);
        }
    }

    public void toggleMode(ActionEvent actionEvent) {
        duoMode.set(!duoMode.get());
    }

    public boolean isDuoMode() {
        return duoMode.get();
    }

    public BooleanProperty duoModeProperty() {
        return duoMode;
    }

    public void setDuoMode(boolean duoMode) {
        this.duoMode.set(duoMode);
    }

    public String getViewToggleText() {
        return viewToggleText.get();
    }

    public ReadOnlyStringProperty viewToggleTextProperty() {
        return viewToggleText;
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataLeft() {
        return dataLeft.get();
    }

    public ListProperty<XYChart.Series<Number, Number>> dataLeftProperty() {
        return dataLeft;
    }

    public void setDataLeft(ObservableList<XYChart.Series<Number, Number>> dataLeft) {
        this.dataLeft.set(dataLeft);
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataRight() {
        return dataRight.get();
    }

    public ListProperty<XYChart.Series<Number, Number>> dataRightProperty() {
        return dataRight;
    }

    public void setDataRight(ObservableList<XYChart.Series<Number, Number>> dataRight) {
        this.dataRight.set(dataRight);
    }

    public String getXlabelLeft() {
        return xlabelLeft.getValue();
    }

    public Property<String> xlabelLeftProperty() {
        return xlabelLeft;
    }

    public void setXlabelLeft(String xlabelLeft) {
        this.xlabelLeft.setValue(xlabelLeft);
    }

    public String getYlabelLeft() {
        return ylabelLeft.getValue();
    }

    public Property<String> ylabelLeftProperty() {
        return ylabelLeft;
    }

    public void setYlabelLeft(String ylabelLeft) {
        this.ylabelLeft.setValue(ylabelLeft);
    }

    public String getXlabelRight() {
        return xlabelRight.getValue();
    }

    public Property<String> xlabelRightProperty() {
        return xlabelRight;
    }

    public void setXlabelRight(String xlabelRight) {
        this.xlabelRight.setValue(xlabelRight);
    }

    public String getYlabelRight() {
        return ylabelRight.getValue();
    }

    public Property<String> ylabelRightProperty() {
        return ylabelRight;
    }

    public void setYlabelRight(String ylabelRight) {
        this.ylabelRight.setValue(ylabelRight);
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

    public String getTitleLeft() {
        return titleLeft.getValue();
    }

    public Property<String> titleLeftProperty() {
        return titleLeft;
    }

    public void setTitleLeft(String titleLeft) {
        this.titleLeft.setValue(titleLeft);
    }

    public String getTitleRight() {
        return titleRight.getValue();
    }

    public Property<String> titleRightProperty() {
        return titleRight;
    }

    public void setTitleRight(String titleRight) {
        this.titleRight.setValue(titleRight);
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataCombined() {
        return dataCombined;
    }

    public String getXlabelCombined() {
        return xlabelCombined.get();
    }

    public ReadOnlyStringProperty xlabelCombinedProperty() {
        return xlabelCombined;
    }

    public String getYlabelCombined() {
        return ylabelCombined.get();
    }

    public ReadOnlyStringProperty ylabelCombinedProperty() {
        return ylabelCombined;
    }

    public String getTitleCombined() {
        return titleCombined.get();
    }

    public ReadOnlyStringProperty titleCombinedProperty() {
        return titleCombined;
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataLeftInternal() {
        return dataLeftInternal;
    }

    public ObservableList<XYChart.Series<Number, Number>> getDataRightInternal() {
        return dataRightInternal;
    }
}