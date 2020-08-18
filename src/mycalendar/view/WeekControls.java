package mycalendar.view;

import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mycalendar.model.CalendarMath;
import mycalendar.model.Event;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.MonthName;

/**
 * Represents controls for managing which week is shown in the calendar. Allows the user to go
 * forward a week, go backward a week, or choose the week to be displayed.
 */
public class WeekControls extends HBox {

  private final Date currentViewDate;
  private final Button currentViewButton;
  private final Runnable refresh;

  /**
   * Creates an instance of the week controls.
   *
   * @param currentViewDate the date currently being viewed in the calendar
   * @param addEventPopup   an operation which takes in an event and adds it to the calendar
   * @param refresh         an operation which refreshes the calendar
   */
  public WeekControls(Date currentViewDate, Consumer<Event> addEventPopup, Runnable refresh) {
    this.currentViewDate = currentViewDate;
    this.refresh = refresh;

    this.currentViewButton = new Button();
    this.currentViewButton
        .setStyle("-fx-text-fill: #9e9e9e;-fx-background-color: transparent; -fx-border-width: 0;");

    this.currentViewButton
        .setOnMouseEntered(e -> this.currentViewButton.setStyle(
            "-fx-text-fill: #9e9e9e;-fx-background-color: rgba(112,112,112,0.55); -fx-border-width: 0;"));
    this.currentViewButton.setOnMouseExited(
        e -> this.currentViewButton.setStyle(
            "-fx-text-fill: #9e9e9e;-fx-background-color: transparent; -fx-border-width: 0;"));

    this.currentViewButton.setMinWidth(140);
    this.currentViewButton.setOnAction(e -> {
      Stage calendarPickerStage = new Stage();
      calendarPickerStage.initStyle(StageStyle.UTILITY);

      DatePicker validDatePicker = new ValidDatePicker();

      validDatePicker.setOnAction(event -> {
        Date chosenDate = CalendarMath.localDateToModelDate(validDatePicker.getValue());
        this.currentViewDate.setDate(chosenDate);
        this.refresh.run();
        this.updateWeekViewLabel();
        calendarPickerStage.close();
      });

      DatePickerSkin validDatePickerSkin = new DatePickerSkin(validDatePicker);
      validDatePickerSkin.show();
      Node validDatePickerContent = validDatePickerSkin.getPopupContent();

      Pane validDatePickerPane = new Pane();
      validDatePickerPane.getChildren().add(validDatePickerContent);

      Scene calendarPickerScene = new Scene(validDatePickerPane, 220, 220);
      calendarPickerStage.setScene(calendarPickerScene);
      calendarPickerStage.initOwner(this.getScene().getWindow());
      calendarPickerStage.initModality(Modality.WINDOW_MODAL);
      calendarPickerStage.setTitle("View Date");
      calendarPickerStage.show();
    });

    this.currentViewButton.setFont(Font.font("Century Gothic", 22));
    this.currentViewButton.setTextAlignment(TextAlignment.CENTER);
    this.currentViewButton.setAlignment(Pos.CENTER);

    this.updateWeekViewLabel();

    HBox currentViewBox = new HBox();
    currentViewBox.setAlignment(Pos.CENTER);
    currentViewBox.getChildren().add(this.currentViewButton);

    int textWidth = 120;
    currentViewBox.setMinWidth(textWidth);
    currentViewBox.setMaxWidth(textWidth);
    currentViewBox.setPrefWidth(textWidth);

    this.setMaxWidth(220);
    this.getChildren().add(this.leftRightButton(true));
    this.getChildren().add(currentViewBox);
    this.getChildren().add(this.leftRightButton(false));

    this.getChildren().add(this.addEventButton(addEventPopup));
  }

