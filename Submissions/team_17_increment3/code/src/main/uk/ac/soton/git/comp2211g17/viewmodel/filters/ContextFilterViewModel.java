package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.fxml.Initializable;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import uk.ac.soton.git.comp2211g17.model.query.filters.ContextFilter;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ContextFilterViewModel implements Initializable {
    public CheckComboBox<Context> choiceBox;

    public void initialize(URL location, ResourceBundle resources) {
        this.choiceBox.getItems().addAll(Context.values());
        this.choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Context object) {
                return object.getName();
            }

            @Override
            public Context fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
    }

    public ContextFilter getFilter() {
        List<Context> contexts = this.choiceBox.getCheckModel().getCheckedItems();
        if (contexts.size() == 0) {
            contexts = this.choiceBox.getItems(); //any if none selected
        }
        ContextFilter filter = new ContextFilter(contexts.get(0));
        for (int i = 1; i < contexts.size(); i++) {
            filter.or(new ContextFilter(contexts.get(i)));
        }
        return filter;
    }
}
