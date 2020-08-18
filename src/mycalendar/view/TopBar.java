package mycalendar.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Creates the top bar for the calendar application containing a live clock, controls for what date
 * is shown, and window controls (minimize, maximize, close).
 */
public class TopBar extends StackPane {

  /**
   * Creates an instance of the top bar.
   *
   * @param weekControls   controls for what date is shown
   * @param liveClock      a continuously updating clock showing the current date and time
   * @param windowControls controls for managing the window (minimize, maximize, close)
   */
  public TopBar(HBox weekControls, LiveClock liveClock, WindowControls windowControls) {
    BorderPane lowerBar = new BorderPane();
    BorderPane upperBar = new BorderPane();

    lowerBar.setCenter(weekControls);
    upperBar.setLeft(liveClock);
    upperBar.setRight(windowControls);

    upperBar.setPickOnBounds(false);

    this.getChildren().add(lowerBar);
    this.getChildren().add(upperBar);
    this.setStyle("-fx-background-color: #2c202f");
  }
}
