package mycalendar.model.dateandtime;

import java.util.Objects;
import mycalendar.model.CalendarMath;

/**
 * Represents a date. Each date has a year, month, and day.
 */
public final class Date {

  private int year;
  private int month;
  private int day;

  /**
   * Constructs a date based on the given year, month, and day.
   *
   * @param year  the year
   * @param month the month
   * @param day   the day
   * @throws IllegalArgumentException if the given year is less than 1900 or greater than 3000, if
   *                                  the given month is less than 1 or greater than 12, or if the
   *                                  given day is less than 1 or greater than the length of the
   *                                  given month
   */
  public Date(int year, int month, int day) throws IllegalArgumentException {
    CalendarMath.validateDate(year, month, day);

    this.year = year;
    this.month = month;
    this.day = day;
  }

  /**
   * Constructs a copy of the given date.
   *
   * @param date the date to copy
   */
  public Date(Date date) {
    this(date.year, date.month, date.day);
  }

  /**
   * Outputs a formatted, readable representation of this date.
   *
   * @return a formatted String of this date in the form July 26, 2020 (for whatever this date is)
   */
  public String formattedDate() {
    return String.format("%s %d, %d", MonthName.getMonthName(this.month), this.day, this.year);
  }

  /**
   * Gets the year of this date.
   *
   * @return the year of this date
   */
  public int getYear() {
    return this.year;
  }

  /**
   * Gets the month of this date.
   *
   * @return the month of this date
   */
  public int getMonth() {
    return this.month;
  }

  /**
   * Gets the day of this date.
   *
   * @return the day of this date
   */
  public int getDay() {
    return this.day;
  }

  /**
   * Sets the year, month, and day of this date to the year, month, and day of the given date.
   *
   * @param date the date to set this date to
   */
  public void setDate(Date date) {
    this.year = date.year;
    this.month = date.month;
    this.day = date.day;
  }

  /**
   * Outputs the string value of this date in the form {@code yyyy mm dd}.
   *
   * @return a formatted string representation of this date
   */
  @Override
  public String toString() {
    return String.format("%d %02d %02d", this.year, this.month, this.day);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Date)) {
      return false;
    }

    Date that = (Date) obj;

    return this.year == that.year && this.month == that.month && this.day == that.day;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.year, this.month, this.day);
  }
}
