package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TimeGranularityViewModel implements Initializable {

    public AnchorPane container;
    public TextField change;
    public Slider slider;
    public IntegerProperty granularityValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        granularityValue = new SimpleIntegerProperty(3600);
        slider.setOnMouseReleased(e -> labelValueChange(slider.getValue()));

        change.setOnAction( e -> { sendGranularity(change.getText());
//            if(e.getCode().equals(KeyCode.ENTER)){
//                sendGranularity(change.getText());
//            }
        });
    }

    public void labelValueChange(double value){
        if(value == 0.0) {
            change.setText("1 Minute");
            granularityValue.setValue(60);
        }
        else if (value == 25.0) {
            change.setText("1 Hour");
            granularityValue.setValue((60*60));
        }
        else if (value == 50.0) {
            change.setText("1 Day");
            granularityValue.setValue((60*60*24));
        }
        else if (value == 75.0) {
            change.setText("1 Week");
            granularityValue.setValue((60*60*24*7));
        }
        else if (value == 100.0) {
            change.setText("1 Month");
            granularityValue.setValue((60*60*24*7*30));
        }
        }

    public IntegerProperty sendGranularity(String value){
        long num = 0;
        String chunk = "";
        try {
            String[] calc = value.split(" ");
            num = Long.parseLong(calc[0]);
            chunk = calc[1];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (chunk.equalsIgnoreCase("Minute") || chunk.equalsIgnoreCase("Minutes"))
            granularityValue.setValue(60*num);
        else if (chunk.equalsIgnoreCase("Hour") || chunk.equalsIgnoreCase("Hours"))
            granularityValue.setValue( num * (60*60));
        else if (chunk.equalsIgnoreCase("Day") || chunk.equalsIgnoreCase("Days"))
            granularityValue.setValue( num * (60*60*24));
        else if (chunk.equalsIgnoreCase("Week") || chunk.equalsIgnoreCase("Weeks"))
            granularityValue.setValue( num * (60*60*24*7));
        else
            granularityValue.setValue( num * (60*60*24*7*30));
        return granularityValue;
    }
    }
