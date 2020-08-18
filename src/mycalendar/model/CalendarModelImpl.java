package mycalendar.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an implementation of the calendar model implementing all methods to allow the user to
 * use and modify the calendar.
 */
public class CalendarModelImpl implements CalendarModel {

  // INVARIANT: The length of events <= 365,000 events
  private final List<Event> events;

  /**
   * Constructs a new calendar with an empty list of events.
   */
  public CalendarModelImpl() {
    this.events = new ArrayList<>();
  }

  @Override
  public List<Event> getEvents() {
    return new ArrayList<>(this.events);
  }

  @Override
  public void addEvent(Event event) throws IllegalArgumentException {
    for (Event existingEvent : this.events) {
      if (existingEvent.equals(event)) {
        throw new IllegalArgumentException("Identical event already exists in this calendar");
      }
    }

    this.validateCalendarAddingEvent();
    this.events.add(event);
  }

  @Override
  public void removeEvent(Event event) throws IllegalArgumentException {
    int indexToDelete = -1;
    for (int i = 0; i < this.events.size(); i++) {
      Event eventToRemove = this.events.get(i);

      if (eventToRemove.equals(event)) {
        indexToDelete = i;
        break;
      }
    }

    if (indexToDelete == -1) {
      throw new IllegalArgumentException(
          "Cannot delete: The given event does not exist in this calendar.");
    } else {
      this.events.remove(indexToDelete);
    }
  }

  @Override
  public String calendarInfo() {
    StringBuilder builder = new StringBuilder();

    int numEvents = this.events.size();
    for (int i = 0; i < numEvents; i++) {
      Event event = this.events.get(i);

      builder.append("##########################################\n");
      builder.append("Event ").append(i + 1).append("/").append(numEvents).append("\n");
      builder.append(event.toString());
      builder.append("##########################################");

      if (i < numEvents - 1) {
        builder.append("\n");
      }
    }

    return builder.toString();
  }


  /**
   * Checks if this calendar's list of events has a length greater than or equal to 365,000 and
   * throws an error if this is the case. Although the calendar is allowed to contain exactly
   * 365,000 events (this is the maximum allowed value), this method is to be called before adding a
   * new event to this calendar, meaning that if the calendar has exactly 365,000 events, adding one
   * more event will cause the calendar to be invalid.
   *
   * @throws IllegalArgumentException if this calendar's list of events contains 365,000 or more
   *                                  events
   */
  private void validateCalendarAddingEvent() throws IllegalArgumentException {
    if (this.events.size() >= 365000) {
      throw new IllegalArgumentException("Calendar size exceeded. "
          + "Calendar can hold a maximum of 365,000 events.");
    }
  }
}
