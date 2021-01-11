package edu.pwr.checkers.client;

import edu.pwr.checkers.client.view.Canvas;
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
  /** reference to the mediator. */
  private final Mediator mediator;

  public void startGame() {
    window.getCanvas().init();
  }

  /**
   * The only constructor.
   * @param mediator reference to the mediator.
   */
  public Controller(Mediator mediator) {
    this.mediator = mediator;
    window = new ClientWindow(this);
  }

  /**
   * Board getter.
   * @return this.board
   */
  public Board getBoard(){
    return mediator.getBoard();
  }

  /**
   * Board setter.
   * @param board board to be set.
   */
  public void setBoard(Board board) {
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

  /**
   * Used to refresh the board.
   */
  public void refresh() {
    window.refresh();
  }

  public void lockButtons() {
    window.lockButtons();
  }

  public void unlockButtons() {
    window.unlockButtons();
  }
}
