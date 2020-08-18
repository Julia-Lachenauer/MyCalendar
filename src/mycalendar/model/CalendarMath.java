package mycalendar.model;

import java.time.LocalDate;
import java.time.LocalTime;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.Time;
import mycalendar.model.dateandtime.DayOfWeek;
import mycalendar.model.dateandtime.MonthName;

/**
 * Represents operations that can be done on dates and times.
 */
public final class CalendarMath {

  /**
   * Gets the current date.
   *
   * @return the current date
   * @throws IllegalArgumentException if the current date is outside the range of the calendar
   *                                  (before 1900 or after 3000)
   */
  public static Date getCurrentDate() throws IllegalArgumentException {
    LocalDate currentDate = LocalDate.now();
    return localDateToModelDate(currentDate);
  }

  /**
   * Gets the current time.
   *
   * @return the current time
   */
  public static Time getCurrentTime() {
    LocalTime currentTime = LocalTime.now();
    return localTimeToModelTime(currentTime);
  }

  /**
   * Gets the number of five minute increments from midnight to the given time.
   *
   * @param time the time
   * @return the number of five minute increments from midnight to the given time
   */
  public static int num5MinIncrementsFromMidnight(Time time) {
    int fullHourIncrements = time.getHour() * 12;

    int minute = time.getMinute();

    int partialHourIncrements = 0;
    for (int i = 5; i < 60; i += 5) {
      if (minute >= i) {
        partialHourIncrements++;
      }
    }

    return fullHourIncrements + partialHourIncrements;
  }

  /**
   * Gets the number of five minute increments between the two given times.
   *
   * @param startTime the start time
   * @param endTime   the end time
   * @return the number of five minute increments between the start time and the end time
   * @throws IllegalArgumentException if the end time is after the start time
   */
  public static int num5MinuteIncrementsBetween(Time startTime, Time endTime)
      throws IllegalArgumentException {
    if (endTime.before(startTime)) {
      throw new IllegalArgumentException("Error: End time cannot be before start time");
    }

    if (endTime.getHour() == startTime.getHour()) {
      return (endTime.getMinute() - startTime.getMinute()) / 5;
    } else {
      int minutesInStartHour = 60 - startTime.getMinute();
      int minutesInEndHour = endTime.getMinute();

      int partialHourIncrements = 0;

      for (int i = 5; i < 60; i += 5) {
        if (minutesInStartHour >= i) {
          partialHourIncrements++;
        }
      }

      for (int i = 5; i < 60; i += 5) {
        if (minutesInEndHour >= i) {
          partialHourIncrements++;
        }
      }

      int fullHourIncrements = (endTime.getHour() - startTime.getHour() - 1) * 12;

      return partialHourIncrements + fullHourIncrements;
    }
  }

  /**
   * Converts the given LocalDate to a calendar model date.
   *
   * @param localDate the LocalDate to convert
   * @return the conversion of the given LocalDate to a calendar model date
   * @throws IllegalArgumentException if the given LocalDate represents a date outside the range of
   *                                  the calendar (before 1900 or after 3000)
   */
  public static Date localDateToModelDate(LocalDate localDate) throws IllegalArgumentException {
    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    int day = localDate.getDayOfMonth();

    return new Date(year, month, day);
  }

  /**
   * Converts the given calendar model date to a LocalDate.
   *
   * @param date the calendar model date
   * @return the given calendar model date as a LocalDate
   */
  public static LocalDate modelDateToLocalDate(Date date) {
    int year = date.getYear();
    int month = date.getMonth();
    int day = date.getDay();

    return LocalDate.of(year, month, day);
  }

  /**
   * Converts the given LocalTime to a calendar model time.
   *
   * @param localTime the LocalTime to convert
   * @return the conversion of the given LocalTime to a calendar model time
   */
  public static Time localTimeToModelTime(LocalTime localTime) {
    int hour = localTime.getHour();
    int minute = localTime.getMinute();

    return new Time(hour, minute);
  }

