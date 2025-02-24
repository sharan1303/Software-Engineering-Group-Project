package uk.ac.soton.git.comp2211g17.view;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Utils {
    private static class FXMLInitException extends RuntimeException {
        public FXMLInitException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(Utils.class.getClassLoader().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            throw new FXMLInitException("Failed to load FXML data", e);
        }
    }

    public static <T> Pair<T, FXMLLoader> loadFXMLWithLoader(String path) {
        FXMLLoader loader = new FXMLLoader(Utils.class.getClassLoader().getResource(path));
        try {
            T component = loader.load();
            return new Pair<>(component, loader);
        } catch (IOException e) {
            throw new FXMLInitException("Failed to load FXML data", e);
        }
    }

    public static <T> T loadFXMLAsComponent(String path, Object root) {
        FXMLLoader loader = new FXMLLoader(Utils.class.getClassLoader().getResource(path));
        loader.setRoot(root);
        loader.setControllerFactory(cl -> root);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new FXMLInitException("Failed to load FXML data", e);
        }
    }

    public static void openErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static int openConfirmationDialog(String title, String header, String content, String... options) {
        return openConfirmationDialog(title, header, content, true, options);
    }
    public static int openConfirmationDialog(String title, String header, String content, boolean includeCancel, String... options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ArrayList<ButtonType> buttons = new ArrayList<>();

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        for(String option : options) {
            buttons.add(new ButtonType(option));
        }

        if (includeCancel) {
            buttons.add(cancel);
        }

        alert.getButtonTypes().setAll(buttons);

        Optional<ButtonType> result = alert.showAndWait();

        if (buttons.contains(result.get())){
            return buttons.indexOf(result.get());
        } else {
            return -1;
        }
    }

    public static int openNumberInputDialog(String title, String prompt, int currentValue, int minimumValue) {
        TextInputDialog inputDialog = new TextInputDialog(Integer.toString(currentValue));
        inputDialog.setTitle(title);
        inputDialog.setHeaderText(title);
        inputDialog.setContentText(prompt);

        Optional<String> result = inputDialog.showAndWait();
        if (result.isPresent()) {
            try {
                int value = Integer.parseInt(result.get());
                if (value >= minimumValue) {
                    return value;
                }
            } catch (NumberFormatException ignored) {}
        }
        return currentValue;
    }

    public static <T> Pair<T, Object> loadFXMLWithController(String path) {
        FXMLLoader loader = new FXMLLoader(Utils.class.getClassLoader().getResource(path));
        try {
            T component = loader.load();
            return new Pair<>(component, loader.getController());
        } catch (IOException e) {
            throw new FXMLInitException("Failed to load FXML data", e);
        }
    }

    public static Insets getPadding() {
        return new Insets(10.0, 10.0, 10.0, 10.0);
    }

    public static Button styleButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("whiteButton");

        return button;
    }

    public static Text titleText(String text, Paint color) {
        Text t = new Text(text);

        t.setFill(color);
        t.setFont(new Font(20.0));

        return t;
    }

    public static Region createVSpacer() {
        final Region region = new Region();

        VBox.setVgrow(region, Priority.ALWAYS);

        return region;
    }

    public static Region createHSpacer() {
        final Region region = new Region();

        HBox.setHgrow(region, Priority.ALWAYS);

        return region;
    }

    public static File saveChart(Stage primaryStage, Node node) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            //save the image

            try {
                //Pad the capture area


                WritableImage writableImage = new WritableImage((int) node.getLayoutBounds().getWidth(),
                        (int) node.getLayoutBounds().getHeight());
                node.snapshot(null, writableImage);


                BufferedImage bufferedImage = new BufferedImage((int) node.getLayoutBounds().getWidth(),
                        (int) node.getLayoutBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);


                for (int x = 0; x < (int) node.getLayoutBounds().getWidth(); x++) {
                    for (int y = 0; y < (int) node.getLayoutBounds().getHeight(); y++) {
                        bufferedImage.setRGB(x, y, writableImage.getPixelReader().getArgb(x, y));
                    }
                }


                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                Utils.openErrorDialog("Disk write error", "An error occurred while writing to disk", ex.toString());
                ex.printStackTrace();
            }

        }

        return file;
    }

    public static void addSaveChartMenu(Node node) {
        ContextMenu menu = new ContextMenu();
        MenuItem saveImage = new MenuItem("Save as image...");
        saveImage.addEventHandler(ActionEvent.ACTION, e -> {
            saveChart((Stage) node.getScene().getWindow(), node);
        });
        menu.getItems().add(saveImage);
        node.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, e -> {
            menu.show(node, e.getScreenX(), e.getScreenY());
        });
    }

    @Nullable
    public static File openPicker(Stage primaryStage, String description, String extensions){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialDirectory(new File("."));

        //Show open file dialog
        return fileChooser.showOpenDialog(primaryStage);
    }

    @Nullable
    public static File openSaver(Stage primaryStage, String description, String extensions){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialDirectory(new File("."));

        //Show open file dialog
        return fileChooser.showSaveDialog(primaryStage);
    }

    /**
     * Utility method for construction of pre-bound properties in field initializers
     */
    public static <T extends Property<V>, V> T propBound(T property, ObservableValue<V> binding) {
        property.bind(binding);
        return property;
    }
}
