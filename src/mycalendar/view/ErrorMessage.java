package mycalendar.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Represents an error message.
 */
public class ErrorMessage extends Stage {

  /**
   * Constructs an error message popup.
   *
   * @param stageOwner   the window which owns this error popup
   * @param errorMessage the error message to display
   */
  public ErrorMessage(Window stageOwner, String errorMessage) {
    VBox errorBox = new VBox();
    errorBox.getChildren().add(new Label(errorMessage));

    Button closeButton = new Button("OK");
    closeButton.setOnAction(e -> this.close());
    errorBox.getChildren().add(closeButton);

    Scene errorScene = new Scene(errorBox);
    this.setTitle("Error");
    this.setScene(errorScene);
    this.initOwner(stageOwner);
    this.initStyle(StageStyle.UTILITY);
    this.initModality(Modality.WINDOW_MODAL);
  }
}