  /**
   * Gets the Sunday of the week containing the given date. If the given date is before January 6,
   * 1900, then Monday, January 1st is returned instead.
   *
   * @param date the date
   * @return the Sunday of the week containing the given date
   */
  public static Date getSundayOfWeek(Date date) {
    int year = date.getYear();
    int month = date.getMonth();
    int day = date.getDay();

    if (year == 1900 && day <= 6) {
      return new Date(1900, 1, 1);
    }

    int numDaysToSubtract = CalendarMath.getDayOfWeek(year, month, day).getDayOfWeekNum() - 1;
    return CalendarMath.getNormalizedDate(year, month, day - numDaysToSubtract);
  }

  public static Date goForwardDays(Date date, int numDays) {
    Date daysAhead = date;

    try {
      daysAhead = CalendarMath.incrementDays(date, numDays, true);
    } catch (IllegalArgumentException ignored) {
    }

    return daysAhead;
  }

  /**
   * Gets the date one week after the given date.
   *
   * @param date the initial date
   * @return the date one week (7 days) after the given date
   * @throws IllegalArgumentException if the resulting date is outside the calendar's range (before
   *                                  1900 or after 3000)
   */
  public static Date goForwardOneWeek(Date date) throws IllegalArgumentException {
    return CalendarMath.incrementDays(date, 7, true);
  }

  /**
   * Gets the date one week before the given date.
   *
   * @param date the initial date
   * @return the date one week (7 days) before the given date
   * @throws IllegalArgumentException if the resulting date is outside the calendar's range (before
   *                                  1900 or after 3000)
   */
  public static Date goBackOneWeek(Date date) {
    return CalendarMath.incrementDays(date, 7, false);
  }

  /**
   * Gets the day of the week of the given date.
   *
   * @param year  the year
   * @param month the month
   * @param day   the day
   * @return the day of the week of the given date
   * @throws IllegalArgumentException if the given date is invalid (if the year is less than 1900 or
   *                                  greater than 3000, if the month is less than 1 or greater than
   *                                  12, or if the day is less than 1 or greater than the length of
   *                                  the given month)
   */
  public static DayOfWeek getDayOfWeek(int year, int month, int day)
      throws IllegalArgumentException {
    CalendarMath.validateDate(year, month, day);

    // Jan 1, 1900 was a Monday (Monday is index 2)
    int daysFromBeginning = CalendarMath.daysBetween(1900, 1, 1, year, month, day);

    int indexOfDay = ((daysFromBeginning + 1) % 7) + 1;

    return DayOfWeek.getDayOfWeek(indexOfDay);
  }

  /**
   * Gets the number of days between the two given dates. The end date is counted, but the start
   * date is not counted. The order of date 1 and date 2 does not matter.
   *
   * @param year1  the year of date 1
   * @param month1 the month of date 1
   * @param day1   the day of date 1
   * @param year2  the year of date 2
   * @param month2 the month of date 2
   * @param day2   the day of date 2
   * @return the number of days between the two given dates
   * @throws IllegalArgumentException if the given date 1 or date 2 are invalid (if the year is less
   *                                  than 1900 or greater than 3000, if the month is less than 1 or
   *                                  greater than 12, or if the day is less than 1 or greater than
   *                                  the length of the given month)
   */
  public static int daysBetween(int year1, int month1, int day1, int year2, int month2, int day2)
      throws IllegalArgumentException {
    CalendarMath.validateDate(year1, month1, day1);
    CalendarMath.validateDate(year2, month2, day2);

    if (year1 == year2) {
      return CalendarMath.daysBetweenSameYear(year1, month1, day1, month2, day2);
    }

    Date initDate = getDate(year1, month1, day1, year2, month2, day2, true);
    Date finalDate = getDate(year1, month1, day1, year2, month2, day2, false);

    int numDays = 0;

    for (int y = initDate.getYear() + 1; y <= finalDate.getYear() - 1; y++) {
      numDays += CalendarMath.leapYear(y) ? 366 : 365;
    }

    numDays += CalendarMath.daysBetweenSameYear(initDate.getYear(), initDate.getMonth(),
        initDate.getDay(), 12, 31);
    numDays += CalendarMath.daysBetweenSameYear(finalDate.getYear(), 1, 1, finalDate.getMonth(),
        finalDate.getDay()) + 1;

    return numDays;
  }

