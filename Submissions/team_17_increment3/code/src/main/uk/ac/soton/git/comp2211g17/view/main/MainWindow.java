package uk.ac.soton.git.comp2211g17.view.main;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uk.ac.soton.git.comp2211g17.model.files.DBFileManager;
import uk.ac.soton.git.comp2211g17.view.PaletteChooser;
import uk.ac.soton.git.comp2211g17.view.PrintDialog;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.view.loader.LoaderWindow;
import uk.ac.soton.git.comp2211g17.viewmodel.BounceViewModel;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
	public Navigation contentsNavigation;
	public BorderPane root;

	public BounceViewModel bounceViewModel = new BounceViewModel();
	public RadioMenuItem bouncePages;
	public RadioMenuItem bounceTime;
	public ScrollPane scrollPane;

	private Properties prop;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// FXML doesn't have bidirectional binding yet :(
		bouncePages.selectedProperty().bindBidirectional(bounceViewModel.pagesMode);
		bounceTime.selectedProperty().bindBidirectional(bounceViewModel.timeSpentMode);

		getProperties();
	}

	public void openLoaderWindow(ActionEvent actionEvent) {
		LoaderWindow.openLoaderWindow(root.getScene().getWindow(), true);
	}

	public void openSaveDialog(ActionEvent actionEvent) {
		try {
			Stage stage = ((Stage) root.getScene().getWindow());
			if (DBFileManager.getOpenFile().getFile() == null) {
				File target = Utils.openSaver(stage, "DB File", "*.db");
				if (target == null) {
					return;
				}
				DBFileManager.getOpenFile().save(target);
			} else {
				DBFileManager.getOpenFile().save();
			}
			stage.setTitle(stage.getTitle().replaceAll("-.*?$", "- " + DBFileManager.getOpenFile().getFile().getName()));
		} catch (IOException e) {
			Utils.openErrorDialog("Disk write error", "Could not save the target file", e.toString());
			e.printStackTrace();
		}
	}

	public void openPaletteChooser(ActionEvent actionEvent) {
		Stage stage = new Stage();
		var pcPair = Utils.loadFXMLWithController("fxml/PaletteChooser.fxml");
		Scene scene = new Scene((AnchorPane) pcPair.getKey());
		var pc = ((PaletteChooser) pcPair.getValue());
		pc.setMainWindowRoot(root);
		pc.setStage(stage);
		pc.setMainWindow(this);

		String palette = prop.getProperty("palette");

		pc.selectPalette(palette != null ? palette : "zesty");

		stage.setScene(scene);
		stage.show();
	}

	public void getProperties() {
		prop = new Properties();

		FileInputStream propInputStream = null;
		try {
			propInputStream = new FileInputStream("config.properties");
		} catch (FileNotFoundException e) {
			propInputStream = null;
		}
		if (propInputStream != null) {
			try {
				prop.load(propInputStream);

				String palette = prop.getProperty("palette");
				palette = palette != null ? palette : "zesty";
				root.getStyleClass().removeAll("zesty", "elegant", "corporate", "retro");
				root.getStyleClass().add(palette);

				String bounceType = prop.getProperty("bounceType");
				String bounceV = prop.getProperty("bounceValue");

				if (bounceV != null) {
					int bounceValue = Integer.parseInt(bounceV);

					if (bounceType.equals("pages")) {
						bounceViewModel.pagesCount.set(bounceValue);
						bouncePages.setSelected(true);
					}

					if (bounceType.equals("time")) {
						bounceViewModel.timeSpentSeconds.set(bounceValue);
						bounceTime.setSelected(true);
					}
				}
			} catch (Exception e) {
				Utils.openErrorDialog("Properties error", "Could not access the properties file",  e.getMessage());
			} finally {
				try {
					propInputStream.close();
				} catch (IOException ignored) {}
			}
		}
	}

	public void savePaletteProp(String palette) {
		prop.setProperty("palette", palette);

		saveAllProp();
	}

	public void saveBounceProp(String bounceType, int bounceValue) {
		prop.setProperty("bounceType", bounceType);
		prop.setProperty("bounceValue", String.valueOf(bounceValue));

		saveAllProp();
	}

	public void saveAllProp() {
		File f = new File("config.properties");
		try (var propOutputStream = new FileOutputStream(f, false)) {
			prop.store(propOutputStream, "");
		} catch (Exception e) {
			Utils.openErrorDialog("Properties error", "Could not access the properties file",  e.getMessage());
		}
	}

	public void navigateHome(ActionEvent actionEvent) {
		contentsNavigation.navigate("homepage");
	}

	public void navigateHelpPage(ActionEvent actionEvent){
		contentsNavigation.navigate("help");
	}

	public void navigateSideToSide(ActionEvent actionEvent) {
		contentsNavigation.navigate("side-by-side");
	}

	public void bouncePages(ActionEvent actionEvent) {
		bounceViewModel.pagesCount.set(Utils.openNumberInputDialog(
			"Bounce: Number of pages",
			"Enter the maximum number of pages viewed:",
			bounceViewModel.pagesCount.get(), 1));

		saveBounceProp("pages", bounceViewModel.pagesCount.get());
	}

	public void bounceTime(ActionEvent actionEvent) {
		bounceViewModel.timeSpentSeconds.set(Utils.openNumberInputDialog(
			"Bounce: Time spent on website",
			"Enter the maximum number of seconds spent on the website:",
			bounceViewModel.timeSpentSeconds.get(), 1));

		saveBounceProp("time", bounceViewModel.timeSpentSeconds.get());
	}

	public BounceViewModel getBounceViewModel() {
		return bounceViewModel;
	}

	public void printPage() {
		PrintDialog.openPrintDialog(root.getScene().getWindow(), contentsNavigation);
	}
}
