package edu.pwr.checkers.model;

import java.util.List;

public interface Player {
  void movePiece(Piece piece, Coordinates cor);
  boolean notMyPiece(Piece piece);
  List<Piece> getPieces();
  Board getCurrState();
  Board getLastState();
  Board getBeginState();
  void setCurrState(Board board);
  void setLastState(Board board);
  void setBeginState(Board board);
}
