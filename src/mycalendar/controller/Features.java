package mycalendar.controller;

import mycalendar.model.Event;

/**
 * Represents features that a user can use to interact with the calendar.
 */
public interface Features {

  /**
   * Adds the given event to the calendar.
   *
   * @param event the event to add
   */
  void addEvent(Event event);

  /**
   * Updates all events in the calendar.
   */
  void updateCalendar();

  /**
   * Removes the given event from the calendar.
   *
   * @param event the event to remove
   * @throws IllegalArgumentException if the given event is not found in the calendar
   */
  void deleteEvent(Event event) throws IllegalArgumentException;
}
