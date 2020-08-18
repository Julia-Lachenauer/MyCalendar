package mycalendar.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mycalendar.controller.Features;
import mycalendar.model.CalendarMath;
import mycalendar.model.Event;
import mycalendar.model.dateandtime.Date;
import mycalendar.model.dateandtime.DayOfWeek;
import mycalendar.model.dateandtime.Time;

/**
 * Represents an interactive view for the calendar allowing the user to interact with the calendar
 * and add, edit, and delete events.
 */
public class CalendarViewBase extends BorderPane implements CalendarView {

  private List<Event> events;
  private Features features;
  private final Date currentViewDate;
  private final WindowControls windowControls;

  private final ScrollPane weekScrollPane;

  /**
   * Creates an instance of the calendar view.
   */
  public CalendarViewBase() {
    super();
    this.events = new ArrayList<>();
    this.windowControls = new WindowControls(0, 0, this.getWidth(), this.getHeight(), false);
    this.currentViewDate = CalendarMath.getCurrentDate();

    this.weekScrollPane = new ScrollPane();
    this.weekScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    this.weekScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    WeekControls weekControls = new WeekControls(this.currentViewDate,
        event -> this.features.addEvent(event), this::refresh);

    LiveClock liveClock = new LiveClock(this::refresh, this.currentViewDate);

    StackPane topBar = new TopBar(weekControls, liveClock, this.windowControls);
    topBar.setOnMouseClicked(e -> {
      if (e.getClickCount() == 2) {
        this.windowControls.toggleMaximize();
      }
    });

    this.setTop(topBar);
    this.updateWeekView();
  }

  @Override
  public void maximize() {
    this.windowControls.maximizeWindow();
  }

  @Override
  public void setEvents(List<Event> events) {
    this.events = events;
  }

  @Override
  public void setFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void setScene(Scene scene) {
    this.windowControls.setScene(scene);
  }

  @Override
  public void refresh() {
    this.updateWeekView();
  }

  /**
   * Gets all the events on the given date.
   *
   * @param date the date to check for events
   * @return a list of every event that takes place on the given date
   */
  private List<Event> getEventsOnDate(Date date) {
    List<Event> eventsOnDate = new ArrayList<>();
    for (Event event : this.events) {
      if (event.getDate().equals(date)) {
        eventsOnDate.add(event);
      }
    }

    return eventsOnDate;
  }

  /**
   * Updates the overall week view (the scrollable content of the calendar).
   */
  private void updateWeekView() {
    // Store the scroll value
    double scrollValue = this.weekScrollPane.getVvalue();

    // Pair time markers with the week columns and set that as the scroll content
    HBox weekView = new HBox();
    weekView.getChildren().addAll(new TimeMarks(), this.updateDayCols());
    this.weekScrollPane.setContent(weekView);

    // Wrap the scroll pane in a border pane and add the day labels to the top
    BorderPane weekScreen = new BorderPane();
    weekScreen.setCenter(this.weekScrollPane);
    weekScreen.setTop(this.updateDayLabels());

    // Set the scroll value to the previous scroll value
    this.weekScrollPane.setVvalue(scrollValue);

    // Set the border pane as the center of this calendar view
    this.setCenter(weekScreen);
  }

  /**
   * Updates each of the seven day labels (the labels showing the day of the week and date of the
   * days in the currently displayed week).
   *
   * @return a box containing the updated day labels
   */
  private HBox updateDayLabels() {
    HBox dayOfWeekCols = new HBox();
    dayOfWeekCols.setPrefHeight(50);
    dayOfWeekCols.setStyle("-fx-background-color: #2f2f2f;-fx-padding: 0 0 0 36");

    for (int i = 0; i < 7; i++) {
      Date sundayOfWeek = CalendarMath.getSundayOfWeek(this.currentViewDate);
      Date dateOfColumn = CalendarMath.goForwardDays(sundayOfWeek, i);

      // Code for setting up the label row on top of the day column
      BorderPane dayLabelBox = new BorderPane();
      dayLabelBox.prefWidthProperty().bind(this.widthProperty().divide(7.3));

      if (dateOfColumn.equals(CalendarMath.getCurrentDate())) {
        dayLabelBox.setStyle(
            "-fx-background-color: #3b3b3b; -fx-border-width: 0 1 0 1; -fx-border-color: #1b1b1b");
      } else {
        dayLabelBox.setStyle("-fx-border-width: 0 1 0 1; -fx-border-color: #1b1b1b");
      }

      Label dayLabel = new Label(
          DayOfWeek.getDayOfWeek(i + 1).toString() + "\n" + dateOfColumn.getDay());
      dayLabel.setFont(Font.font("Century Gothic", 18));
      dayLabel.setAlignment(Pos.CENTER);
      dayLabel.setTextAlignment(TextAlignment.CENTER);
      dayLabel.setStyle("-fx-text-fill: #c2c2c2");

      dayLabelBox.setCenter(dayLabel);

      dayOfWeekCols.getChildren().add(dayLabelBox);
    }

    return dayOfWeekCols;
  }

