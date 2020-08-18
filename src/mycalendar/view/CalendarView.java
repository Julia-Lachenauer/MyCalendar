package mycalendar.view;

import java.util.List;
import javafx.scene.Scene;
import mycalendar.controller.Features;
import mycalendar.model.Event;

/**
 * Contains the functions required to set up the user interface for the calendar.
 */
public interface CalendarView {

  /**
   * Maximizes the application.
   */
  void maximize();

  /**
   * Supplies the GUI with the events from the calendar.
   *
   * @param events the events to display
   */
  void setEvents(List<Event> events);

  /**
   * Supplies the GUI with the given set of features in order to add more functionality to the
   * program.
   *
   * @param features an implementation of the Features interface defining methods to add
   *                 functionality to the program
   */
  void setFeatures(Features features);

  /**
   * Sets the scene to display with the given scene.
   *
   * @param scene the scene to display
   */
  void setScene(Scene scene);

  /**
   * Refreshes the GUI.
   */
  void refresh();
}
