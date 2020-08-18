package mycalendar.view;

import java.time.LocalDate;
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
 * Represents a window containing fields to allow a user edit an existing event in the calendar.
 */
public class EditEventPopup extends VBox {

  /**
   * Creates the window to allow the user to edit the given event.
   *
   * @param event        the event to edit
   * @param eventUpdater an operation which updates the calendar
   * @param deleteEvent  an operation which takes in an event and deletes it from the calendar
   */
  public EditEventPopup(Event event, Runnable eventUpdater, Consumer<Event> deleteEvent) {
    TextField titleField = new TextField();
    titleField.setMaxWidth(200);
    titleField.setPromptText("Event title");
    titleField.setText(event.getTitle());

    DatePicker eventDatePicker = new ValidDatePicker();
    eventDatePicker.setPromptText("Event date");
    eventDatePicker.setValue(CalendarMath.modelDateToLocalDate(event.getDate()));

    TimePicker startTimePicker = new TimePicker();
    startTimePicker.setAsStartTimePicker();
    startTimePicker.setTime(event.getStartTime());

    TimePicker endTimePicker = new TimePicker();
    endTimePicker.setAsEndTimePicker();
    endTimePicker.setTime(event.getEndTime());

    ComboBox<EventColor> colorPicker = new EventColorPicker();
    colorPicker.setValue(event.getColor());

    TextArea descriptionArea = new TextArea();
    descriptionArea.setMaxWidth(300);
    descriptionArea.setPromptText("Event description");
    descriptionArea.setText(event.getDescription());

    Button confirmButton = new Button("Update Event");
    Button deleteButton = new Button("Delete Event");
    deleteButton.setStyle("-fx-background-color: rgba(224,70,60,0.83)");

    confirmButton.setOnAction(actionEvent -> {
      try {
        LocalDate eventLocalDate = eventDatePicker.getValue();
        Date eventDate = CalendarMath.localDateToModelDate(eventLocalDate);
        Time newStartTime = startTimePicker.getTime();
        Time newEndTime = endTimePicker.getTime();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        EventColor color = colorPicker.getValue();

        try {
          event.setTitle(title);
          event.setDate(eventDate);
          event.setStartAndEndTimes(newStartTime, newEndTime);
          event.setDescription(description);
          event.setColor(color);

          eventUpdater.run();

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

    deleteButton.setOnAction(actionEvent -> {
      try {
        deleteEvent.accept(event);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
      } catch (IllegalArgumentException iae) {
        ErrorMessage message = new ErrorMessage(this.getScene().getWindow(), iae.getMessage());
        message.show();
      }
    });

    this.getChildren().addAll(titleField, eventDatePicker, startTimePicker, endTimePicker,
        descriptionArea, colorPicker, confirmButton, deleteButton);
  }
}
