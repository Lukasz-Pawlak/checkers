package edu.pwr.checkers.client;

import edu.pwr.checkers.client.view.ClientWindow;
import edu.pwr.checkers.model.*;

public class Controller {
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

  public void refresh() {
    window.refresh();
  }
}
