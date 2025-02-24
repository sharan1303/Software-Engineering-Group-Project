package uk.ac.soton.git.comp2211g17.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.column.CategoriesColumn;
import uk.ac.soton.git.comp2211g17.model.types.column.LongIntegerColumn;

import java.util.Arrays;

public class Utils {
    public static Insets getPadding() {
        return new Insets(10.0, 10.0, 10.0, 10.0);
    }

    public static Button styleButton(String text) {
        Button button = new Button(text);

        button.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(30.0, false), Insets.EMPTY)));
        button.setTextFill(new Color(63.0 / 255, 160.0 / 255, 251.0 / 255, 1.0));
        button.setPadding(Utils.getPadding());

        return button;
    }

    public static Text titleText(String text, Paint color) {
        Text t = new Text(text);

        t.setFill(color);
        t.setFont(new Font(20.0));

        return t;
    }

    public static HBox graphAxisSelector(String label, Column[] fields, boolean xAxis) {

        ComboBox comboBox = new ComboBox();

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10.0);

        fields = Arrays.stream(fields).filter((f) -> xAxis && (f instanceof CategoriesColumn) || !xAxis && (f instanceof LongIntegerColumn)).toArray(Column[]::new);

        comboBox.getItems().addAll(Arrays.stream(fields).map((field) -> field.getField().getName()).toArray(String[]::new));

        hbox.getChildren().addAll(new Text(label), comboBox);

        return hbox;
    }

    public static Region createVSpacer() {
        final Region region = new Region();

        VBox.setVgrow(region, Priority.ALWAYS);

        return region;
    }

    public static Region createHSpacer() {
        final Region region = new Region();

        HBox.setHgrow(region, Priority.ALWAYS);

        return region;
    }
}