  /**
   * Creates a button which allows the user to add an event by creating a popup with the necessary
   * fields.
   *
   * @param addEventPopup an operation which takes in an event and adds it to the calendar
   * @return a button allowing the user to add an event to the calendar
   */
  private Button addEventButton(Consumer<Event> addEventPopup) {
    Button addEventButton = new Button();
    addEventButton.setMinWidth(240);
    addEventButton.setFont(Font.font("Century Gothic", 30));
    addEventButton.setScaleX(0.8);
    addEventButton.setScaleY(0.8);
    addEventButton
        .setStyle("-fx-text-fill: #9e9e9e;-fx-background-color: transparent;-fx-border-width: 0;");

    addEventButton
        .setOnMouseEntered(e -> addEventButton
            .setStyle("-fx-text-fill: #9e9e9e;-fx-background-color: df8f80;-fx-border-width: 0;"));
    addEventButton
        .setOnMouseExited(e -> addEventButton.setStyle(
            "-fx-text-fill: #9e9e9e;-fx-background-color: transparent;-fx-border-width: 0;"));

    addEventButton.setTextAlignment(TextAlignment.CENTER);
    addEventButton.setAlignment(Pos.CENTER);
    addEventButton.setOnAction(e -> {
      Stage addEventStage = new Stage();
      VBox eventAdder = new AddEventPopup(addEventPopup);
      Scene addEventScene = new Scene(eventAdder, 350, 350);
      addEventStage.setScene(addEventScene);
      addEventStage.initOwner(this.getScene().getWindow());
      addEventStage.initModality(Modality.WINDOW_MODAL);
      addEventStage.setTitle("Add Event");
      addEventStage.show();
      eventAdder.requestFocus();

    });

    addEventButton.setText("Add event");

    return addEventButton;
  }

  /**
   * Creates a button allowing the user to increment the currently viewed date either one week
   * forward or one week backward.
   *
   * @param left true if the button should send the user one week backward, false if the button
   *             should send the user one week forward
   * @return a button allowing the user to increment the currently viewed date either one week
   * forward or one week backward
   */
  private Button leftRightButton(boolean left) {
    Button button = new Button();
    DropShadow dropShadow = new DropShadow();

    if (left) {
      button.setText("<");
      button.setOnAction(actionEvent -> this.incrementWeek(true));
      dropShadow.setOffsetX(3);
    } else {
      button.setText(">");
      dropShadow.setOffsetX(-3);
      button.setOnAction(actionEvent -> this.incrementWeek(false));
    }

    button.setFont(Font.font("Century Gothic", 30));
    button
        .setStyle("-fx-text-fill: #9e9e9e;-fx-background-color: transparent;-fx-border-width: 0;");
    button.setTextAlignment(TextAlignment.CENTER);
    button.setAlignment(Pos.CENTER);

    dropShadow.setOffsetY(3);
    dropShadow.setSpread(0.6);
    dropShadow.setRadius(1);
    dropShadow.setColor(Color.BLACK);
    button.setOnMouseEntered(e -> button.setEffect(dropShadow));
    button.setOnMouseExited(e -> button.setEffect(null));
    button.setOnMousePressed(e -> {
      button.setScaleX(0.8);
      button.setScaleY(0.8);
    });
    button.setOnMouseReleased(e -> {
      button.setScaleX(1);
      button.setScaleY(1);
    });

    return button;
  }

  /**
   * Increments the currently viewed date either one week forward or one week backward.
   *
   * @param backward true if the date should be incremented one week backward, false if the date
   *                 should be incremented one week forwardS
   */
  private void incrementWeek(boolean backward) {
    Date incrementedWeek;

    if (backward) {
      incrementedWeek = CalendarMath.goBackOneWeek(this.currentViewDate);
    } else {
      incrementedWeek = CalendarMath.goForwardOneWeek(this.currentViewDate);
    }

    this.currentViewDate.setDate(incrementedWeek);

    this.refresh.run();
    this.updateWeekViewLabel();
  }

  /**
   * Updates the label showing the currently viewed month and year to reflect the date currently
   * being viewed in the calendar.
   */
  private void updateWeekViewLabel() {
    String month = MonthName.getMonthName(this.currentViewDate.getMonth()).substring(0, 3);
    int year = this.currentViewDate.getYear();

    this.currentViewButton.setText(String.format("%s %d", month, year));
  }
}
