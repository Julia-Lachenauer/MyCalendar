package mycalendar.model.dateandtime;

import mycalendar.model.CalendarMath;

/**
 * Represents the months of the year. Each month has its length in days. There are two
 * representations for February: one with 28 days and one with 29 days to be used in leap years.
 */
public enum MonthName {
  January(31), February(28), March(31), April(30), May(31), June(30), July(31), August(31),
  September(30), October(31), November(30), December(31), FebruaryLeap(29);

  private final int days;

  /**
   * Constructs a Month using the given number of days.
   *
   * @param days the number of days that this month has
   */
  MonthName(int days) {
    this.days = days;
  }

  /**
   * Gets the name of the month based on the given month number.
   *
   * @param month the number corresponding to the month's placement in the year
   * @return the name of the given month
   * @throws IllegalArgumentException if the given month is less than 1 or greater than 12
   */
  public static String getMonthName(int month) throws IllegalArgumentException {
    MonthName monthName = MonthName.getMonthFromMonthNum(month, false);
    return monthName.toString();
  }

  /**
   * Gets the length of the month based on the given year and month number. The year is used in the
   * case that the month to be returned is a February during a leap year.
   *
   * @param year  the year
   * @param month the number corresponding to the month's placement in the year
   * @return length in days of the month based on the given year and month number
   * @throws IllegalArgumentException if the given month is less than 1 or greater than 12
   */
  public static int getMonthLength(int year, int month) throws IllegalArgumentException {
    return MonthName.getMonthFromMonthNum(month, CalendarMath.leapYear(year)).getDays();
  }

  /**
   * Gets the length of this month in days.
   *
   * @return the length of this month in days
   */
  private int getDays() {
    return this.days;
  }

  /**
   * Gets the month based on the given month number and given boolean representing if the month
   * takes place during a leap year.
   *
   * @param month the number corresponding to the month's placement in the year
   * @param leap  whether or not the month takes place during a leap year
   * @return the month based on the given month number and given boolean representing whether or not
   * the month takes place during a leap year
   * @throws IllegalArgumentException if the given month number is less than 1 or greater than 12
   */
  private static MonthName getMonthFromMonthNum(int month, boolean leap)
      throws IllegalArgumentException {
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Month number must be between 1 and 12 inclusive.");
    }

    MonthName monthNameToReturn = null;

    switch (month) {
      case 1:
        monthNameToReturn = MonthName.January;
        break;
      case 2:
        monthNameToReturn = leap ? MonthName.FebruaryLeap : MonthName.February;
        break;
      case 3:
        monthNameToReturn = MonthName.March;
        break;
      case 4:
        monthNameToReturn = MonthName.April;
        break;
      case 5:
        monthNameToReturn = MonthName.May;
        break;
      case 6:
        monthNameToReturn = MonthName.June;
        break;
      case 7:
        monthNameToReturn = MonthName.July;
        break;
      case 8:
        monthNameToReturn = MonthName.August;
        break;
      case 9:
        monthNameToReturn = MonthName.September;
        break;
      case 10:
        monthNameToReturn = MonthName.October;
        break;
      case 11:
        monthNameToReturn = MonthName.November;
        break;
      case 12:
        monthNameToReturn = MonthName.December;
        break;
    }

    return monthNameToReturn;
  }
}
