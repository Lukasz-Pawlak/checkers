package edu.pwr.checkers.client;

import edu.pwr.checkers.client.view.ClientWindow;
import edu.pwr.checkers.model.*;

/**
 * This class is used to lover the coupling between GUI and model.
 * It communicates with Mediator.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class Controller {
  /** Reference to the window in which game takes place. */
  private final ClientWindow window;
  /** Currently used board. */ //TODO: is it even needed here? Could be only passed when needed. Not changing it now, because maybe test use it in weird way.
  private Board board;
  /** reference to the mediator. */
  private final Mediator mediator;

  /**
   * The only constructor.
   * @param mediator reference to the mediator.
   */
  public Controller(Mediator mediator) {
    this.mediator = mediator;
    this.board = mediator.getBoard();
    window = new ClientWindow(this);
  }

  /**
   * Board getter.
   * @return this.board
   */
  public Board getBoard(){
    // /*like this*/ return mediator.getBoard();
    return board;
  }

  /**
   * Board setter.
   * @param board board to be set.
   */
  public void setBoard(Board board) {
    this.board = board; // and this is redundant
    window.useThisBoard(board);
  }

  /**
   * Method passing information from GUI to mediator about partial move
   * that player wants to perform.
   * @param piece piece to be moved.
   * @param cor coordinates to which piece shall be moved.
   * @return whether server acknowledged move and permitted it.
   */
  public boolean movePiece(Piece piece, Coordinates cor) {
    return mediator.movePiece(piece, cor);
  }

  /**
   * This method shows server message on the window.
   * @param message message to be shown.
   */
  public void showMessage(String message) {
    window.setMessage(message);
  }

  /**
   * This method shows status of the game on the window.
   * @param status status to be set.
   */
  public void setStatus(String status) {
    window.setStatus(status);
  }

  /**
   * This method sends cancel move request to the server.
   */
  public void sendCancelMoveRequest() {
    mediator.sendCancelMoveRequest();
  }

  /**
   * This method sends accept move request to the server.
   */
  public void sendAcceptMoveRequest() {
    mediator.sendAcceptMoveRequest();
  }
}
