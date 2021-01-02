package edu.pwr.checkers.model;

import java.io.Serializable;

/**
 * This enum represents colors which may be used in game.
 * @version 1.0
 * @author Wojciech Sęk
 */
public enum Color implements Serializable {
  /** color red */
  RED,
  /** color green */
  GREEN,
  /** color blue */
  BLUE,
  /** color cyan */
  CYAN,
  /** color magenta */
  MAGENTA,
  /** color yellow */
  YELLOW,
  /** lack of color */
  NOCOLOR;

  /**
   * This function returns the opposite color of this.
   * 'The opposite' is arbitrary decision coded in this function.
   * Useful: getInverse(getInverse(x)) = x, for non-null and
   * non-NOCOLOR x. If x is NOCOLOR null is returned.
   * @return color opposite to this.
   */
  public Color getInverse() {
    switch(this) {
      case RED:
        return CYAN;
      case CYAN:
        return RED;
      case BLUE:
        return YELLOW;
      case YELLOW:
        return BLUE;
      case MAGENTA:
        return GREEN;
      case GREEN:
        return MAGENTA;
      default:
        return null;
    }
  }
}
