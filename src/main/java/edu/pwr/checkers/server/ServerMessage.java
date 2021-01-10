package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Color;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;

public class ServerMessage implements Serializable {
  private String message = null;
  private Board board = null;
  private Player player = null;
  private Exception exception = null;
  private String status = null;

  public ServerMessage(String message) {
    this.message = message;
  }

  public ServerMessage(String message, Board board) {
    this.message = message;
    this.board = board;
  }
  
  public ServerMessage(String message, String status) {
    this.message = message;
    this.status = status;
  }

  public ServerMessage(String message, Exception exception) {
    this.message = message;
    this.exception = exception;
  }

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

  public String getMessage() {
    return message;
  }

  public String getStatus() {
    return status;
  }

  public Board getBoard() {
    return board;
  }

  public Player getPlayer() {
    return player;
  }
}
