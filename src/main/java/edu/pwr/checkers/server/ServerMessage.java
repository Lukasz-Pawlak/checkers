package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;

/**
 * Class representing a message grom server to client.
 */
public class ServerMessage implements Serializable {
  private String message;
  private Board board = null;
  private Player player = null;
  private Exception exception = null;
  private String status = null;

  /**
   * Constructor settting just the message.
   * @param message message to be set
   */
  public ServerMessage(String message) {
    this.message = message;
  }

  /**
   * Constructor just the message and board.
   * @param message message to be set
   * @param board board to be set
   */
  public ServerMessage(String message, Board board) {
    this.message = message;
    this.board = board;
  }

  /**
   * Constructor just the message and status.
   * @param message message to be set
   * @param status status to be set
   */
  public ServerMessage(String message, String status) {
    this.message = message;
    this.status = status;
  }

  /**
   * Constructor just the message and exception.
   * @param message message to be set
   * @param exception exception to be set
   */
  public ServerMessage(String message, Exception exception) {
    this.message = message;
    this.exception = exception;
  }

  /**
   * Constructor just the message and player.
   * @param message message to be set
   * @param player player to be set
   */
  public ServerMessage(String message, Player player) {
    this.message = message;
    this.player = player;
  }

  /**
   * initializing new client
   * @param init
   * @param board
   * @param nextPlayer
   */
    public ServerMessage(String init, Board board, Player nextPlayer) {
      this.message = init;
      this.board = board;
      this.player = nextPlayer;
    }

  /**
   * Method to get the exact message.
   * @return the exact message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Method to get the status.
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Method to get the board.
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Method to get the player.
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
}