  /**
   * Updates the seven day columns to represent the currently displayed date and to show all the
   * events taking place in the displayed week.
   *
   * @return a box containing the updated day columns
   */
  private HBox updateDayCols() {
    HBox dayCols = new HBox();

    for (int i = 0; i < 7; i++) {
      // Code for setting up the day column
      GridPane dayColumn = new DayColumn();
      dayColumn.prefWidthProperty().bind(this.widthProperty().divide(7.3));
      dayColumn.prefHeightProperty().bind(this.heightProperty().multiply(1.6));

      Date sundayOfWeek = CalendarMath.getSundayOfWeek(this.currentViewDate);
      Date dateOfColumn = CalendarMath.goForwardDays(sundayOfWeek, i);

      if (dateOfColumn.equals(CalendarMath.getCurrentDate())) {
        dayColumn.setStyle(
            "-fx-background-color: #4a4a4a;-fx-border-color: #4d4259;-fx-border-width: 1.4;");
      }

      StackPane eventOverlay = this.eventOverlay(dateOfColumn, dayColumn);
      dayCols.getChildren().add(eventOverlay);
    }

    return dayCols;
  }

  /**
   * Creates an overlay for the day columns containing all the events on the given date.
   *
   * @param dateOfColumn the date of the column to overlay
   * @param dayColumn    the day column itself to receive the event overlay
   * @return a column to be put on top of the given date column to show the events taking place on
   * that date
   */
  private StackPane eventOverlay(Date dateOfColumn, GridPane dayColumn) {
    GridPane minuteGridPane = new GridPane();
    minuteGridPane.prefWidthProperty().bind(this.widthProperty().divide(7.3));
    minuteGridPane.prefHeightProperty().bind(dayColumn.heightProperty());
    minuteGridPane.setStyle("-fx-background-color: transparent;");

    GridPane currentTimeGridPane = new GridPane();
    currentTimeGridPane.prefWidthProperty().bind(this.widthProperty().divide(7.3));
    currentTimeGridPane.prefHeightProperty().bind(this.heightProperty().multiply(1.6));
    currentTimeGridPane.setStyle("-fx-background-color: transparent;");

    Date today = CalendarMath.getCurrentDate();

    for (int j = 0; j < 288; j++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setPercentHeight(100.0 / 288);

      minuteGridPane.getRowConstraints().add(rowConstraints);
      currentTimeGridPane.getRowConstraints().add(rowConstraints);
    }

    StackPane dayColWithEvents = new StackPane();

    for (int j = 0; j < 288; j++) {
      HBox currentTimePane = new HBox();
      currentTimePane.prefWidthProperty().bind(dayColumn.widthProperty());

      int currentTimeIncrements = CalendarMath
          .num5MinIncrementsFromMidnight(CalendarMath.getCurrentTime());

      if (dateOfColumn.equals(today) && currentTimeIncrements == j) {
        currentTimePane
            .setStyle("-fx-border-color: rgba(224,224,224,0.6);-fx-border-width: 2 0 0 0;");
      } else {
        currentTimePane.setStyle("-fx-border-width: 0;");
      }

      currentTimeGridPane.add(currentTimePane, 0, j);

      if (j % 3 == 0) {
        HBox pane = new HBox();
        pane.prefWidthProperty().bind(dayColumn.widthProperty());

        if (j % 4 != 1) {
          pane.setStyle("-fx-border-color: dimgray;-fx-border-width: 0 0 0.8 0;");
        } else {
          pane.setStyle("-fx-border-width: 0;");
        }

        GridPane.setRowSpan(pane, 3);
        minuteGridPane.add(pane, 0, j);
      }
    }

    for (Event event : this.getEventsOnDate(dateOfColumn)) {
      int startMinuteOfDay = CalendarMath.num5MinIncrementsFromMidnight(event.getStartTime());

      HBox eventPane = this.eventPane(dayColumn, event);
      minuteGridPane.add(eventPane, 0, startMinuteOfDay);

    }
    dayColWithEvents.getChildren().add(dayColumn);
    dayColWithEvents.getChildren().add(minuteGridPane);

    if (dateOfColumn.equals(today)) {
      currentTimeGridPane.setMouseTransparent(true);
      dayColWithEvents.getChildren().add(currentTimeGridPane);
    }

    return dayColWithEvents;
  }

