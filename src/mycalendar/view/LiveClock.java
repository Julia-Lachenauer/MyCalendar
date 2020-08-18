package mycalendar.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.Duration;
import mycalendar.model.CalendarMath;
import mycalendar.model.dateandtime.Date;

/**
 * Represents a clock showing the date and time which updates every second.
 */
public class LiveClock extends VBox {

  private final Runnable refresh;
  private final Date currentViewDate;

  /**
   * Creates an instance of the live clock.
   *
   * @param refresh         an operation which refreshes the GUI
   * @param currentViewDate the date currently being viewed in the calendar
   */
  public LiveClock(Runnable refresh, Date currentViewDate) {
    super();
    this.refresh = refresh;
    this.currentViewDate = currentViewDate;

    this.resetDateToToday();

    Label dateLabel = new Label();
    Label timeLabel = new Label();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a");

    Timeline dateTimeLine = this
        .updateEachSecond(event -> dateLabel.setText(LocalDate.now().format(dateFormatter)));
    Timeline timeTimeLine = this.updateEachSecond(event -> timeLabel
        .setText(LocalTime.now().format(timeFormatter).replace("AM", "am").replace("PM", "pm")));

    dateTimeLine.play();
    timeTimeLine.play();

    dateLabel.setFont(Font.font("Century Gothic", 26));
    dateLabel.setUnderline(true);
    dateLabel.setStyle("-fx-text-fill: #9e9e9e");

    timeLabel.setFont(Font.font("Century Gothic", FontPosture.ITALIC, 12));
    timeLabel.setStyle("-fx-text-fill: #9e9e9e");

    Button viewTodayButton = this.viewCurrentDay(dateLabel);

    HBox timeBox = new HBox();
    timeBox.setAlignment(Pos.CENTER_RIGHT);
    timeBox.getChildren().add(timeLabel);
    timeBox.setMaxWidth(timeLabel.getMaxWidth());

    this.setSpacing(5);
    this.setMaxWidth(220);
    this.getChildren().add(viewTodayButton);
    this.getChildren().add(timeBox);
  }

  /**
   * Resets the currently viewed date to today and refreshes the GUI.
   */
  public void resetDateToToday() {
    this.currentViewDate.setDate(CalendarMath.getCurrentDate());
    this.refresh.run();
  }

  /**
   * Creates a button containing the given label which when pressed resets the date to today.
   *
   * @param dateLabel a label containing today's date (and should be continuously updated)
   * @return a button displaying the current date which resets the date to today when pressed
   */
  private Button viewCurrentDay(Label dateLabel) {
    Button viewTodayButton = new Button();

    viewTodayButton.setGraphic(dateLabel);
    viewTodayButton.setStyle(
        "-fx-text-fill: #9e9e9e;-fx-background-color: rgba(100,100,100,0.48); -fx-border-width: 0;");

    DropShadow dropShadow = new DropShadow();
    dropShadow.setOffsetX(2);
    dropShadow.setOffsetY(2);
    dropShadow.setSpread(0.5);
    dropShadow.setRadius(2);
    dropShadow.setColor(Color.BLACK);

    viewTodayButton.setOnAction(actionEvent -> this.resetDateToToday());
    viewTodayButton.setOnMouseEntered(e -> viewTodayButton.setEffect(dropShadow));
    viewTodayButton.setOnMouseExited(e -> viewTodayButton.setEffect(null));
    viewTodayButton.setOnMouseEntered(e -> viewTodayButton.setStyle(
        "-fx-text-fill: #9e9e9e;-fx-background-color: rgba(163,163,163,0.53); -fx-border-width: 0;"));
    viewTodayButton.setOnMouseExited(e -> viewTodayButton.setStyle(
        "-fx-text-fill: #9e9e9e;-fx-background-color: rgba(100,100,100,0.48); -fx-border-width: 0;"));

    return viewTodayButton;
  }

  /**
   * Creates a timeline which updates every second repeatedly.
   *
   * @param updateEvent the event to call every second
   * @return a timeline which updates every second repeatedly
   */
  private Timeline updateEachSecond(EventHandler<ActionEvent> updateEvent) {
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Animation.INDEFINITE);

    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0), updateEvent));
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));

    return timeline;
  }
}
