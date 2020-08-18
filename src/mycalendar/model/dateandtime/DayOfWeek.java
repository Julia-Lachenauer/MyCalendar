package mycalendar.model.dateandtime;

/**
 * Represents the days of the week and each day's placement in the week starting with Sunday and
 * ending with Saturday. Each day of the week has a corresponding number starting with Sunday
 * corresponding to 1 and ending with Saturday corresponding to 7.
 */
public enum DayOfWeek {
  Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7);

  private final int dayOfWeekNum;


  /**
   * Constructs a day of the week using the given day of the week number (from 1 to 7) with Sunday
   * corresponding to 1 and Saturday corresponding to 7.
   *
   * @param dayOfWeekNum a number from 1 to 7 corresponding to the day of the week
   */
  DayOfWeek(int dayOfWeekNum) {
    this.dayOfWeekNum = dayOfWeekNum;
  }

  /**
   * Gets the number corresponding to this day of the week with Sunday corresponding to 1 and
   * Saturday corresponding to 7.
   *
   * @return the number from 1 to 7 corresponding to this day of the week
   */
  public int getDayOfWeekNum() {
    return this.dayOfWeekNum;
  }

  /**
   * Gets the day of the week based on the given number corresponding to a place in the week. Sunday
   * is represented by 1 and Saturday is represented by 7.
   *
   * @param dayNum the number corresponding to a place in the week
   * @return the day of the week which the given number represents
   * @throws IllegalArgumentException if the given number is less than 1 or greater than 7
   */
  public static DayOfWeek getDayOfWeek(int dayNum) throws IllegalArgumentException {
    if (dayNum < 1 || dayNum > 7) {
      throw new IllegalArgumentException("Day number must be within 1 and 7 inclusive.");
    }

    DayOfWeek dayToReturn = null;

    switch (dayNum) {
      case 1:
        dayToReturn = DayOfWeek.Sunday;
        break;
      case 2:
        dayToReturn = DayOfWeek.Monday;
        break;
      case 3:
        dayToReturn = DayOfWeek.Tuesday;
        break;
      case 4:
        dayToReturn = DayOfWeek.Wednesday;
        break;
      case 5:
        dayToReturn = DayOfWeek.Thursday;
        break;
      case 6:
        dayToReturn = DayOfWeek.Friday;
        break;
      case 7:
        dayToReturn = DayOfWeek.Saturday;
        break;
    }

    return dayToReturn;
  }
}