  /**
   * Creates a box representing an event which contains the event title, start time, and end time.
   * The box is sized to proportionally match the duration of the event, and if there is room, the
   * event description is shown as well. If the box is moused over, a tooltip will appear showing
   * the event's name, start time, end time, and description. The box background color is set to the
   * color of the event.
   *
   * @param dayColumn the day column this event pane will be put on top of
   * @param event     the event to be represented and displayed
   * @return a box representing the given event
   */
  private HBox eventPane(GridPane dayColumn, Event event) {
    Time startTime = event.getStartTime();
    Time endTime = event.getEndTime();

    int duration = CalendarMath.num5MinuteIncrementsBetween(startTime, endTime);

    HBox eventPane = new HBox();

    eventPane.maxHeightProperty().bind(dayColumn.heightProperty());
    eventPane.prefWidthProperty().bind(dayColumn.widthProperty());

    String colorString = event.getColor().rgbString();

    eventPane.setStyle("-fx-background-color: " + colorString
        + ";-fx-border-color: floralwhite;-fx-border-width: 2");

    String startTimeString = event.getStartTime().simpleFormat();
    String endTimeString = event.getEndTime().simpleFormat();
    String timeString = String.format("%s - %s", startTimeString, endTimeString);
    String eventInfo;

    Label eventLabel = new Label();
    eventLabel.setFont(Font.font("Century Gothic", 10));
    eventLabel.setPadding(new Insets(0, 0, 0, 6));

    Label titleLabel = new Label(event.getTitle());
    titleLabel.setPadding(new Insets(0, 0, 0, 6));
    titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 10));

    if (duration > 9) {
      VBox eventLabelBox = new VBox();
      eventInfo = timeString + "\n" + event.getDescription();
      eventLabel.setText(eventInfo);

      eventLabelBox.getChildren().addAll(titleLabel, eventLabel);
      eventPane.getChildren().add(eventLabelBox);
    } else {
      HBox eventLabelBox = new HBox();
      eventInfo = "\t" + timeString;
      eventLabel.setText(eventInfo);

      eventLabelBox.getChildren().addAll(titleLabel, eventLabel);
      eventPane.getChildren().add(eventLabelBox);
    }

    eventPane.setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.PRIMARY) {
        Stage editEventStage = new Stage();
        VBox eventEditor = new EditEventPopup(event, () -> this.features.updateCalendar(),
            eventToDelete -> this.features.deleteEvent(eventToDelete));
        Scene editEventScene = new Scene(eventEditor, 350, 350);
        editEventStage.setScene(editEventScene);
        editEventStage.initOwner(this.getScene().getWindow());
        editEventStage.initModality(Modality.WINDOW_MODAL);
        editEventStage.setTitle("Edit Event");
        editEventStage.show();
        eventEditor.requestFocus();
      }
    });

    String toolTipString = String.format("%s\n%s\n%s\n%s", event.getTitle(),
        event.getDate().formattedDate(), timeString, event.getDescription());

    Tooltip eventToolTip = new Tooltip(toolTipString);
    eventToolTip.setShowDelay(new Duration(200));
    Tooltip.install(eventPane, eventToolTip);

    if (duration < 2) {
      duration = 2;
    }

    GridPane.setRowSpan(eventPane, duration);

    return eventPane;
  }
}
