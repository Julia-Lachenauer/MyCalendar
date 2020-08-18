package mycalendar.model.dateandtime;

import java.util.Objects;
import mycalendar.model.CalendarMath;

/**
 * Represents a time. Each time has an hour and a minute. Time is represented using the 24-hour
 * clock, so hours range from 0 to 23.
 */
public final class Time {

  private int hour;
  private int minute;

  /**
   * Constructs a time based on the given hour and minute where the given hour is based on the
   * 24-hour clock. Times are represented using the 24-hour clock, meaning an hour can range from 0
   * to 23.
   *
   * @param hour   the hour (using the 24-hour clock)
   * @param minute the minute
   * @throws IllegalArgumentException if the given hour is less than 0 or greater than 23 or if the
   *                                  given minute is less than 0 or greater than 59
   */
  public Time(int hour, int minute) throws IllegalArgumentException {
    CalendarMath.validateTime(hour, minute);

    this.hour = hour;
    this.minute = minute;
  }

  /**
   * Constructs a copy of the given time.
   *
   * @param time the date to copy
   */
  public Time(Time time) {
    this(time.hour, time.minute);
  }

  /**
   * Sets the hour and minute of this time to the hour and minute of the given time.
   *
   * @param time the time to set this time to
   */
  public void setTime(Time time) {
    this.hour = time.hour;
    this.minute = time.minute;
  }

  /**
   * Outputs this time using the 12-hour clock in the form {@code hh:mm am/pm}.
   *
   * @return a simple representation of this time using the 12-hour clock
   */
  public String simpleFormat() {
    String amPm = (this.hour < 12) ? "am" : "pm";
    return String.format("%02d:%02d %s", this.hourInTwelveHourClock(), this.minute, amPm);
  }

  /**
   * Determines if this time is before the given time (assuming both times are on the same date).
   * Returns false if the two times are the same or if this time is after the given time.
   *
   * @param time the time to compare this time to
   * @return whether or not this time is before the given time
   */
  public boolean before(Time time) {
    // This hour is earlier than given hour
    if (this.hour < time.hour) {
      return true;
    }

    // This hour is after given hour
    if (this.hour > time.hour) {
      return false;
    }

    // Same hour
    return this.minute < time.minute;
  }

  /**
   * Determines if this time is before or equal to the given time (assuming both times are on the
   * same date). Returns false if this time is after the given time.
   *
   * @param time the time to compare this time to
   * @return whether or not this time is before or equal to the given time
   */
  public boolean beforeOrSame(Time time) {
    // This hour is earlier than given hour
    if (this.hour < time.hour) {
      return true;
    }

    // This hour is after given hour
    if (this.hour > time.hour) {
      return false;
    }

    // Same hour
    return this.minute <= time.minute;
  }

  /**
   * Determines if this time is after the given time (assuming both times are on the same date).
   * Returns false if the two times are the same or if this time is before the given time.
   *
   * @param time the time to compare this time to
   * @return whether or not this time is after the given time
   */
  public boolean after(Time time) {
    // This hour is after given hour
    if (this.hour > time.hour) {
      return true;
    }

    // This hour is before given hour
    if (this.hour < time.hour) {
      return false;
    }

    // Same hour
    return this.minute > time.minute;
  }

  /**
   * Determines if this time is after or equal to the given time (assuming both times are on the
   * same date). Returns false if this time is before the given time.
   *
   * @param time the time to compare this time to
   * @return whether or not this time is after or equal to the given time
   */
  public boolean afterOrSame(Time time) {
    // This hour is after given hour
    if (this.hour > time.hour) {
      return true;
    }

    // This hour is before given hour
    if (this.hour < time.hour) {
      return false;
    }

    // Same hour
    return this.minute >= time.minute;
  }

  /**
   * Gets the hour of this time as represented by the 12-hour clock. The hour returned will be from
   * 1 to 12 inclusive.
   *
   * @return the hour of this time as represented by the 12-hour clock
   */
  public int hourInTwelveHourClock() {
    return (this.hour % 12 == 0) ? 12 : (this.hour % 12);
  }

  /**
   * Gets the hour of this time. Hours are represented using the 24-hour clock (meaning that hours
   * range from 0 to 23).
   *
   * @return the hour of this time
   */
  public int getHour() {
    return this.hour;
  }

  /**
   * Gets the minute of this time. Minutes range from 0 to 59.
   *
   * @return the minute of this time
   */
  public int getMinute() {
    return this.minute;
  }

  /**
   * Outputs the string value of this time in the form {@code hh mm}, where hh represents the hour
   * and mm represents the minute. Hours are represented using the 24-hour clock (meaning that hours
   * range from 0 to 23).
   *
   * @return a formatted string representation of this time
   */
  @Override
  public String toString() {
    return String.format("%02d %02d", this.hour, this.minute);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Time)) {
      return false;
    }

    Time that = (Time) obj;

    return this.hour == that.hour && this.minute == that.minute;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.hour, this.minute);
  }
}
