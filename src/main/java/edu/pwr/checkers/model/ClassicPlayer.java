package edu.pwr.checkers.model;

import java.util.List;

/**
 * This class represents player in classic version of
 * the game of Chinese Checkers.
 * @version 1.0
 * @author Wojciech Sęk
 */
public class ClassicPlayer implements Player {
  /**
   * A list of colors this player owns. In classic game,
   * this list has only one element.
   */
  private final List<Color> colors;

  /**
   * The only constructor.
   * @param colors list of colors this player will own.
   */
  public ClassicPlayer(List<Color> colors) {
    this.colors = colors;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean notMyPiece(Piece piece) {
    return !colors.contains(piece.getColor());
  }

  /**
   * @inheritDoc
   */
  @Override
  public List<Color> getColors() {
    return colors;
  }
}
