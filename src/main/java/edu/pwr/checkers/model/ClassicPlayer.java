package edu.pwr.checkers.model;

import java.util.List;

public class ClassicPlayer implements Player {
  private final List<Piece> pieces;
  private Move currentMove;
  private Move previousMove;
  private final Board board;

  public ClassicPlayer(List<Piece> pieces, Board board) {
    this.pieces = pieces;
    this.board = board;
  }

  @Override
  public void currentMove(Piece piece, Coordinates cor, Board board) {
    currentMove = new Move(piece, cor, board);
  }

  @Override
  public Move getPreviousMove() {
    return previousMove;
  }

  @Override
  public Move getCurrentMove() {
    return currentMove;
  }

  @Override
  public void setCurrentMove(Move move) {
    currentMove = move;
  }

  @Override
  public void setPreviousMove(Move move) {
    this.previousMove = move;
  }

  @Override
  public List<Piece> getPieces() {
    return pieces;
  }
}
