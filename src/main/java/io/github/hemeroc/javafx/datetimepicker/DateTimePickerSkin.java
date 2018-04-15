package io.github.hemeroc.javafx.datetimepicker;

import io.github.hemeroc.javafx.datetimepicker.util.CustomBinding;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static javafx.scene.input.MouseButton.PRIMARY;

public class DateTimePickerSkin extends DatePickerSkin {
    private final ObjectProperty<LocalTime> timeObjectProperty;
    private final Node popupContent;

    public DateTimePickerSkin(DateTimePicker dateTimePicker) {
        super(dateTimePicker);
        popupContent = super.getPopupContent();
        timeObjectProperty = new SimpleObjectProperty<>(this, "displayedTime", LocalTime.from(dateTimePicker.getDateTimeValue()));
        HourMinuteSpinner sHour = new HourMinuteSpinner(0, 23, dateTimePicker.getDateTimeValue().getHour());
        CustomBinding.bindBidirectional(timeObjectProperty, sHour.valueProperty(),
                LocalTime::getHour,
                integer -> timeObjectProperty.get().withHour(integer)
        );
        HourMinuteSpinner sMinute = new HourMinuteSpinner(0, 59, dateTimePicker.getDateTimeValue().getMinute());
        CustomBinding.bindBidirectional(timeObjectProperty, sHour.valueProperty(),
                LocalTime::getMinute,
                integer -> timeObjectProperty.get().withMinute(integer)
        );
        HBox hBox = new HBox(new Label("Time:"), sHour, new Label(":"), sMinute);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(8, 8, 8, 8));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getStyleClass().add("month-year-pane");
        ((VBox) popupContent).getChildren().add(hBox);
        registerChangeListener(dateTimePicker.valueProperty(), e -> {
            LocalDateTime dateTimeValue = dateTimePicker.getDateTimeValue();
            timeObjectProperty.set((dateTimeValue != null) ? LocalTime.from(dateTimeValue) : LocalTime.MIDNIGHT);
            dateTimePicker.fireEvent(new ActionEvent());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getPopupContent() {
        return popupContent;
    }

    class HourMinuteSpinner extends HBox {

        private final IntegerSpinnerValueFactory integerSpinnerValueFactory;

        public ObjectProperty<Integer> valueProperty() {
            return integerSpinnerValueFactory.valueProperty();
        }

        HourMinuteSpinner(int min, int max, int initial) {
            integerSpinnerValueFactory = new IntegerSpinnerValueFactory(min, max, initial);
            integerSpinnerValueFactory.setWrapAround(true);
            this.getStyleClass().add("spinner");
            Button decreaseButton = new Button();
            decreaseButton.getStyleClass().add("left-button");
            StackPane decreaseArrow = new StackPane();
            decreaseArrow.getStyleClass().add("left-arrow");
            decreaseArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            decreaseButton.setGraphic(decreaseArrow);

            Button increaseButton = new Button();
            increaseButton.getStyleClass().add("right-button");
            StackPane increaseArrow = new StackPane();
            increaseArrow.getStyleClass().add("right-arrow");
            increaseArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            increaseButton.setGraphic(increaseArrow);

            Label valueLabel = new Label();
            valueLabel.minWidth(10);
            valueLabel.textProperty()
                    .bindBidirectional(integerSpinnerValueFactory.valueProperty(), new IntegerStringConverter() {
                        @Override
                        public String toString(Integer object) {
                            return String.format("%02d", object);
                        }
                    });
            valueLabel.getStyleClass().add("spinner-label");

            IncrementHandler incrementHandler = new IncrementHandler(integerSpinnerValueFactory);
            decreaseButton.setOnAction(event -> {
                integerSpinnerValueFactory.decrement(1);
            });
            decreaseButton.addEventFilter(MouseEvent.MOUSE_PRESSED, incrementHandler);
            decreaseButton.addEventFilter(MouseEvent.MOUSE_RELEASED, incrementHandler);
            increaseButton.setOnAction(event -> {
                integerSpinnerValueFactory.increment(1);
            });
            increaseButton.addEventFilter(MouseEvent.MOUSE_PRESSED, incrementHandler);
            increaseButton.addEventFilter(MouseEvent.MOUSE_RELEASED, incrementHandler);

            this.getChildren().addAll(decreaseButton, valueLabel, increaseButton);
        }
    }

    private static final class IncrementHandler implements EventHandler<MouseEvent> {
        private SpinnerValueFactory spinnerValueFactory;
        private boolean increment;
        private long startTimestamp;
        private long nextStep;
        private static final long DELAY = 1000L * 1000L * 500; // 0.5 sec
        private static final long STEP = 1000L * 1000L * 100; // 0.5 sec
        private final AnimationTimer timer = new AnimationTimer() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void handle(long now) {
                if (now - startTimestamp >= nextStep) {
                    nextStep += STEP;
                    if (increment) {
                        spinnerValueFactory.increment(1);
                    } else {
                        spinnerValueFactory.decrement(1);
                    }
                }
            }
        };

        IncrementHandler(IntegerSpinnerValueFactory integerSpinnerValueFactory) {
            spinnerValueFactory = integerSpinnerValueFactory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == PRIMARY) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    Button button = (Button) event.getSource();
                    increment = button.getStyleClass().contains("right-button");
                    startTimestamp = System.nanoTime();
                    nextStep = DELAY;
                    timer.handle(startTimestamp + DELAY);
                    timer.start();
                    event.consume();
                    button.requestFocus();
                }
                if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    timer.stop();
                }
            }
        }
    }

}