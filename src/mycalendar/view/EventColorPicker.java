package mycalendar.view;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import mycalendar.model.EventColor;

/**
 * Represents a drop-down menu allowing the user to choose a color for an event to be displayed
 * with.
 */
public class EventColorPicker extends ComboBox<EventColor> {

  /**
   * Creates the color picker.
   */
  public EventColorPicker() {
    EventColor[] colors = EventColor.values();
    this.getItems().addAll(colors);

    this.setCellFactory(colorListView -> new ListCell<>() {
      private final Label label;

      {
        this.label = new Label();
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      }

      @Override
      protected void updateItem(EventColor color, boolean empty) {
        super.updateItem(color, empty);

        if (color == null || empty) {
          this.setGraphic(null);
        } else {
          this.label.setText(color.toString());
          Color cellColor = Color.rgb(color.getRed(), color.getGreen(), color.getBlue());

          this.setBackground(
              new Background(new BackgroundFill(cellColor, CornerRadii.EMPTY, Insets.EMPTY)));

          this.setGraphic(this.label);
        }
      }
    });

    this.setButtonCell(this.getCellFactory().call(null));
  }
}
