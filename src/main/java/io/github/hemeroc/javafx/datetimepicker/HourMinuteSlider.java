package io.github.hemeroc.javafx.datetimepicker;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class HourMinuteSlider extends HBox {

    private final Slider slider;

    DoubleProperty valueProperty() {
        return slider.valueProperty();
    }

    HourMinuteSlider(int minValue, int maxValue, int currentValue, int majorTickCount, int minorTickCount) {
        slider = new Slider(minValue, maxValue, currentValue);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(majorTickCount);
        slider.setMinorTickCount(minorTickCount);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        setHgrow(slider, Priority.ALWAYS);
        final Label valueLabel = new Label("00");
        valueLabel.setMinWidth(20);
        valueLabel.textProperty().bind(Bindings.format("%02.0f", slider.valueProperty()));
        valueLabel.getStyleClass().add("spinner-label");
        this.setSpacing(5);
        this.getChildren().addAll(valueLabel, slider);
    }
}
