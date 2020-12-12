package edu.pwr.checkers.model;

import java.util.List;

public class Move {
  private MoveType movetype;
  private Piece piece;
  private Coordinates newCor;

  public Move(Piece piece, Coordinates newCor, Board board) {
    Field field = piece.getField();
    Field newField = board.getField(newCor.x, newCor.y);
    List<Field> neighbours = board.getNeighborsOf(field);
    List<Field> furtherNeighbours = board.getFurtherNeighborsOf(field);
    if (neighbours.contains(newField)) {
      movetype = MoveType.ONESTEP;
    } else if (furtherNeighbours.contains(newField)) {
      movetype = MoveType.JUMPSEQ;
    } else {
      movetype = MoveType.UNKNOWN;
    }
    this.piece = piece;
    this.newCor = newCor;
  }

  public MoveType getMovetype() {
    return movetype;
  }

  public Coordinates getNewCor() {
    return newCor;
  }
}
