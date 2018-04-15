package io.github.hemeroc.javafx.datetimepicker;

import io.github.hemeroc.javafx.datetimepicker.testfx.TestFXHeadlessExtension;
import io.github.hemeroc.javafx.datetimepicker.testfx.TestFXScreenshotOnFailureExtension;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextMatchers;

import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import static io.github.hemeroc.javafx.datetimepicker.testfx.TestFXHamcrestNodeMatcher.hasNoChild;
import static javafx.geometry.Pos.CENTER;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChild;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@ExtendWith({
        TestFXHeadlessExtension.class,
        ApplicationExtension.class,
        TestFXScreenshotOnFailureExtension.class,
})
class DateTimePickerTest {

    private static final LocalDateTime INITIAL_DATETIME = LocalDateTime.of(2018, 4, 15, 13, 37);
    private static final String DE_FORMAT = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM,
            FormatStyle.SHORT,
            IsoChronology.INSTANCE,
            Locale.GERMANY);

    private static final String DATE_TIME_SELECT_BUTTON = ".date-time-picker .arrow-button";
    private static final String TIME_SELECTOR_SLIDER_PANE = ".time-selector-slider-pane";
    private static final String DATE_TIME_PICKER_POPUP = ".date-time-picker-popup";
    private static final String TIME_SELECTOR_SPINNER_PANE = ".time-selector-spinner-pane";

    private DateTimePicker dateTimePicker;

    @Start
    void onStart(Stage stage) {
        dateTimePicker = new DateTimePicker(INITIAL_DATETIME);
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

    @Test
    void verifyThatNoTimeSelectorIsVisibleWhenSetToNone(FxRobot robot) {
        dateTimePicker.setTimeSelector(DateTimePicker.TimeSelector.NONE);
        robot.clickOn(DATE_TIME_SELECT_BUTTON);
        verifyThat(TIME_SELECTOR_SLIDER_PANE, hasNoChild(TIME_SELECTOR_SLIDER_PANE));
        verifyThat(TIME_SELECTOR_SLIDER_PANE, hasNoChild(TIME_SELECTOR_SPINNER_PANE));
    }

    @Test
    void verifyThatSliderTimeSelectorIsVisibleWhenSetToSlider(FxRobot robot) {
        dateTimePicker.setTimeSelector(DateTimePicker.TimeSelector.SLIDER);
        robot.clickOn(DATE_TIME_SELECT_BUTTON);
        verifyThat(DATE_TIME_PICKER_POPUP, hasChild(TIME_SELECTOR_SLIDER_PANE));
        verifyThat(TIME_SELECTOR_SLIDER_PANE, isVisible());
    }

    @Test
    void verifyThatSpinnerTimeSelectorIsVisibleWhenSetToSpinner(FxRobot robot) {
        dateTimePicker.setTimeSelector(DateTimePicker.TimeSelector.SPINNER);
        robot.clickOn(DATE_TIME_SELECT_BUTTON);
        verifyThat(DATE_TIME_PICKER_POPUP, hasChild(TIME_SELECTOR_SPINNER_PANE));
        verifyThat(TIME_SELECTOR_SPINNER_PANE, isVisible());
    }
}
