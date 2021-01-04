package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;

public class ServerMessage implements Serializable {
  private String message = null;
  private Board board = null;
  private Player player = null;

  public ServerMessage(String message) {
    this.message = message;
  }

  public ServerMessage(String message, Board board) {
    this.message = message;
    this.board = board;
  }

  public ServerMessage(String message, Player player) {
    this.message = message;
    this.player = player;
  }


  public String getMessage() {
    return message;
  }

  public Board getBoard() {
    return board;
  }

  public Player getPlayer() {
    return player;
  }
}
