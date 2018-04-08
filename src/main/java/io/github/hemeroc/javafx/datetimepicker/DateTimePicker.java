package io.github.hemeroc.javafx.datetimepicker;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import static io.github.hemeroc.javafx.datetimepicker.DateTimePicker.TimeSelector.NONE;
import static java.lang.Boolean.TRUE;

public class DateTimePicker extends DatePicker {

    public static final String DEFAULT_FORMAT = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM,
            FormatStyle.SHORT,
            IsoChronology.INSTANCE,
            Locale.getDefault());
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
    private ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());
    private ObjectProperty<TimeSelector> timeSelector = new SimpleObjectProperty<>(NONE);
    private ObjectProperty<Boolean> minutesSelector = new SimpleObjectProperty<>(TRUE);
    private ObjectProperty<String> format = new SimpleObjectProperty<>() {
        public void set(String newValue) {
            super.set(newValue);
            formatter = DateTimeFormatter.ofPattern(newValue);
        }
    };

    public DateTimePicker(LocalDateTime localDateTime) {
        super(localDateTime.toLocalDate());
        setConverter(new InternalConverter());
        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                dateTimeValue.set(null);
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
                } else {
                    LocalTime time = dateTimeValue.get().toLocalTime();
                    dateTimeValue.set(LocalDateTime.of(newValue, time));
                }
            }
        });
        dateTimeValue.addListener((observable, oldValue, newValue) -> {
            setValue(newValue == null ? null : newValue.toLocalDate());
        });
        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                simulateEnterPressed();
        });
    }

    public DateTimePicker() {
        this(LocalDateTime.now());
    }

    private void simulateEnterPressed() {
        getEditor().fireEvent(new KeyEvent(getEditor(), getEditor(), KeyEvent.KEY_PRESSED, null, null, KeyCode.ENTER,
                false, false, false, false));
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue.get();
    }

    public void setDateTimeValue(LocalDateTime dateTimeValue) {
        if (dateTimeValue.isAfter(LocalDateTime.of(1971, 6, 30, 12, 0))) {
            this.dateTimeValue.set(dateTimeValue);
        } else {
            this.dateTimeValue.set(null);
        }
    }

    public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return dateTimeValue;
    }

    public String getFormat() {
        return format.get();
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public ObjectProperty<String> formatProperty() {
        return format;
    }

    public TimeSelector getTimeSelector() {
        return timeSelector.get();
    }

    public void setTimeSelector(TimeSelector timeSelector) {
        this.timeSelector.set(timeSelector);
    }

    public ObjectProperty<TimeSelector> timeSelectorProperty() {
        return timeSelector;
    }

    public Boolean getMinutesSelector() {
        return minutesSelector.get();
    }

    public void setTimeSelector(Boolean minutesSelector) {
        this.minutesSelector.set(minutesSelector);
    }

    public ObjectProperty<Boolean> minutesSelector() {
        return minutesSelector;
    }

    class InternalConverter extends StringConverter<LocalDate> {
        public String toString(LocalDate object) {
            LocalDateTime value = getDateTimeValue();
            return (value != null) ? value.format(formatter) : "";
        }

        public LocalDate fromString(String value) {
            if (value == null || value.isEmpty()) {
                dateTimeValue.set(null);
                return null;
            }
            dateTimeValue.set(LocalDateTime.parse(value, formatter));
            return dateTimeValue.get().toLocalDate();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DateTimePickerSkin(this);
    }

    public enum TimeSelector {
        NONE,
        SPINNER,
        SLIDER,
    }

}





