package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;

public class Controller {
  private Player player;
  public Board beginState, lastState, currState;
  private MoveType lastMoveType;

  public void movePiece(Piece piece, Coordinates cor) {
    player.movePiece(piece, cor);
  }

  public void restoreLastState() {
    // TODO: make GUI draw lastState
  }

  public void restartState(Board board) {
    lastState = beginState;
  }

  public void saveState() {
    lastState = currState;
  }

  public void showMessage(String message) {
    // TODO: implementing sending it to GUI
    System.out.println(message);
  }

  public void sendCancelMoveRequest() {

  }

  public void sendAcceptMoveRequest() {

  }
}
