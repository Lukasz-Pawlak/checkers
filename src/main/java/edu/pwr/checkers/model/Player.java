package edu.pwr.checkers.model;

import java.util.List;

public interface Player {
  void currentMove(Piece piece, Coordinates cor, Board board);
  Move getCurrentMove();
  void setCurrentMove(Move move);
  void setPreviousMove(Move move);
  Move getPreviousMove();
  List<Piece> getPieces();
}
