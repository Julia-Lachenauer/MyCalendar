package mycalendar.view;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Represents the controls pertaining to the window itself (minimize, maximize, close).
 */
public class WindowControls extends HBox {

  private double oldX;
  private double oldY;
  private double oldWidth;
  private double oldHeight;

  private boolean isMaximized;

  private Scene scene;

  /**
   * Creates an instance of the windows controls.
   *
   * @param oldX        the old x value of the window
   * @param oldY        the old y value of the window
   * @param oldWidth    the old window width
   * @param oldHeight   the old window height
   * @param isMaximized wither or not the window is currently maximized
   */
  public WindowControls(double oldX, double oldY, double oldWidth, double oldHeight,
      boolean isMaximized) {
    this.oldX = oldX;
    this.oldY = oldY;
    this.oldWidth = oldWidth;
    this.oldHeight = oldHeight;
    this.isMaximized = isMaximized;

    Button minimize = this.windowButton(new ImageView(new Image("/resources/minimize.png")));
    minimize.setOnAction(e -> {
      Stage stage = (Stage) minimize.getScene().getWindow();
      stage.setIconified(true);
    });

    Button maximize = this.windowButton(new ImageView(new Image("/resources/maximize.png")));
    maximize.setOnAction(e -> this.toggleMaximize());

    Button close = this.windowButton(new ImageView(new Image("/resources/close.png")));
    close.setOnAction(e -> {
      Stage stage = (Stage) close.getScene().getWindow();
      stage.close();
      Platform.exit();
    });
    close.setOnMouseEntered(e -> close.setStyle("-fx-background-color: rgba(220,28,0,0.86);"));

    this.getChildren().addAll(minimize, maximize, close);
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Creates an instance of a window controls button using the given image. This button does not
   * have any functionality.
   *
   * @param iconImage the image to set to the button
   * @return a window controls button with the given image
   */
  private Button windowButton(ImageView iconImage) {
    Button windowButton = new Button();
    windowButton.setMinSize(15, 15);

    iconImage.setScaleX(0.8);
    iconImage.setScaleY(0.8);
    windowButton.setGraphic(iconImage);
    windowButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

    windowButton.setOnMouseEntered(
        e -> windowButton.setStyle("-fx-background-color: rgba(243,235,235,0.3);"));
    windowButton.setOnMouseExited(e -> windowButton.setStyle("-fx-background-color: transparent;"));

    return windowButton;
  }

  /**
   * Updates the old window bounds (x, y, width, height) with the window's current x, y, width, and
   * height. This should only be called when the window is unmaximized.
   */
  public void updateOldBounds() {
    Stage stage = (Stage) this.scene.getWindow();

    this.oldX = stage.getX();
    this.oldY = stage.getY();
    this.oldWidth = stage.getWidth();
    this.oldHeight = stage.getHeight();
  }

  /**
   * Toggles whether or not the window is maximized.
   */
  public void toggleMaximize() {
    if (!this.isMaximized) {
      this.maximizeWindow();
    } else {
      this.unmaximizeWindow();
    }
  }

  /**
   * Maximizes the window.
   */
  public void maximizeWindow() {
    this.updateOldBounds();
    Stage stage = (Stage) this.scene.getWindow();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());

    this.isMaximized = true;
  }

  /**
   * Unmaximizes the window and sets it back to its old bounds.
   */
  private void unmaximizeWindow() {
    Stage stage = (Stage) this.scene.getWindow();

    stage.setX(this.oldX);
    stage.setY(this.oldY);
    stage.setWidth(this.oldWidth);
    stage.setHeight(this.oldHeight);
    this.isMaximized = false;
  }
}