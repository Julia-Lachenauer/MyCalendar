package mycalendar.view;

import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

/**
 * Represents a date picker which only allows dates to be chosen that are valid in the calendar
 * model (no earlier than 1900 and no later than 3000).
 */
public class ValidDatePicker extends DatePicker {

  /**
   * Creates an instance of the valid date picker.
   */
  public ValidDatePicker() {
    super();

    this.setDayCellFactory(datePicker -> new DateCell() {
      @Override
      public void updateItem(LocalDate localDate, boolean b) {
        super.updateItem(localDate, b);

        LocalDate earliest = LocalDate.of(1900, 1, 1);
        LocalDate latest = LocalDate.of(3000, 12, 31);

        if (localDate.isBefore(earliest) || localDate.isAfter(latest)) {
          this.setDisable(true);
          this.setStyle("-fx-background-color: gray");
        }

        if (localDate.equals(LocalDate.now())) {
          this.setStyle("-fx-background-color: cornflowerblue");
        }
      }
    });
  }
}
