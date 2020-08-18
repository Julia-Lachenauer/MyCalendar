package mycalendar.model;

import java.util.InputMismatchException;
import java.util.Scanner;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.Time;

/**
 * Allows the user to generate a calendar from a properly formatted calendar file (or other properly
 * formatted input source).
 */
public final class CalendarReader {

  /**
   * Generates a calendar from a properly formatted calendar input source.
   *
   * @param readable the input source containing the properly formatted calendar information
   * @return a calendar with the data specified in the given input source
   * @throws IllegalStateException    if there is unexpected input
   * @throws IllegalArgumentException if the calendar data is invalid (such as an invalid date)
   */
  public static CalendarModel readCalendarFile(Readable readable)
      throws IllegalStateException, IllegalArgumentException {
    CalendarModel model = new CalendarModelImpl();

    Scanner scan = new Scanner(readable);

    while (scan.hasNext()) {
      if (scan.next().equals("##########################################")) {
        model.addEvent(CalendarReader.readEvent(scan));
      }
    }
    scan.close();

    return model;
  }

  /**
   * Generates an event from the event section of a properly formatted calendar input source.
   *
   * @param scan a scanner containing the input source of the properly formatted calendar
   *             information
   * @return an event with the data specified by the given input source
   * @throws IllegalStateException    if there is unexpected input in the input source
   * @throws IllegalArgumentException if the calendar data is invalid (such as an invalid date)
   */
  private static Event readEvent(Scanner scan)
      throws IllegalStateException, IllegalArgumentException {
    Date date = null;

    Time startTime = null;
    Time endTime = null;

    String title = "";
    String description = "";

    EventColor color = EventColor.Fire;

    inputLoop:
    while (scan.hasNext()) {
      String field = scan.next();

      switch (field) {
        case "date:":
          date = CalendarReader.readDate(scan);
          break;
        case "start_time:":
          startTime = CalendarReader.readTime(scan);
          break;
        case "end_time:":
          endTime = CalendarReader.readTime(scan);
          break;
        case "title:":
          title = CalendarReader.readTextBlock(scan);
          break;
        case "description:":
          description = CalendarReader.readTextBlock(scan);
          break;
        case "color:":
          color = CalendarReader.readColor(scan);
          break;
        case "##########################################":
          break inputLoop;
        default:
      }
    }
    return new Event(date, startTime, endTime, title, description, color);
  }

  /**
   * Generates a date from the date section of a properly formatted calendar input source.
   *
   * @param scan a scanner containing the input source of the properly formatted calendar
   *             information
   * @return a date with the data specified by the given input source
   * @throws IllegalStateException    if there is unexpected input in the input source
   * @throws IllegalArgumentException if the date specified by the input source is invalid (if the
   *                                  year is less than 1900 or greater than 3000, if the month is
   *                                  less than 1 or greater than 12, or if the day is less than 1
   *                                  or greater than the length of the month)
   */
  private static Date readDate(Scanner scan)
      throws IllegalStateException, IllegalArgumentException {
    int[] dateInts = new int[3];

    for (int i = 0; i < 3; i++) {
      try {
        dateInts[i] = scan.nextInt();
      } catch (InputMismatchException ime) {
        throw new IllegalStateException("Unexpected input");
      }
    }

    return new Date(dateInts[0], dateInts[1], dateInts[2]);
  }

  /**
   * Generates a time from the time section of a properly formatted calendar input source.
   *
   * @param scan a scanner containing the input source of the properly formatted calendar
   *             information
   * @return a time with the data specified by the given input source
   * @throws IllegalStateException    if there is unexpected input in the input source
   * @throws IllegalArgumentException if the time specified by the input source is invalid if the
   *                                  hour is less than 0 or greater than 23 or if the minute is
   *                                  less than 0 or greater than 59
   */
  private static Time readTime(Scanner scan)
      throws IllegalStateException, IllegalArgumentException {
    int[] timeInts = new int[2];

    for (int i = 0; i < 2; i++) {
      try {
        timeInts[i] = scan.nextInt();
      } catch (InputMismatchException ime) {
        throw new IllegalStateException("Unexpected input");
      }
    }

    return new Time(timeInts[0], timeInts[1]);
  }

  /**
   * Gets the color (using the predefined color names) from the color section of a properly
   * formatted calendar input source.
   *
   * @param scan a scanner containing the input source of the properly formatted calendar
   *             information
   * @return a color with the data specified by the given input source
   * @throws IllegalStateException    if the scanner is closed
   * @throws IllegalArgumentException if the specified color name is not defined
   */
  private static EventColor readColor(Scanner scan)
      throws IllegalStateException, IllegalArgumentException {
    return EventColor.valueOf(scan.next());
  }

  /**
   * Generates a string containing all text until the section break ("-------------------------------------")
   * from either the title or description section of a properly formatted calendar input source. All
   * line breaks within the text block are preserved.
   *
   * @param scan a scanner containing the input source of the properly formatted calendar
   *             information
   * @return the text block from the given input source
   */
  private static String readTextBlock(Scanner scan) {
    StringBuilder builder = new StringBuilder();

    boolean builderEmpty = true;
    while (scan.hasNextLine()) {
      if (scan.nextLine().equals("-------------------------------------")) {
        break;
      }
      if (!builderEmpty) {
        builder.append("\n");
      }
      builder.append(scan.nextLine());
      builderEmpty = false;
    }

    return builder.toString();
  }
}
