package mycalendar.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Represents a column showing a single day with dividers for each hour.
 */
public class DayColumn extends GridPane {

  /**
   * Constructs the column.
   */
  public DayColumn() {
    this.setStyle("-fx-background-color: #2a2a2a;-fx-border-color: #4d4259;-fx-border-width: 1.4;");

    for (int i = 0; i < 24; i++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setVgrow(Priority.ALWAYS);
      rowConstraints.setPercentHeight(100.0 / 24);
      this.getRowConstraints().add(rowConstraints);
    }

    for (int i = 0; i < 24; i++) {
      HBox pane = new HBox();
      pane.prefWidthProperty().bind(this.widthProperty());
      pane.setStyle("-fx-border-color: #726b80;-fx-border-style: solid;-fx-border-width: 0 0 2 0;");

      this.add(pane, 0, i);
    }
  }
}
