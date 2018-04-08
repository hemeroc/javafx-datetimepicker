package io.github.hemeroc.javafx.datetimepicker.demo;

import io.github.hemeroc.javafx.datetimepicker.DateTimePicker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_LEFT;

public class DateTimePickerDemo extends Application {
    public void start(Stage stage) {
        DateTimePicker dateTimePickerNoTime = new DateTimePicker();
        DateTimePicker dateTimePickerHoursOnly = new DateTimePicker();
        DateTimePicker dateTimePickerHoursAndMinutes = new DateTimePicker();

        VBox vBox = new VBox(
                new Label("DateTimePicker - No Time"),
                dateTimePickerNoTime,
                new Label("DateTimePicker - Hours only"),
                dateTimePickerHoursOnly,
                new Label("DateTimePicker - Hours and minutes"),
                dateTimePickerHoursAndMinutes
        );
        vBox.setSpacing(10);
        vBox.setAlignment(CENTER_LEFT);
        HBox hBox = new HBox(vBox);
        hBox.setAlignment(CENTER);

        stage.setTitle("DateTimePicker - Demo");
        stage.setResizable(false);
        stage.setScene(new Scene(hBox, 300, 300));
        stage.centerOnScreen();
        stage.show();
        stage.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }
}