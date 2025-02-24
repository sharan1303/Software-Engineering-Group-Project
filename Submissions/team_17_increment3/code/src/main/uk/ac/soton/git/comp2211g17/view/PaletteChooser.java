package uk.ac.soton.git.comp2211g17.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.git.comp2211g17.view.main.MainWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class PaletteChooser implements Initializable {
    public BorderPane root;
    public VBox elegant;
    public VBox zesty;
    public VBox corporate;
    public VBox retro;
    private String palette = "zesty";

    private Node mainWindowRoot;
    private Stage stage;
    private MainWindow mainWindow;

    public void setMainWindowRoot(Node mainWindowRoot) {
        this.mainWindowRoot = mainWindowRoot;
    }

    public void setMainWindow(MainWindow mainWindow) { this.mainWindow = mainWindow; }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        zesty.getStyleClass().add("selected");
    }

    public void zestyClicked(MouseEvent mouseEvent) {
        selectPalette("zesty");
    }

    public void elegantClicked(MouseEvent mouseEvent) {
        selectPalette("elegant");
    }

    public void corporateClicked(MouseEvent mouseEvent) {
        selectPalette("corporate");
    }

    public void retroClicked(MouseEvent mouseEvent) {
        selectPalette("retro");
    }

    public void selectPalette(String palette) {
        root.getStyleClass().removeAll("zesty", "elegant", "corporate", "retro");
        zesty.getStyleClass().remove("selected");
        elegant.getStyleClass().remove("selected");
        corporate.getStyleClass().remove("selected");
        retro.getStyleClass().remove("selected");

        root.getStyleClass().add(palette);

        this.palette = palette;

        switch (palette) {
            case "elegant": elegant.getStyleClass().add("selected"); break;
            case "corporate": corporate.getStyleClass().add("selected"); break;
            case "retro": retro.getStyleClass().add("selected"); break;
            case "zesty": default: zesty.getStyleClass().add("selected"); break;
        }
    }
    public void setPalette(ActionEvent actionEvent) {
        this.mainWindowRoot.getStyleClass().removeAll("zesty", "elegant", "corporate", "retro");
        this.mainWindowRoot.getStyleClass().add(palette);

        this.mainWindow.savePaletteProp(palette);

        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void cancel(ActionEvent actionEvent) {
        this.stage.close();
    }
}
