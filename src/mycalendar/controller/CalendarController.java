package mycalendar.controller;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mycalendar.FileManager;
import mycalendar.model.CalendarModel;
import mycalendar.model.Event;
import mycalendar.view.CalendarViewBase;

/**
 * Represents a controller used for facilitating interactions between the user and the calendar.
 * Also manages the application window (allows the user to move the window and maximize the window
 * brought to the top of the screen).
 */
public class CalendarController implements Features {

  private final CalendarModel model;
  private final CalendarViewBase view;

  private double xMove = 0;
  private double yMove = 0;

  /**
   * Constructs a controller for managing the calendar.
   *
   * @param model the calendar model
   * @param view  the calendar view
   * @throws IllegalArgumentException if the model or the view is null
   */
  public CalendarController(CalendarModel model, CalendarViewBase view)
      throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null.");
    }
    this.model = model;
    this.view = view;

    this.view.setFeatures(this);
    this.view.setEvents(this.model.getEvents());

    this.view.refresh();
  }

  @Override
  public void addEvent(Event event) {
    this.model.addEvent(event);
    this.refreshCalendar();
  }

  @Override
  public void updateCalendar() {
    this.refreshCalendar();
  }

  @Override
  public void deleteEvent(Event event) throws IllegalArgumentException {
    this.model.removeEvent(event);
    this.refreshCalendar();
  }

  /**
   * Saves and refreshes the calendar model.
   */
  private void refreshCalendar() {
    FileManager.saveCalendar();
    this.view.setEvents(this.model.getEvents());
    this.view.refresh();
  }

  /**
   * Sets up the application window and then runs the program.
   *
   * @param stage the stage to display
   */
  public void run(Stage stage) {
    Rectangle2D bounds = Screen.getPrimary().getBounds();

    double width = bounds.getWidth() * 0.9;
    double height = bounds.getHeight() * 0.9;

    this.setWindowFunctions(stage);

    stage.setResizable(true);
    stage.getIcons().add(new Image("/resources/icon.png"));
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setScene(new Scene(this.view, width, height));
    this.view.setScene(stage.getScene());
    stage.setTitle("MyCalendar");
    stage.show();
  }

  /**
   * Sets the window to be able to be moved and to maximize when brought to the top of the screen.
   *
   * @param stage the stage to display
   */
  private void setWindowFunctions(Stage stage) {
    this.view.setOnMousePressed(e -> {
      this.xMove = e.getSceneX();
      this.yMove = e.getSceneY();
    });

    this.view.setOnMouseDragged(e -> {
      stage.setX(e.getScreenX() - this.xMove);
      stage.setY(e.getScreenY() - this.yMove);
    });

    this.view.setOnMouseReleased(e -> {
      if (stage.getY() < 10) {
        this.view.maximize();
      }
    });
  }
}
