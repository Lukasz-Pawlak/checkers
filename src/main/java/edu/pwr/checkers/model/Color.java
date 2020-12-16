package edu.pwr.checkers.model;

import java.io.Serializable;

public enum Color implements Serializable {
  RED,
  GREEN,
  BLUE,
  CYAN,
  MAGENTA,
  YELLOW,
  NOCOLOR;

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
