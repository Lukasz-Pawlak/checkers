package edu.pwr.checkers.client;

import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;

public class ClientMessage implements Serializable {
  private String message;
  private Player player;
  private Piece piece = null;
  private Coordinates coordinates = null;

  public ClientMessage(String message) {
    this.message = message;
  }

  public ClientMessage(String message, Player player) {
    this.message = message;
    this.player = player;
  }

  public ClientMessage(String message, Player player, Piece piece, Coordinates coordinates) {
    this.message = message;
    this.player = player;
    this.piece = piece;
    this.coordinates = coordinates;
  }

  public String getMessage() {
    return message;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public Piece getPiece() {
    return piece;
  }

  public Player getPlayer() {
    return player;
  }
}