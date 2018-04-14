package io.github.hemeroc.javafx.datetimepicker;

import io.github.hemeroc.javafx.datetimepicker.util.CustomBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimePickerSkin extends DatePickerSkin {
    private final ObjectProperty<LocalTime> timeObjectProperty;
    private final Node popupContent;
    private final DateTimePicker dateTimePicker;
    private final Node timeSpinner;
    private final Node timeSlider;

    DateTimePickerSkin(DateTimePicker dateTimePicker) {
        super(dateTimePicker);
        this.dateTimePicker = dateTimePicker;
        timeObjectProperty = new SimpleObjectProperty<>(this, "displayedTime", LocalTime.from(dateTimePicker.getDateTimeValue()));

        popupContent = super.getPopupContent();

        timeSpinner = getTimeSpinner();
        timeSlider = getTimeSlider();

        dateTimePicker.timeSelectorProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                switch (oldValue) {
                    case SPINNER:
                        ((VBox) popupContent).getChildren().remove(timeSpinner);
                        break;
                    case SLIDER:
                        ((VBox) popupContent).getChildren().remove(timeSlider);
                        break;
                }
                switch (newValue) {
                    case SPINNER:
                        ((VBox) popupContent).getChildren().add(timeSpinner);
                        break;
                    case SLIDER:
                        ((VBox) popupContent).getChildren().add(timeSlider);
                        break;
                }
            }
        });

    }

    private Node getTimeSpinner() {
        if (timeSpinner != null) {
            return timeSpinner;
        }
        final HourMinuteSpinner spinnerHours =
                new HourMinuteSpinner(0, 23, dateTimePicker.getDateTimeValue().getHour());
        CustomBinding.bindBidirectional(timeObjectProperty, spinnerHours.valueProperty(),
                LocalTime::getHour,
                hour -> timeObjectProperty.get().withHour(hour));
        final HourMinuteSpinner spinnerMinutes =
                new HourMinuteSpinner(0, 59, dateTimePicker.getDateTimeValue().getMinute());
        CustomBinding.bindBidirectional(timeObjectProperty, spinnerMinutes.valueProperty(),
                LocalTime::getMinute,
                minute -> timeObjectProperty.get().withMinute(minute));
        final Label labelTimeSeperator = new Label(":");
        HBox hBox = new HBox(new Label("Time:"), spinnerHours, labelTimeSeperator, spinnerMinutes);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(8));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getStyleClass().add("month-year-pane");
        dateTimePicker.minutesSelectorProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                if (newValue) {
                    hBox.getChildren().add(labelTimeSeperator);
                    hBox.getChildren().add(spinnerMinutes);
                } else {
                    hBox.getChildren().remove(labelTimeSeperator);
                    hBox.getChildren().remove(spinnerMinutes);
                }
            }
        });
        registerChangeListener(dateTimePicker.valueProperty(), e -> {
            LocalDateTime dateTimeValue = dateTimePicker.getDateTimeValue();
            timeObjectProperty.set((dateTimeValue != null) ? LocalTime.from(dateTimeValue) : LocalTime.MIDNIGHT);
            dateTimePicker.fireEvent(new ActionEvent());
        });
        return hBox;
    }

    private Node getTimeSlider() {
        if (timeSlider != null) {
            return timeSlider;
        }
        final HourMinuteSlider sliderHours =
                new HourMinuteSlider(0, 23, dateTimePicker.getDateTimeValue().getHour(), 6, 5);
        CustomBinding.bindBidirectional(timeObjectProperty, sliderHours.valueProperty(),
                LocalTime::getHour,
                hour -> timeObjectProperty.get().withHour(hour.intValue()));
        final HourMinuteSlider sliderMinutes =
                new HourMinuteSlider(0, 59, dateTimePicker.getDateTimeValue().getMinute(), 10, 9);
        CustomBinding.bindBidirectional(timeObjectProperty, sliderMinutes.valueProperty(),
                LocalTime::getMinute,
                minute -> timeObjectProperty.get().withMinute(minute.intValue()));
        final VBox vBox = new VBox(5, sliderHours, sliderMinutes);
        dateTimePicker.minutesSelectorProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                if (newValue) {
                    vBox.getChildren().add(sliderMinutes);
                } else {
                    vBox.getChildren().remove(sliderMinutes);
                }
            }
        });
        vBox.setPadding(new Insets(8));
        return vBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getPopupContent() {
        return popupContent;
    }


}
