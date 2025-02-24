package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class DuoFilterGroupViewModel implements Initializable {

    public Button dateButton;
    public Button audienceButton;
    public Button contextButton;

    public BorderPane containerLeft;
    public BorderPane containerRight;

    private AnchorPane dateFilterLeft;
    private DateFilterViewModel dateFilterViewModelLeft;

    private AnchorPane audienceFilterLeft;
    private AudienceFilterViewModel audienceFilterViewModelLeft;

    private AnchorPane contextFilterLeft;
    private ContextFilterViewModel contextFilterViewModelLeft;

    private AnchorPane dateFilterRight;
    private DateFilterViewModel dateFilterViewModelRight;

    private AnchorPane audienceFilterRight;
    private AudienceFilterViewModel audienceFilterViewModelRight;

    private AnchorPane contextFilterRight;
    private ContextFilterViewModel contextFilterViewModelRight;

    public Button filterButton;

    public void initialize(URL location, ResourceBundle resources) {
        {
            var dateFilter = Utils.loadFXMLWithController("fxml/Filters/DateFilter.fxml");
            var audienceFilter = Utils.loadFXMLWithController("fxml/Filters/AudienceFilter.fxml");
            var contextFilter = Utils.loadFXMLWithController("fxml/Filters/ContextFilter.fxml");

            this.dateFilterLeft = (AnchorPane) dateFilter.getKey();
            this.dateFilterViewModelLeft = (DateFilterViewModel) dateFilter.getValue();

            this.audienceFilterLeft = (AnchorPane) audienceFilter.getKey();
            this.audienceFilterViewModelLeft = (AudienceFilterViewModel) audienceFilter.getValue();

            this.contextFilterLeft = (AnchorPane) contextFilter.getKey();
            this.contextFilterViewModelLeft = (ContextFilterViewModel) contextFilter.getValue();
        }
        {
            var dateFilter = Utils.loadFXMLWithController("fxml/Filters/DateFilter.fxml");
            var audienceFilter = Utils.loadFXMLWithController("fxml/Filters/AudienceFilter.fxml");
            var contextFilter = Utils.loadFXMLWithController("fxml/Filters/ContextFilter.fxml");

            this.dateFilterRight = (AnchorPane) dateFilter.getKey();
            this.dateFilterViewModelRight = (DateFilterViewModel) dateFilter.getValue();

            this.audienceFilterRight = (AnchorPane) audienceFilter.getKey();
            this.audienceFilterViewModelRight = (AudienceFilterViewModel) audienceFilter.getValue();

            this.contextFilterRight = (AnchorPane) contextFilter.getKey();
            this.contextFilterViewModelRight = (ContextFilterViewModel) contextFilter.getValue();
        }

        this.containerLeft.setCenter(dateFilterLeft);
        this.containerRight.setCenter(dateFilterRight);
    }

    public void selectTab(ActionEvent actionEvent) {
        var target = (Button) actionEvent.getTarget();
        dateButton.getStyleClass().removeAll("blueButton");
        audienceButton.getStyleClass().removeAll("blueButton");
        contextButton.getStyleClass().removeAll("blueButton");
        dateButton.getStyleClass().add("whiteButton");
        audienceButton.getStyleClass().add("whiteButton");
        contextButton.getStyleClass().add("whiteButton");
        if (target.equals(dateButton)) {
            target.getStyleClass().add("blueButton");
            containerLeft.setCenter(dateFilterLeft);
            containerRight.setCenter(dateFilterRight);
        } else if (target.equals(audienceButton)) {
            target.getStyleClass().add("blueButton");
            containerLeft.setCenter(audienceFilterLeft);
            containerRight.setCenter(audienceFilterRight);
        } else {
            target.getStyleClass().add("blueButton");
            containerLeft.setCenter(contextFilterLeft);
            containerRight.setCenter(contextFilterRight);
        }
    }

    public Function<Table<?>, Filter> getFilterLeft() {
        if (this.containerLeft.getCenter().equals(this.dateFilterLeft)) {
            return this.dateFilterViewModelLeft.getFilter();
        } else if (this.containerLeft.getCenter().equals(this.audienceFilterLeft)) {
            return table -> this.audienceFilterViewModelLeft.getFilter();
        } else {
            return table -> this.contextFilterViewModelLeft.getFilter();
        }
    }

    public Function<Table<?>, Filter> getFilterRight() {
        if (this.containerRight.getCenter().equals(this.dateFilterRight)) {
            return this.dateFilterViewModelRight.getFilter();
        } else if (this.containerRight.getCenter().equals(this.audienceFilterRight)) {
            return table -> this.audienceFilterViewModelRight.getFilter();
        } else {
            return table -> this.contextFilterViewModelRight.getFilter();
        }
    }

}
