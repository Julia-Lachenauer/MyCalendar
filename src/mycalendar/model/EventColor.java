package mycalendar.model;

/**
 * Represents the different colors that an event can have when viewed in the calendar.
 */
public enum EventColor {
  Rose(247, 25, 73), Forest(170, 212, 129), Ice(77, 184, 250), Fire(247, 72, 72);

  private final int red;
  private final int green;
  private final int blue;

  /**
   * Constructs a color using the given RGB (red, green, blue) values.
   *
   * @param red   the red value of the color
   * @param green the green value of the color
   * @param blue  the blue value of the color
   * @throws IllegalArgumentException if any of the given color values are less than 0 or greater
   *                                  than 255
   */
  EventColor(int red, int green, int blue) throws IllegalArgumentException {
    if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
      throw new IllegalArgumentException("All color values must range from 0 to 255 inclusive.");
    }
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Returns a formatted string of the RGB values of this color in the form rgb(r, g, b).
   *
   * @return a formatted string representation of the RGB values of this color
   */
  public String rgbString() {
    return String.format("rgb(%d, %d, %d)", this.red, this.green, this.blue);
  }

  /**
   * Gets the RGB red value of this color.
   *
   * @return the red value of this color
   */
  public int getRed() {
    return this.red;
  }

  /**
   * Gets the RGB green value of this color.
   *
   * @return the green value of this color
   */
  public int getGreen() {
    return this.green;
  }

  /**
   * Gets the RGB blue value of this color.
   *
   * @return the blue value of this color
   */
  public int getBlue() {
    return this.blue;
  }
}
