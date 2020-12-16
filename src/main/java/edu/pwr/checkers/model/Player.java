package edu.pwr.checkers.model;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {
  void movePiece(Piece piece, Coordinates cor);
  boolean notMyPiece(Piece piece);
  List<Color> getColors();
}
