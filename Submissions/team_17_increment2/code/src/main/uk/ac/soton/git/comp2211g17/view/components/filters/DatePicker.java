package uk.ac.soton.git.comp2211g17.view.components.filters;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class DatePicker {
    private Stage stage;
    private javafx.scene.control.DatePicker picker;
    private LocalDateTime value;

    public DatePicker(String title) {
        stage = new Stage();
        stage.setTitle(title);
        BorderPane root = new BorderPane();

        picker = new javafx.scene.control.DatePicker(LocalDate.now());
        picker.setShowWeekNumbers(false);

        picker.setOnAction(this::close);
        DatePickerSkin datePickerSkin = new DatePickerSkin(picker);
        Node popupContent = datePickerSkin.getPopupContent();

        BorderPane box = new BorderPane();
        box.setCenter(popupContent);

        Scene scene = new Scene(box, 300, 225);
        stage.setScene(scene);

        stage.showAndWait();
    }

    public void close(ActionEvent actionEvent) {
        this.value = picker.getValue().atStartOfDay();
        System.out.println(this.value);
        this.stage.close();
    }

    public LocalDateTime getValue() {
        return this.value;
    }
}
