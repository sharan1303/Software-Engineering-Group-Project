package uk.ac.soton.git.comp2211g17.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uk.ac.soton.git.comp2211g17.controller.MainWindowController;
import uk.ac.soton.git.comp2211g17.view.graph.Histogram;
import uk.ac.soton.git.comp2211g17.view.table.DataTable;

public class MainWindow extends BorderPane {
    private final MainWindowController controller;

    public MainWindow(MainWindowController controller) {
        this.controller = controller;

        BorderPane mainPane = new BorderPane();
        mainPane.setBackground(new Background(new BackgroundFill(new Color(63.0 / 255, 160.0 / 255, 251.0 / 255, 1.0), CornerRadii.EMPTY, Insets.EMPTY)));

        //middle pane

        VBox centerBox = new VBox();
        //mainBox.getChildren().add(new TimeGraph("Test Time Graph", controller.impressionCostGraphController));
        BorderPane graphBox = new BorderPane();
        graphBox.setCenter(new Histogram("Context Frequencies", controller.contextFrequencies));
        graphBox.setRight(new KeyMetricsPane(controller.keyMetricsPaneController));
        centerBox.getChildren().add(graphBox);

        VBox tableBox = new VBox();
        HBox tablePaginationBox = new HBox();

        Button prevButton = new Button("Prev");
        prevButton.setOnAction(evt -> {
            if (controller.testTableController.currentPage.get() > 0) {
                controller.testTableController.currentPage.set(controller.testTableController.currentPage.get() - 1);
            }
        });
        Label pageLabel = new Label("Page 1");
        pageLabel.setPadding(new Insets(0, 10, 0, 10));
        controller.testTableController.currentPage.addListener((observable, oldValue, newValue) -> pageLabel.setText("Page " + (newValue.intValue() + 1)));
        Button nextButton = new Button("Next");
        nextButton.setOnAction(evt -> controller.testTableController.currentPage.set(controller.testTableController.currentPage.get() + 1));
        tablePaginationBox.getChildren().addAll(prevButton, pageLabel, nextButton);

        tableBox.getChildren().add(tablePaginationBox);
        tableBox.getChildren().add(new DataTable(controller.testTableController));
        centerBox.getChildren().add(tableBox);

        centerBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20, 0, 0, 0, false), Insets.EMPTY)));
        centerBox.setPadding(Utils.getPadding());
        mainPane.setCenter(centerBox);

        //top app bar thing

        HBox appBar = new HBox(); //dunno what it's usually called;

        appBar.getChildren().add(Utils.titleText("Dashboard", Color.WHITE));
        appBar.getChildren().add(Utils.createHSpacer());
        appBar.getChildren().add(Utils.titleText("Home", Color.WHITE)); //todo Change this depending on what page they are on
        appBar.getChildren().add(Utils.createHSpacer());
        appBar.setSpacing(5.0);

        //load data button

        Button loadData = Utils.styleButton("Load Campaign");

        appBar.getChildren().add(loadData);

        appBar.setPadding(Utils.getPadding());

        mainPane.setTop(appBar);

        //side bar thing

        VBox sidePane = new VBox();

        Button homePageButton = Utils.styleButton("Home");

        Button sideViewButton = Utils.styleButton("Side View");

        Button settingsPageButton = Utils.styleButton("Settings");

        sidePane.setSpacing(5.0);

        sidePane.getChildren().addAll(homePageButton, sideViewButton, settingsPageButton, Utils.createVSpacer());

        sidePane.setPadding(Utils.getPadding());

        mainPane.setLeft(sidePane);


        centerProperty().set(mainPane);
    }
}