  /**
   * Determines if the date represented by the given first year, month, and day is chronologically
   * before the date represented by the given second year, month, and day. Returns false if the two
   * dates are the same or if the first date is after the second date.
   *
   * @param firstYear   the year of the first date
   * @param firstMonth  the month of the first date
   * @param firstDay    the day of the first date
   * @param secondYear  the year of the second date
   * @param secondMonth the month of the second date
   * @param secondDay   the day of the second date
   * @return whether or not the first date is chronologically before the second date
   * @throws IllegalArgumentException if the first or second date is invalid (if the year is less
   *                                  than 1900 or greater than 3000, if the month is less than 1 or
   *                                  greater than 12, or if the day is less than 1 or greater than
   *                                  the length of the given month)
   */
  public static boolean datesInOrder(int firstYear, int firstMonth, int firstDay, int secondYear,
      int secondMonth, int secondDay) throws IllegalArgumentException {
    CalendarMath.validateDate(firstYear, firstMonth, firstDay);
    CalendarMath.validateDate(secondYear, secondMonth, secondDay);

    if (firstYear < secondYear) {
      return true;
    }

    // Same year
    if (firstMonth < secondMonth) {
      return true;
    }

    // Same month
    return firstDay < secondDay;
  }

  /**
   * Determine if the given year is a leap year. Leap years occur on years divisible by 4 but not
   * divisible by 100 (unless the year is divisible by 400).
   *
   * @param year the year to check
   * @return whether or not the given year is a leap year
   */
  public static boolean leapYear(int year) {
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
  }

  /**
   * Checks if the given date is valid and throws an error if it is not.
   *
   * @param year  the year
   * @param month the month
   * @param day   the day
   * @throws IllegalArgumentException if the year is less than 1900 or greater than 3000, if the
   *                                  month is less than 1 or greater than 12, or if the day is less
   *                                  than 1 or greater than the length of the given month
   */
  public static void validateDate(int year, int month, int day) throws IllegalArgumentException {
    int monthLength = MonthName.getMonthLength(year, month);

    if (year < 1900 || year > 3000 || month < 1 || month > 12 || day < 1 || day > monthLength) {
      throw new IllegalArgumentException("Invalid date. y:" + year + " m:" + month + " d:" + day);
    }
  }

