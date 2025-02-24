package uk.ac.soton.git.comp2211g17.view.components;

import javafx.beans.NamedArg;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class FileSelectorGroup extends BorderPane implements Initializable {
	@FXML
	private TextField textBox;
	@FXML
	private ChoiceBox<ComboBoxValue> choiceBox;

	private PredefinedFormatString defaultValue = null;
	private final Property<ComboBoxValue> customValue = new SimpleObjectProperty<>();
	private boolean isEditing = false;

	public FileSelectorGroup(@NamedArg("defaultValue") String defaultValue) {
		for (PredefinedFormatString value : PredefinedFormatString.values()) {
			if (value.toString().equals(defaultValue)) {
				this.defaultValue = value;
				break;
			}
		}
		Utils.loadFXMLAsComponent("fxml/LoaderWindow/FileSelectorGroup.fxml", this);
	}

	public String getPath() {
		return textBox.getText();
	}

	public String getFormatString() {
		return choiceBox.getValue().getFormatString();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isEditing = true;
		choiceBox.getItems().clear();
		choiceBox.getItems().addAll(buildValues());
		choiceBox.setValue(defaultValue);
		isEditing = false;

		choiceBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(ComboBoxValue object) {
				return object.getText();
			}

			@Override
			public ComboBoxValue fromString(String string) {
				throw new UnsupportedOperationException();
			}
		});

		customValue.addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(oldValue)) {
				isEditing = true;
				choiceBox.getItems().clear();
				choiceBox.getItems().addAll(buildValues());
				choiceBox.setValue(newValue);
				isEditing = false;
			}
		});
	}

	@FXML
	private void formatStringSelected(ActionEvent actionEvent) {
		if (!isEditing) {
			ComboBoxValue value = choiceBox.getValue();
			value.onClicked();
		}
	}

	@FXML
	private void openFileChooser(ActionEvent actionEvent) {
		File file = Utils.openPicker((Stage) getScene().getWindow(), "CSV file", "*.csv");
		if (file == null) {
			return;
		}
		textBox.setText(file.getAbsolutePath());
	}

	public enum PredefinedFormatString implements ComboBoxValue {
		CLICK_LOG("Click log", "DATE,ID,CLICK-COST"),
		IMPRESSION_LOG("Impression log", "DATE,ID,USER-GENDER,USER-AGE,INCOME,CONTEXT,IMPRESSION-COST"),
		SERVER_LOG("Server log", "DATE,ID,DATE-EXIT,VIEWED-PAGES,CONVERSION");

		public final String name;
		public final String formatString;

		PredefinedFormatString(String name, String formatString) {
			this.name = name;
			this.formatString = formatString;
		}

		@Override
		public String getText() {
			return name;
		}

		@Override
		public String getFormatString() {
			return formatString;
		}

		@Override
		public void onClicked() {}
	}

	private interface ComboBoxValue {
		String getText();
		String getFormatString();
		void onClicked();
	}

	private List<ComboBoxValue> buildValues() {
		List<ComboBoxValue> values = new ArrayList<>(Arrays.asList(PredefinedFormatString.values()));
		if (customValue.getValue() != null) {
			values.add(customValue.getValue());
		}
		values.add(new ComboBoxValue() {
			@Override
			public String getText() {
				return "Other...";
			}

			@Override
			public String getFormatString() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void onClicked() {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText("Insert custom layout string");
				dialog.showAndWait();

				String textResult = dialog.getEditor().getText();
				if (textResult == null || textResult.trim().length() == 0) {
					return;
				}
				customValue.setValue(new ComboBoxValue() {
					@Override
					public String getText() {
						return "Custom: " + textResult;
					}

					@Override
					public String getFormatString() {
						return textResult;
					}

					@Override
					public void onClicked() {}
				});
			}
		});
		return values;
	}
}
