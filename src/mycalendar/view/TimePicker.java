package mycalendar.view;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import mycalendar.model.dateandtime.Time;

/**
 * Represents a time picker allowing the user to choose the hour, minute, and whether the time is in
 * the morning (AM) or at night (PM).
 */
public class TimePicker extends HBox {

  private final ComboBox<Integer> hourPicker;
  private final ComboBox<String> minutePicker;
  private final ComboBox<String> AMPMPicker;

  /**
   * Creates an instance of the time picker.
   */
  public TimePicker() {
    this.hourPicker = this.hourPicker();
    this.minutePicker = this.minutePicker();
    this.AMPMPicker = this.AMPMPicker();

    this.getChildren().addAll(this.hourPicker, this.minutePicker, this.AMPMPicker);
  }

  /**
   * Gets the time selected by the user.
   *
   * @return the time selected by the user
   * @throws NullPointerException if not every field has been filled in
   */
  public Time getTime() throws NullPointerException {
    try {
      int chosenHour = this.hourPicker.getValue();
      String AMPM = this.AMPMPicker.getValue();

      int hour;

      if (chosenHour == 12) {
        if (AMPM.equals("AM")) {
          hour = 0;
        } else {
          hour = 12;
        }
      } else {
        if (AMPM.equals("AM")) {
          hour = chosenHour;
        } else {
          hour = chosenHour + 12;
        }
      }

      int minute = Integer.parseInt(this.minutePicker.getValue());

      return new Time(hour, minute);
    } catch (NullPointerException ignored) {
      throw new NullPointerException("Error");
    }
  }

  /**
   * Sets this time picker as one which will choose the start time for an event.
   */
  public void setAsStartTimePicker() {
    this.hourPicker.setPromptText("Start hour");
    this.minutePicker.setPromptText("Start minute");
    this.AMPMPicker.setPromptText("Start AM/PM");
  }

  /**
   * Sets this time picker as one which will choose the end time for an event.
   */
  public void setAsEndTimePicker() {
    this.hourPicker.setPromptText("End hour");
    this.minutePicker.setPromptText("End minute");
    this.AMPMPicker.setPromptText("End AM/PM");
  }

  /**
   * Sets the default time on this time picker to the given time.
   *
   * @param time the time to set this time picker to
   */
  public void setTime(Time time) {
    int hour = time.hourInTwelveHourClock();
    int minute = time.getMinute();
    boolean morning = (time.getHour()) < 12;

    this.hourPicker.setValue(hour);
    this.minutePicker.setValue(String.format("%02d", minute));
    this.AMPMPicker.setValue(morning ? "AM" : "PM");
  }

  /**
   * Creates the hour picker for this time picker.
   *
   * @return a dropdown picker containing the hours of the day (starting at 12, ending at 11)
   */
  private ComboBox<Integer> hourPicker() {
    ComboBox<Integer> hours = new ComboBox<>();

    hours.getItems().add(12);
    for (int i = 1; i < 12; i++) {
      hours.getItems().add(i);
    }

    return hours;
  }

  /**
   * Creates the minute picker for this time picker.
   *
   * @return a dropdown picker containing the minutes of the day (starting at 00, ending at 59)
   */
  private ComboBox<String> minutePicker() {
    ComboBox<String> minutes = new ComboBox<>();

    for (int i = 0; i < 60; i++) {
      minutes.getItems().add(String.format("%02d", i));
    }

    return minutes;
  }

  /**
   * Creates the AM/PM (morning/night) picker for this time picker.
   *
   * @return a dropdown picker containing the Strings "AM" and "PM"
   */
  private ComboBox<String> AMPMPicker() {
    ComboBox<String> amPm = new ComboBox<>();
    amPm.getItems().addAll("AM", "PM");
    return amPm;
  }
}
