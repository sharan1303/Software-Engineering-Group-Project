package uk.ac.soton.git.comp2211g17.view.loader;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import uk.ac.soton.git.comp2211g17.model.files.DBFile;
import uk.ac.soton.git.comp2211g17.model.inputs.InputDataParser;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.FileSelectorGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoaderWindow {
    private static final int MIN_CHOOSERS = 3;

    public VBox fileChoosersContainer;
    public Button removeButton;
    public Text titleText;

    public Stage stage;
    public Stage mainWindow;
    public BorderPane container;

    public void loadCampaign(ActionEvent actionEvent) {
        Pair<AnchorPane, FXMLLoader> loaderPair = Utils.loadFXMLWithLoader("fxml/LoaderWindow/LoaderProgressWindow.fxml");
        AnchorPane result = loaderPair.getKey();
        FXMLLoader loader = loaderPair.getValue();
        Label progressLabel = (Label) loader.getNamespace().get("progressLabel");

        container.setCenter(result);

        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Node node : fileChoosersContainer.getChildren()) {
                    FileSelectorGroup selectorGroup = (FileSelectorGroup) node;
                    updateMessage("Reading file " + selectorGroup.getPath());
                    InputDataParser.parse(DatabaseManager.getInstance(), new BufferedReader(new FileReader(selectorGroup.getPath())),
                        selectorGroup.getFormatString(), true);
                }
                return null;
            }
        };

        loadingTask.messageProperty().addListener(((observable, oldValue, newValue) -> {
            progressLabel.setText(newValue);
        }));

        loadingTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (e) -> {
            DatabaseManager.getInstance().reload();
            if (mainWindow != null) {
                mainWindow.setTitle(mainWindow.getTitle() + "*");
            }

            stage.close();
        });
        loadingTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, (e) -> {
            Utils.openErrorDialog("Import error", "An error occurred during the import", e.getSource().getException().toString());
        });

        new Thread(loadingTask).start();
    }

    public void loadFromDB(ActionEvent actionEvent) {
        File file = Utils.openPicker((Stage) container.getScene().getWindow(), "DB file", "*.db");
        if (file == null) {
            return;
        }

        try {
            DBFile dbFile = new DBFile(file);

            if (dbFile.accessible()) {
                DatabaseManager.getInstance().setPath(dbFile.getWorkingFile().getPath());
                DatabaseManager.getInstance().reload();
                ((Stage) container.getScene().getWindow()).close();
            } else {
                Utils.openErrorDialog("Load from DB error", "An error occurred when accessing the database",
                        !file.exists() ? "File \"" + file.getPath() + "\" does not exist." :
                                !file.canRead() ? "User does not have the read access to \"" + file.getPath() + "\"." :
                                        !file.canWrite() ? "User does not have the write access to \"" + file.getPath() + "\"." :
                                                "Unknown file error."
                );
            }
        } catch (IOException e) {
            Utils.openErrorDialog("Disk read error", "An error occurred while opening the file", e.toString());
            e.printStackTrace();
        }
    }

    public void addMore(ActionEvent actionEvent) {
        fileChoosersContainer.getChildren().add(new FileSelectorGroup(null));
        removeButton.setDisable(false);
    }

    public void removeLast(ActionEvent actionEvent) {
        int size = fileChoosersContainer.getChildren().size();
        if (size > MIN_CHOOSERS) {
            if (size == MIN_CHOOSERS + 1) {
                ((Button) actionEvent.getSource()).setDisable(true);
            }
            fileChoosersContainer.getChildren().remove(size - 1);
        } // TODO: handle all the errors thanks
    }

    public static void openLoaderWindow(Window application) {
        Stage stage = new Stage();
        Pair<Parent, Object> rootPair = Utils.loadFXMLWithController("fxml/LoaderWindow/LoaderWindow.fxml");
        Parent root = rootPair.getKey();

        stage.setTitle("Load campaign");

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setMinHeight(400);
        stage.setMinWidth(600);

        ((LoaderWindow)rootPair.getValue()).stage = stage;
        ((LoaderWindow)rootPair.getValue()).mainWindow = (Stage) application;

        stage.showAndWait();
    }
}
