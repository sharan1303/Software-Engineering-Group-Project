package uk.ac.soton.git.comp2211g17.view.main;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.ac.soton.git.comp2211g17.view.Utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelpPageView implements Initializable {

    public TableView<Definition> table;
    public TableColumn<Definition,String> term;
    public TableColumn<Definition,String> definition;
    public ArrayList<Definition>  definitionList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        definitionList = createDefinitions();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
//        term.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        term.setMaxWidth(175.0);
        term.setMinWidth(175.0);
        definition.setCellValueFactory(new PropertyValueFactory<>("definition"));
        definition.prefWidthProperty().bind(table.widthProperty().multiply(0.7));
        setTable();
    }

    public void setTable() {
        for(Definition def : definitionList)
            table.getItems().add(def);
    }

    public ArrayList<Definition> createDefinitions() {
        ArrayList<Definition> def = new ArrayList<>();
        def.add(new Definition("Acquisition / Conversion","A conversion, or acquisition, occurs when a user clicks and\n then acts on an ad. The " +
                "specific definition of an action\n depends on the campaign (e.g., buying a product, registering\n as a " +
                "new customer or joining a mailing list)."));
        def.add(new Definition("Bounce","A user clicks on an ad, but then fails to interact with the\n website (typically detected" +
                "when a user navigates away from\n the website after a short time, or when only a single page\n has " +
                "been viewed)."));
        def.add(new Definition("Bounce Rate","The average number of bounces per click"));
        def.add(new Definition("Campaign","An effort by the marketing agency to gain exposure for\n a client's website " +
                "by participating in a range of ad auctions\n offered by different providers and networks. Bid amounts, keywords and other " +
                "variables will be tailored to the client's needs."));
        def.add(new Definition("Click","A click occurs when a user clicks on an ad that is shown to them."));
        def.add(new Definition("Click Cost","The cost of a particular click(usually determined through\n an auction process)."));
        def.add(new Definition("Click-through-rate (CTR)","The average number of clicks per impression"));
        def.add(new Definition("Conversion Rate","The average number of conversions per click."));
        def.add(new Definition("Cost-per-acquisition (CPA)","The average amount of money spent on an advertising\n campaign for every " +
                "one thousand impressions"));
        def.add(new Definition("Impression","An impressions occurs whenever an ad is shown to a user,\n regardless of whether they click on it."));
        def.add(new Definition("Uniques","The number of unique users that click on an ad during\n the course of a campaign."));
        return def;
    }

	public void openUserGuide() {
    	try {
			if (Desktop.isDesktopSupported()) {
				// Copy bundled file to a temp directory
				Path tempFile = Files.createTempFile("adauction", "userguide.pdf");
				InputStream resource = HelpPageView.class.getClassLoader().getResourceAsStream("docs/User_Guide.pdf");
				if (resource == null) {
					Utils.openErrorDialog("Open User Guide", "Failed to find the file", new NullPointerException().toString());
					return;
				}
				Files.copy(resource, tempFile, StandardCopyOption.REPLACE_EXISTING);

				Desktop.getDesktop().open(tempFile.toFile());
			}
		} catch (IOException e) {
			Utils.openErrorDialog("Open User Guide", "An error occurred while opening the file", e.toString());
		}
	}

	public static class Definition {
        public String term;
        public String definition;

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String def) {
            this.definition = def;
        }

        public Definition(String term, String def){
            this.term = term;
            this.definition = def;
        }
    }
}


