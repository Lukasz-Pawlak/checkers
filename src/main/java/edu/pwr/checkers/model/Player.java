package edu.pwr.checkers.model;

import java.util.List;

public interface Player {
  void movePiece(Piece piece, Coordinates cor);
  boolean notMyPiece(Piece piece);
  List<Color> getColors();
}
