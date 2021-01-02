package edu.pwr.checkers.model;

import java.io.Serializable;
import java.util.List;

/**
 * This interface represents player.
 * @version 1.0
 * @author Wojciech Sęk
 */
public interface Player extends Serializable {
  /**
   * This method should return true iff the piece does
   * not belonge to this player.
   * @param piece Piece object to be tested
   * @return whether the piece belongs to this player.
   */
  boolean notMyPiece(Piece piece);

  /**
   * This method returns a list if colors which this
   * player owns.
   * @return the list of colors this player owns.
   */
  List<Color> getColors();
}
