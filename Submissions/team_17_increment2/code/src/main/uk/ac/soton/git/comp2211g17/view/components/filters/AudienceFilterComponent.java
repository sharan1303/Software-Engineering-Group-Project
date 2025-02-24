package uk.ac.soton.git.comp2211g17.view.components.filters;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.fxmisc.easybind.EasyBind;
import org.kordamp.ikonli.javafx.FontIcon;
import uk.ac.soton.git.comp2211g17.model.types.Category;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.util.List;

public class AudienceFilterComponent<T extends Category> extends BorderPane {
    @FXML
    public CheckComboBox<T> choiceBox;
    @FXML
    public Text title;
    @FXML
    public FontIcon buttonIcon;
    private BooleanProperty isOn = new SimpleBooleanProperty(true);

    public AudienceFilterComponent(@NamedArg("title") String title) {
        Utils.loadFXMLAsComponent("fxml/Filters/AudienceFilterComponent.fxml", this);

        this.title.setText(title);
        this.choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object.getName();
            }

            @Override
            public T fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });

        this.choiceBox.disableProperty().bind(isOn.not());
        EasyBind.subscribe(isOn, value -> {
            this.buttonIcon.setIconLiteral(value ? "eva-checkmark-circle-2-outline" : "eva-close-circle-outline");
        });
    }

    public void setComponents(T[] components) {
        this.choiceBox.getItems().addAll(components);
    }

    public void disableFilter(ActionEvent actionEvent) {
        this.isOn.set(!isOn.get());
    }

    public boolean isIsOn() {
        return isOn.get();
    }

    public BooleanProperty isOnProperty() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn.set(isOn);
    }

    public List<T> getValue() {
        return this.choiceBox.getCheckModel().getCheckedItems().size() > 0
                ? this.choiceBox.getCheckModel().getCheckedItems()
                : this.choiceBox.getItems(); //return "any" if none selected
    }
}
