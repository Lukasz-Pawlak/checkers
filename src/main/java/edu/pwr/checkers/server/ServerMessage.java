package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing a message from server to client.

 * @version 1.0
 * @author Wojciech Sęk
 */
public class ServerMessage implements Serializable {
  private final String message;
  private Board board = null;
  private Player player = null;
  private String status = null;
  private List<Game> games = null;

  /**
   * Constructor setting just the message.
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
   * Constructor just the message and player.
   * @param message message to be set
   * @param player player to be set
   */
  public ServerMessage(String message, Player player) {
    this.message = message;
    this.player = player;
  }

  /**
   * Initializing new client
   * @param init initial message
   * @param board initial board
   * @param nextPlayer player of this client
   */
    public ServerMessage(String init, Board board, Player nextPlayer) {
      this.message = init;
      this.board = board;
      this.player = nextPlayer;
    }

    public ServerMessage(String init, List<Game> games) {
    this.message = init;
    this.games = games;
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

  public List<Game> getGames() {
    return games;
  }
}
