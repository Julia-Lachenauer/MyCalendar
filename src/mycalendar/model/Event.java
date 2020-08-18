package mycalendar.model;

import java.util.Objects;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.Time;

/**
 * Represents an event in a calendar. A calendar event has a date, a start time, an end time, a
 * title, a description, and a color. The start time is always chronologically before or the same as
 * the end time (this is checked and ensured by the constructors and methods).
 */
public final class Event {

  private final Date date;

  // INVARIANT: The start time is chronologically before or the same as the end time
  private final Time startTime;
  private final Time endTime;

  private String title;
  private String description;

  private EventColor color;

  /**
   * Constructs a new event with the given date, start/end times, title, description, and color.
   *
   * @param date        the date of this event
   * @param startTime   the start time of this event
   * @param endTime     the end time of this event
   * @param title       the title of this event
   * @param description the description of this event
   * @param color       the color to mark this event as in the calendar
   * @throws IllegalArgumentException if the end time is chronologically before the start time
   */
  public Event(Date date, Time startTime, Time endTime, String title, String description,
      EventColor color) throws IllegalArgumentException {
    if (startTime.after(endTime)) {
      throw new IllegalArgumentException("Error: start date/time must be before end date/time.");
    }

    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.title = title;
    this.description = description;
    this.color = color;
  }

  /**
   * Determines whether or not this event overlaps with the given event. An event A is considered to
   * be overlapping this event if either event A starts before this event starts and ends after this
   * event starts or if event A starts after this event starts before this event ends.
   *
   * <p>If an event A starts before this event starts but ends at the exact same date/time that
   * this event starts, this is not considered to be an overlap. Likewise, if an event A starts at
   * the exact same date/time that this event ends, this is not considered to be an overlap.
   *
   * <p>However, if an event A has the exact same start and end date/time as this event, it is
   * considered to be an overlap.
   *
   * @param event the event to check for overlaps
   * @return whether or not this event overlaps with the given event
   */
  public boolean overlap(Event event) {

    if (this.date.equals(event.date)) {
      return false;
    } else {
      // Given event starts before this event starts and ends after this event ends
      boolean eventStartsBeforeThisStarts = event.startTime.beforeOrSame(this.startTime);
      boolean eventEndsAfterThisStarts = event.startTime.afterOrSame(this.startTime);
      boolean overlapEventStartsBefore = eventStartsBeforeThisStarts && eventEndsAfterThisStarts;

      // Given event starts after this event starts but before this event ends
      boolean eventStartsAfterThisStarts = event.startTime.afterOrSame(this.startTime);
      boolean eventStartsBeforeThisEnds = event.startTime.beforeOrSame(this.endTime);
      boolean overlapEventStartsAfter = eventStartsAfterThisStarts && eventStartsBeforeThisEnds;

      // Given event has the same start and end
      boolean sameStart = this.startTime.equals(event.startTime);
      boolean sameEnd = this.endTime.equals(event.endTime);
      boolean overlapIdenticalStartEnd = sameStart && sameEnd;

      return overlapEventStartsBefore || overlapEventStartsAfter || overlapIdenticalStartEnd;
    }
  }

  /**
   * Gets the start date of this event.
   *
   * @return the start date of this event
   */
  public Date getDate() {
    return new Date(this.date);
  }

  /**
   * Gets the start time of this event.
   *
   * @return the start time of this event
   */
  public Time getStartTime() {
    return new Time(this.startTime);
  }

  /**
   * Gets the end time of this event.
   *
   * @return the end time of this event
   */
  public Time getEndTime() {
    return new Time(this.endTime);
  }

  /**
   * Gets the title of this event.
   *
   * @return the title of this event
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Gets the description of this event.
   *
   * @return the description of this event
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Gets the the color of this event.
   *
   * @return the color of this event
   */
  public EventColor getColor() {
    return this.color;
  }

  /**
   * Sets the date of this event to the given date.
   *
   * @param newDate the new date of this event
   */
  public void setDate(Date newDate) {
    this.date.setDate(newDate);
  }

  /**
   * Sets the start time of this event to the given start time.
   *
   * @param newStartTime the new start time
   * @param newEndTime   the new end time
   * @throws IllegalArgumentException if the new end time is chronologically before the new start
   *                                  time
   */
  public void setStartAndEndTimes(Time newStartTime, Time newEndTime)
      throws IllegalArgumentException {
    if (newEndTime.before(newStartTime)) {
      throw new IllegalArgumentException("Given end time cannot be before given end time");
    }

    this.startTime.setTime(newStartTime);
    this.endTime.setTime(newEndTime);
  }

  /**
   * Sets the title of this event to the given title.
   *
   * @param title the new title
   * @throws IllegalArgumentException if the title is the string "-------------------------------------"
   */
  public void setTitle(String title) throws IllegalArgumentException {
    if (title.equals("-------------------------------------")) {
      throw new IllegalArgumentException(
          "Title cannot be \"-------------------------------------\".");
    }

    this.title = title;
  }

  /**
   * Sets the description of this event to the given description.
   *
   * @param description the new description
   * @throws IllegalArgumentException if the description is the string "-------------------------------------"
   */
  public void setDescription(String description) throws IllegalArgumentException {
    if (description.equals("-------------------------------------")) {
      throw new IllegalArgumentException(
          "Description cannot be \"-------------------------------------\".");
    }

    this.description = description;
  }

  /**
   * Sets the color of this event to the given color.
   *
   * @param color the new color
   */
  public void setColor(EventColor color) {
    this.color = color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Event)) {
      return false;
    }

    Event that = (Event) obj;

    return this.toString().equals(that.toString());
  }

  /**
   * Outputs a formatted string representation of this event containing all data (date, start time,
   * end time, title, description, and color).
   *
   * @return a formatted string representation of this date containing all data for this event
   */
  @Override
  public String toString() {
    String eventStr = "";
    eventStr = eventStr.concat("-------------------------------------\n");
    eventStr = eventStr.concat("date: ").concat(this.date.toString());
    eventStr = eventStr.concat("\n");

    eventStr = eventStr.concat("-------------------------------------\n");

    eventStr = eventStr.concat("start_time: ").concat(this.startTime.toString());
    eventStr = eventStr.concat("\n");
    eventStr = eventStr.concat("end_time: ").concat(this.endTime.toString());
    eventStr = eventStr.concat("\n");

    eventStr = eventStr.concat("-------------------------------------\n");

    eventStr = eventStr.concat("title: \n");
    eventStr = eventStr.concat(this.title).concat("\n");

    eventStr = eventStr.concat("-------------------------------------\n");

    eventStr = eventStr.concat("description: \n");
    eventStr = eventStr.concat(this.description).concat("\n");

    eventStr = eventStr.concat("-------------------------------------\n");

    eventStr = eventStr.concat("color: ").concat(this.color.toString());
    eventStr = eventStr.concat(" - ").concat(this.color.rgbString()).concat("\n");

    eventStr = eventStr.concat("-------------------------------------\n");
    return eventStr;
  }
}
