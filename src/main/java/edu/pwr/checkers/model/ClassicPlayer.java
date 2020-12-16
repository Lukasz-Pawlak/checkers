package edu.pwr.checkers.model;

import edu.pwr.checkers.client.Controller;

import java.util.List;

public class ClassicPlayer implements Player {
  private final List<Color> colors;
  Controller controller;

  public ClassicPlayer(List<Color> colors) {
    this.colors = colors;
  }

  @Override
  public boolean notMyPiece(Piece piece) {
    return !colors.contains(piece.getColor());
  }

  @Override
  public List<Color> getColors() {
    return colors;
  }
}
