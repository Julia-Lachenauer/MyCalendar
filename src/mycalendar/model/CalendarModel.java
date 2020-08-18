package mycalendar.model;

import java.util.List;

/**
 * Represents the model for calendar program containing methods that a user can initiate to interact
 * with and modify the calendar. Each calendar contains a list of events, and that list has a
 * maximum length of 365,000 events.
 */
public interface CalendarModel {

  /**
   * Gets a copy of this calendar's list of events.
   *
   * @return a copy of this calendar's list of events
   */
  List<Event> getEvents();

  /**
   * Adds the given event to this calendar's list of events.
   *
   * @param event the event to add
   * @throws IllegalArgumentException if adding this event to the list causes the number of items in
   *                                  the list to exceed 365,000 events or if the new event is
   *                                  identical to any existing event in this calendar.
   */
  void addEvent(Event event) throws IllegalArgumentException;

  /**
   * Removes the given event from this calendar.
   *
   * @param event the event to remove from this calendar
   * @throws IllegalArgumentException if the given event is not found in this calendar
   */
  void removeEvent(Event event) throws IllegalArgumentException;

  /**
   * Outputs a formatted string representing this calendar containing all data for each event (start
   * date/time, end date/time, title, description, and color). The string returned by this method is
   * in the proper format to be read in to the program using the {@link CalendarReader} class.
   *
   * @return a formatted string representation of this calendar
   */
  String calendarInfo();
}
