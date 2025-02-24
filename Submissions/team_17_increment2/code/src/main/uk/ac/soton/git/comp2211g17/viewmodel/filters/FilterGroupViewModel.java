package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class FilterGroupViewModel implements Initializable {

    public Button dateButton;
    public Button audienceButton;
    public Button contextButton;
    public BorderPane container;

    public Button filterButton;

    private AnchorPane dateFilter;
    private DateFilterViewModel dateFilterViewModel;

    private AnchorPane audienceFilter;
    private AudienceFilterViewModel audienceFilterViewModel;

    private AnchorPane contextFilter;
    private ContextFilterViewModel contextFilterViewModel;

    public void initialize(URL location, ResourceBundle resources) {
        var d = Utils.loadFXMLWithController("fxml/Filters/DateFilter.fxml");
        var a = Utils.loadFXMLWithController("fxml/Filters/AudienceFilter.fxml");
        var c = Utils.loadFXMLWithController("fxml/Filters/ContextFilter.fxml");

        this.dateFilter = (AnchorPane) d.getKey();
        this.dateFilterViewModel = (DateFilterViewModel) d.getValue();

        this.audienceFilter = (AnchorPane) a.getKey();
        this.audienceFilterViewModel = (AudienceFilterViewModel) a.getValue();

        this.contextFilter = (AnchorPane) c.getKey();
        this.contextFilterViewModel = (ContextFilterViewModel) c.getValue();

        this.container.setCenter(dateFilter);
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
            container.setCenter(dateFilter);
        } else if (target.equals(audienceButton)) {
            target.getStyleClass().add("blueButton");
            container.setCenter(audienceFilter);
        } else {
            target.getStyleClass().add("blueButton");
            container.setCenter(contextFilter);
        }
    }

    public Function<Table<?>, Filter> getFilter() {
        if (this.container.getCenter().equals(this.dateFilter)) {
            return this.dateFilterViewModel.getFilter();
        } else if (this.container.getCenter().equals(this.audienceFilter)) {
            return table -> this.audienceFilterViewModel.getFilter();
        } else {
            return table -> this.contextFilterViewModel.getFilter();
        }
    }

    public static void openFilterGroup(Application application) {
        AnchorPane filterGroup = Utils.loadFXML("fxml/Filters/FilterGroup.fxml");

        Stage stage = new Stage();
        stage.setScene(new Scene(filterGroup));
        stage.showAndWait();

        System.out.println(stage.getWidth());
    }

}
