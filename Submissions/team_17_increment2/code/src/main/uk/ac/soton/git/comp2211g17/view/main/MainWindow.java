package uk.ac.soton.git.comp2211g17.view.main;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uk.ac.soton.git.comp2211g17.model.files.DBFileManager;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.Navigation;
import uk.ac.soton.git.comp2211g17.view.loader.LoaderWindow;
import uk.ac.soton.git.comp2211g17.viewmodel.BounceViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
	public Navigation contentsNavigation;
	public BorderPane root;

	public BounceViewModel bounceViewModel = new BounceViewModel();
	public RadioMenuItem bouncePages;
	public RadioMenuItem bounceTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// FXML doesn't have bidirectional binding yet :(
		bouncePages.selectedProperty().bindBidirectional(bounceViewModel.pagesMode);
		bounceTime.selectedProperty().bindBidirectional(bounceViewModel.timeSpentMode);
	}

	public void openLoaderWindow(ActionEvent actionEvent) {
		LoaderWindow.openLoaderWindow(root.getScene().getWindow());
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

	public void navigateHome(ActionEvent actionEvent) {
		contentsNavigation.navigate("homepage");
	}

	public void navigateSideToSide(ActionEvent actionEvent) {
		contentsNavigation.navigate("side-by-side");
	}

	public void bouncePages(ActionEvent actionEvent) {
		bounceViewModel.pagesCount.set(Utils.openNumberInputDialog(
			"Bounce: Number of pages",
			"Enter the maximum number of pages viewed:",
			bounceViewModel.pagesCount.get(), 1));
	}

	public void bounceTime(ActionEvent actionEvent) {
		bounceViewModel.timeSpentSeconds.set(Utils.openNumberInputDialog(
			"Bounce: Time spent on website",
			"Enter the maximum number of seconds spent on the website:",
			bounceViewModel.timeSpentSeconds.get(), 1));
	}

	public BounceViewModel getBounceViewModel() {
		return bounceViewModel;
	}
}
