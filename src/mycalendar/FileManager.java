package mycalendar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.filechooser.FileSystemView;
import mycalendar.model.CalendarModel;
import mycalendar.model.CalendarModelImpl;
import mycalendar.model.CalendarReader;

/**
 * Manages the files used by the calendar. Keeps track of the currently open file, opens the
 * calendar file, and saves the existing calendar file. The calendar.mycal are stored in the
 * MyCalendar folder in the Windows Documents folder. This file (and folder) are created if they do
 * not already exist.
 */
public final class FileManager {

  public static CalendarModel OPEN_CALENDAR = new CalendarModelImpl();
  private static String OPEN_FILE_PATH = getFolderPath() + "calendar.mycal";

  /**
   * Opens the calendar.mycal file.
   *
   * @throws IllegalArgumentException if the calendar file is not found or if the calendar itself
   *                                  has invalid data (such as an invalid date)
   * @throws IllegalStateException    if there is an error when creating the calendar.mycal file (if
   *                                  it needs to be created)
   */
  public static void openFile() throws IllegalArgumentException, IllegalStateException {
    try {
      String filePath = getFolderPath() + "calendar.mycal";

      Path path = Path.of(filePath);
      if (!Files.exists(path)) {
        Files.createFile(path);
      }

      try {
        OPEN_CALENDAR = CalendarReader.readCalendarFile(new FileReader(filePath));
        OPEN_FILE_PATH = filePath;
      } catch (FileNotFoundException fnfe) {
        throw new IllegalArgumentException("File not found");
      }

    } catch (IOException ioe) {
      throw new IllegalStateException("File creation failed");
    }
  }

  /**
   * Saves the given calendar as a .mycal file within the MyCalendar folder in the user's My
   * Documents folder. If this folder does not exist, that folder will be created. Files generated
   * by saving the given calendar can be used within the calendar reader to be imported into the
   * program.
   *
   * @throws IllegalStateException if the file writing process fails for any reason
   */
  public static void saveCalendar() throws IllegalStateException {
    try {
      FileWriter writer = new FileWriter(OPEN_FILE_PATH);
      writer.append(OPEN_CALENDAR.calendarInfo());
      writer.close();
    } catch (IOException ioe) {
      throw new IllegalStateException("File write failed");
    }
  }

  /**
   * Gets the path to the MyCalendar folder, which is in the Windows Documents folder. If the
   * MyCalendar folder does not exist in the Windows Documents directory, it is created.
   *
   * @return the path to the MyCalendar folder, which is within the Documents directory
   * @throws IllegalStateException if the directory creation fails
   */
  private static String getFolderPath() throws IllegalStateException {
    String folderPath =
        FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\MyCalendar\\";
    try {
      if (!Files.isDirectory(Paths.get(folderPath))) {
        Files.createDirectory(Paths.get(folderPath));
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Directory creation failed");
    }

    return folderPath;
  }
}
