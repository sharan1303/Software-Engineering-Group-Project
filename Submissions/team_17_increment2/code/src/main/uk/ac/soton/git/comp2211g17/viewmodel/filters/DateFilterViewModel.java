package uk.ac.soton.git.comp2211g17.viewmodel.filters;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jooq.Table;
import uk.ac.soton.git.comp2211g17.model.query.filters.DateFilter;
import uk.ac.soton.git.comp2211g17.model.query.filters.Filter;
import uk.ac.soton.git.comp2211g17.view.Utils;
import uk.ac.soton.git.comp2211g17.view.components.filters.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

public class DateFilterViewModel {
    public TextField endDate;
    public TextField startDate;

    private boolean startPickerOpen = false;
    private boolean endPickerOpen = false;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy");

    public static void openDateFilter(Application applcation) {
            Pair<AnchorPane, FXMLLoader> loader = Utils.loadFXMLWithLoader("fxml/Filters/DateFilter.fxml");

            AnchorPane pane = loader.getKey();
            DateFilterViewModel vm = loader.getValue().getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.showAndWait();
    }

    public void pickEndDate(ActionEvent actionEvent) {
        if (startPickerOpen) {
            return;
        }
        startPickerOpen = true;
        DatePicker picker = new DatePicker("Pick end date");
        LocalDateTime time = picker.getValue();
        if (time == null) {
            startPickerOpen = false;
            return;
        }
        this.endDate.setText(formatter.format(time));
        startPickerOpen = false;
    }
    
    public void pickStartDate(ActionEvent actionEvent) {
        if (endPickerOpen) {
            return;
        }
        endPickerOpen = true;
        DatePicker picker = new DatePicker("Pick start date");
        LocalDateTime time = picker.getValue();
        if (time == null) {
            endPickerOpen = false;
            return;
        }
        this.startDate.setText(formatter.format(time));
        endPickerOpen = false;
    }

    public void infiniteEndDate(ActionEvent actionEvent) {
        this.endDate.setText("End of campaign");
    }

    public void infiniteStartDate(ActionEvent actionEvent) {
        this.startDate.setText("Start of campaign");
    }

    public Function<Table<?>, Filter> getFilter() {
        if (this.startDate.getText().equals("Start of campaign") && this.endDate.getText().equals("End of campaign")) {
            return null;
        } else if (this.startDate.getText().equals("Start of campaign")) {
            LocalDateTime date = validateDate(endDate.getText());
            if (date != null) {
                return table -> new DateFilter(DateFilter.DateOperator.TO, date, table);
            } else {
                return null;
            }
        } else if (this.endDate.getText().equals("End of campaign")) {
            LocalDateTime date = validateDate(startDate.getText());
            if (date != null) {
                return table -> new DateFilter(DateFilter.DateOperator.FROM, date, table);
            } else {
                return null;
            }
        } else {
            LocalDateTime startDateParsed = validateDate(startDate.getText());
            LocalDateTime endDateParsed = validateDate(endDate.getText());
            if (startDateParsed != null && endDateParsed != null) {
                return table -> new DateFilter(startDateParsed, endDateParsed, table);
            } else {
                return null;
            }
        }
    }

    private LocalDateTime validateDate(String date) {
        try {
            LocalDateTime d = LocalDate.parse(date, formatter).atStartOfDay();
            return d;
        } catch (DateTimeParseException e) {
            Utils.openErrorDialog("Incorrect date format", "Date " + date + " is not in the correct dd/MM/yyyy format.", e.toString());
            return null;
        }
    }
}