  /**
   * Checks if the given time is valid and throws an error if it is not. Times are to be given based
   * on the 24-hour clock.
   *
   * @param hour   the hour (using the 24-hour clock)
   * @param minute the minute
   * @throws IllegalArgumentException if the hour is less than 0 or greater than 23 or if the minute
   *                                  is less than 0 or greater than 59
   */
  public static void validateTime(int hour, int minute) throws IllegalArgumentException {
    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
      throw new IllegalArgumentException("Invalid time.");
    }
  }

  /**
   * Increments the given date (either forward or backward) by the given number of days.
   *
   * @param date    the given start date
   * @param numDays the number of days to increment the given date by
   * @param add     true if adding the days, false if subtracting the days
   * @return the given start date incremented (forward or backward) by the given number of days
   * @throws IllegalArgumentException if the resulting date is outside the calendar's range (before
   *                                  1900 or after 3000)
   */
  private static Date incrementDays(Date date, int numDays, boolean add)
      throws IllegalArgumentException {
    int year = date.getYear();
    int month = date.getMonth();
    int day = date.getDay();

    if (add) {
      return CalendarMath.getNormalizedDate(year, month, day + numDays);
    } else {
      return CalendarMath.getNormalizedDate(year, month, day - numDays);
    }
  }

  /**
   * Gets the number of days between two dates where both dates are in the same given year. The
   * order of the two dates does not matter. The end date is included, but the start date is not
   * included.
   *
   * @param year   the year containing both given dates
   * @param month1 the month of date 1
   * @param day1   the day of date 1
   * @param month2 the month of date 2
   * @param day2   the day of date 2
   * @return the number of days between the two given dates within the same year
   * @throws IllegalArgumentException if the given date 1 or date 2 are invalid where date 1 and
   *                                  date 2 both use the same given year (if the year is less than
   *                                  1900 or greater than 3000, if the month is less than 1 or
   *                                  greater than 12, or if the day is less than 1 or greater than
   *                                  the length of the given month)
   */
  private static int daysBetweenSameYear(int year, int month1, int day1, int month2, int day2)
      throws IllegalArgumentException {
    CalendarMath.validateDate(year, month1, day1);
    CalendarMath.validateDate(year, month2, day2);

    Date initDate = getDate(year, month1, day1, year, month2, day2, true);
    Date finalDate = getDate(year, month1, day1, year, month2, day2, false);

    int initMonth = initDate.getMonth();
    int finalMonth = finalDate.getMonth();

    int initDay = initDate.getDay();
    int finalDay = finalDate.getDay();

    if (initMonth == finalMonth) {
      return finalDay - initDay;
    }

    int numDays = 0;

    for (int m = initMonth + 1; m <= finalMonth - 1; m++) {
      numDays += MonthName.getMonthLength(year, m);
    }

    numDays += (MonthName.getMonthLength(year, initMonth) - initDay);
    numDays += finalDay;

    return numDays;
  }

  /**
   * Returns the date representing the given number of years, months, and days. The given numbers of
   * years, months, and days do not need to fit the restrictions of a standard date (such as months
   * having to be from 1 to 12).
   *
   * @param years  the number of years
   * @param months the number of months
   * @param days   the number of days
   * @return a date representing the given number of years, months, and days
   * @throws IllegalArgumentException if the result of normalizing the given date produces a date
   *                                  past the year 3000 or before the year 1900 (dates after the
   *                                  year 3000 or before the year 1900 are not supported)
   */
  private static Date getNormalizedDate(int years, int months, int days)
      throws IllegalArgumentException {
    // 1914 24 62 -> 1915 12 62 -> 1915 13 31 -> 1916 01 31
    // 1914 17 62 -> 1915 05 62 -> 1915 06 31 -> 1915 07 01
    // 2020 09 34 -> 2020 10 06
    // 2020 09 -2 -> 2020 08 29

    while (months < 1) {
      months += 12;
      years--;
    }

    while (months > 12) {
      months -= 12;
      years++;
    }

    while (days < 1) {
      if (months == 1) {
        months = 12;
        years--;
      } else {
        months--;
      }
      days += MonthName.getMonthLength(years, months);
    }

    while (days > 31) {
      days -= MonthName.getMonthLength(years, months);

      if (months == 12) {
        months = 1;
        years++;
      } else {
        months++;
      }
    }

    int monthLength = MonthName.getMonthLength(years, months);

    if (days > monthLength) {
      days -= monthLength;

      if (months == 12) {
        months = 1;
        years++;
      } else {
        months++;
      }
    }

    return new Date(years, months, days);
  }

  /**
   * Returns either the chronologically first or second date from the given two dates based on the
   * given boolean.
   *
   * @param year1    the year of date 1
   * @param month1   the month of date 1
   * @param day1     the day of date 1
   * @param year2    the year of date 2
   * @param month2   the month of date 2
   * @param day2     the day of date 2
   * @param initDate true if the chronologically first date should be returned, false if the
   *                 chronologically second date should be returned
   * @return either the chronologically first or second date from the given two dates
   * @throws IllegalArgumentException if the given date 1 or date 2 are invalid (if the year is less
   *                                  than 1900 or greater than 3000, if the month is less than 1 or
   *                                  greater than 12, or if the day is less than 1 or greater than
   *                                  the length of the given month)
   */
  private static Date getDate(int year1, int month1, int day1, int year2, int month2, int day2,
      boolean initDate) throws IllegalArgumentException {
    boolean date1First = CalendarMath.datesInOrder(year1, month1, day1, year2, month2, day2);

    int initYear = date1First ? year1 : year2;
    int initMonth = date1First ? month1 : month2;
    int initDay = date1First ? day1 : day2;

    int finalYear = date1First ? year2 : year1;
    int finalMonth = date1First ? month2 : month1;
    int finalDay = date1First ? day2 : day1;

    if (initDate) {
      return new Date(initYear, initMonth, initDay);
    } else {
      return new Date(finalYear, finalMonth, finalDay);
    }
  }
}