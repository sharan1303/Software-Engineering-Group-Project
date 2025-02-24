package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uk.ac.soton.git.comp2211g17.model.query.filters.AudienceFilter;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.filters.AudienceFilterComponent;

import java.net.URL;
import java.util.ResourceBundle;

public class AudienceFilterViewModel implements Initializable {
    public AudienceFilterComponent<AudienceAge> ageComponent;
    public AudienceFilterComponent<Gender> genderComponent;
    public AudienceFilterComponent<IncomeGroup> incomeComponent;

    public void initialize(URL location, ResourceBundle resources) {
        this.ageComponent.setComponents(AudienceAge.values());
        this.genderComponent.setComponents(Gender.values());
        this.incomeComponent.setComponents(IncomeGroup.values());
    }

    public static void openAudienceFilter(Application application) {
        AnchorPane audienceFilter = Utils.loadFXML("fxml/Filters/AudienceFilter.fxml");

        Stage stage = new Stage();
        stage.setScene(new Scene(audienceFilter));
        stage.showAndWait();
    }

    public AudienceFilter getFilter() {
        AudienceFilter filter = new AudienceFilter();
        if (this.ageComponent.isIsOn()) {
            for (AudienceAge age : this.ageComponent.getValue()) {
                filter.addAge(age);
            }
        }
        if (this.incomeComponent.isIsOn()) {
            for (IncomeGroup income : this.incomeComponent.getValue()) {
                filter.addIncome(income);
            }
        }
        if (this.genderComponent.isIsOn()) {
            for (Gender gender : this.genderComponent.getValue()) {
                filter.addGender(gender);
            }
        }

        return filter.getCondition() != null ? filter : null;
    }
}
