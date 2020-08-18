package mycalendar.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mycalendar.model.CalendarMath;
import mycalendar.model.Event;
import mycalendar.model.EventColor;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.Time;

/**
 * Represents a window containing fields to allow a user to create and add an event to the
 * calendar.
 */
public class AddEventPopup extends VBox {

  /**
   * Creates the window to allow the user to add an event to the calendar.
   *
   * @param addEvent an operation which takes in an event and adds it to the calendar
   */
  public AddEventPopup(Consumer<Event> addEvent) {
    TextField titleField = new TextField();
    titleField.setMaxWidth(200);
    titleField.setPromptText("Event title");

    DatePicker eventDatePicker = new ValidDatePicker();
    eventDatePicker.setPromptText("Event date");
    eventDatePicker.setValue(LocalDate.now());

    TimePicker startTimePicker = new TimePicker();
    startTimePicker.setAsStartTimePicker();
    startTimePicker.setTime(CalendarMath.localTimeToModelTime(LocalTime.now()));

    TimePicker endTimePicker = new TimePicker();
    endTimePicker.setAsEndTimePicker();

    ComboBox<EventColor> colorPicker = new EventColorPicker();
    colorPicker.setPromptText("Event color");
    colorPicker.setValue(EventColor.Fire);

    TextArea descriptionArea = new TextArea();
    descriptionArea.setMaxWidth(300);
    descriptionArea.setPromptText("Event description");

    Button confirmButton = new Button("Add Event");

    confirmButton.setOnAction(actionEvent -> {
      try {
        LocalDate eventLocalDate = eventDatePicker.getValue();
        Date eventDate = CalendarMath.localDateToModelDate(eventLocalDate);
        Time startTime = startTimePicker.getTime();
        Time endTime = endTimePicker.getTime();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        EventColor color = colorPicker.getValue();

        try {
          Event event = new Event(eventDate, startTime, endTime, title, description, color);
          addEvent.accept(event);

          Stage stage = (Stage) this.getScene().getWindow();
          stage.close();
        } catch (IllegalArgumentException iae) {
          ErrorMessage message = new ErrorMessage(this.getScene().getWindow(), iae.getMessage());
          message.show();
        }
      } catch (NullPointerException npe) {
        ErrorMessage message = new ErrorMessage(this.getScene().getWindow(),
            "All items must be filled in.");
        message.show();
      }
    });

    this.getChildren().addAll(titleField, eventDatePicker, startTimePicker, endTimePicker,
        descriptionArea, colorPicker, confirmButton);
  }
}
