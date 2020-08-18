package mycalendar.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * Represents the marks on the left side of the calendar which keep track of the time of day.
 */
public class TimeMarks extends StackPane {

  /**
   * Creates an instance of the time marks.
   */
  public TimeMarks() {
    super();

    GridPane timeColumn = new GridPane();

    for (int i = 0; i < 24; i++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setVgrow(Priority.ALWAYS);
      rowConstraints.setPercentHeight(100.0 / 24);
      timeColumn.getRowConstraints().add(rowConstraints);
    }

    for (int i = 0; i < 23; i++) {
      HBox pane = new HBox();
      pane.setAlignment(Pos.BOTTOM_RIGHT);
      pane.prefHeightProperty().bind(timeColumn.heightProperty().divide(24));

      int hourInt = ((i + 1) % 12 == 0) ? 12 : ((i + 1) % 12);
      String hourString = ((i + 1) < 12) ? hourInt + " AM" : hourInt + " PM";
      Label timeLabel = new Label(hourString);
      timeLabel.setStyle("-fx-text-fill: #bcbcbc");
      pane.getChildren().add(timeLabel);

      timeColumn.add(pane, 0, i);
    }

    this.getChildren().add(timeColumn);
    this.setPadding(new Insets(10, 0, 0, 0));
    this.setStyle("-fx-background-color: #454545");
  }

}
