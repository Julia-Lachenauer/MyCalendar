package mycalendar;

import javafx.application.Application;
import javafx.stage.Stage;
import mycalendar.controller.CalendarController;
import mycalendar.model.CalendarModel;
import mycalendar.view.CalendarViewBase;

/**
 * Allows for the program to be run.
 */
public class MyCalendar extends Application {

  /**
   * Launches the program.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    FileManager.openFile();
    CalendarModel model = FileManager.OPEN_CALENDAR;
    CalendarViewBase root = new CalendarViewBase();
    CalendarController controller = new CalendarController(model, root);

    controller.run(stage);
  }
}
