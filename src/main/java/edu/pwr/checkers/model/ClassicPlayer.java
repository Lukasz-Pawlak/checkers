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

  @Override
  public Board getLastState() {
    return controller.lastState;
  }

  @Override
  public Board getBeginState() {
    return controller.beginState;
  }

  @Override
  public void setCurrState(Board board) {
    controller.currState = board;
  }

  @Override
  public void setLastState(Board board) {
    controller.lastState = board;
  }

  @Override
  public void setBeginState(Board board) {
    controller.beginState = board;
  }
}
