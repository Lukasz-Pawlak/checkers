package edu.pwr.checkers.client;

import edu.pwr.checkers.client.view.ClientWindow;
import edu.pwr.checkers.model.*;

public class Controller {
  public Board beginState, lastState, currState; // ig we need a way to clone existing board
  private MoveType lastMoveType;
  private final ClientWindow window;
  private Board board;
  private Mediator mediator;

  public Controller(Mediator mediator) {
    this.mediator = mediator;
    this.board = mediator.getBoard();
    window = new ClientWindow(this);
  }

  public Board getBoard(){
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
    window.useThisBoard(board);
  }

  public boolean movePiece(Piece piece, Coordinates cor) {
    return mediator.movePiece(piece, cor);
  }

  public void restoreLastState() {
    // TODO: make GUI draw lastState
  }

  public void restartState(Board board) {
    lastState = beginState;
  }

  public void saveState() { // that's not gonna work dude, we need to clone it or smth
    lastState = currState;
  }

  public void showMessage(String message) {
    window.setMessage(message);
  }

  public void setStatus(String status) {
    window.setStatus(status);
  }

  public void sendCancelMoveRequest() {
    mediator.sendCancelMoveRequest();
  }

  public void sendAcceptMoveRequest() {
    mediator.sendAcceptMoveRequest();
  }
}
