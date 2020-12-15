package edu.pwr.checkers.model;

import edu.pwr.checkers.client.Controller;

import java.util.List;

public class ClassicPlayer implements Player {
  private final List<Piece> pieces;
  Controller controller;

  public ClassicPlayer(List<Piece> pieces) {
    this.pieces = pieces;
  }


  @Override
  public void movePiece(Piece piece, Coordinates cor) {
    if (notMyPiece(piece)) {
      // TODO: send msg to controller
    } else {
      //client.sendMoveRequest(this, piece, cor);
    }
  }

  @Override
  public boolean notMyPiece(Piece piece) {
    return !pieces.contains(piece);
  }

  @Override
  public List<Piece> getPieces() {
    return pieces;
  }

  @Override
  public Board getCurrState() {
    return controller.currState;
  }
}
