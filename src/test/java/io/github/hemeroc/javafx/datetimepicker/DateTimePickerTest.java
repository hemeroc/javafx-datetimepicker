package io.github.hemeroc.javafx.datetimepicker;

import io.github.hemeroc.javafx.datetimepicker.testfx.TestFXHeadlessExtension;
import io.github.hemeroc.javafx.datetimepicker.testfx.TestFXScreenshotExtension;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextMatchers;

import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import static javafx.geometry.Pos.CENTER;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith({TestFXHeadlessExtension.class, ApplicationExtension.class, TestFXScreenshotExtension.class})
class DateTimePickerTest {

    private static final LocalDateTime INITIAL_DATETIME = LocalDateTime.of(2018, 4, 15, 13, 37);
    private static final String DE_FORMAT = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM,
            FormatStyle.SHORT,
            IsoChronology.INSTANCE,
            Locale.GERMANY);

    @Start
    void onStart(Stage stage) {
        DateTimePicker dateTimePicker = new DateTimePicker(INITIAL_DATETIME);
        dateTimePicker.setFormat(DE_FORMAT);
        VBox vBox = new VBox(dateTimePicker);
        vBox.setAlignment(CENTER);
        stage.setScene(new Scene(vBox, 250, 50));
        stage.show();
        stage.toFront();
        stage.setAlwaysOnTop(true);
    }

    @Test
    void verifyThatInitialTimestampIsSet() {
        verifyThat(".date-time-picker .text", TextMatchers.hasText("15.04.2018, 13:37"));
    }

}