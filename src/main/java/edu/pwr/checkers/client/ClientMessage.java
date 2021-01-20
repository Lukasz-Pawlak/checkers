package edu.pwr.checkers.client;

import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;

import java.io.Serializable;

/**
 * Class representing a message from client to server.

 * @version 1.0
 * @author Wojciech Sęk
 */
public class ClientMessage implements Serializable {
  private String message;
  private Player player;
  private Piece piece = null;
  private Coordinates coordinates = null;
  private Integer gameNo = null;

  /**
   * Constructor setting just the message.
   * @param message message to be set
   */
  public ClientMessage(String message) {
    this.message = message;
  }

  /**
   * Constructor just the message and player.
   * @param message message to be set
   * @param player player to be set
   */
  public ClientMessage(String message, Player player) {
    this.message = message;
    this.player = player;
  }

  public ClientMessage(int gameNo) {
    this.message = "GIMMEGAME";
    this.gameNo = gameNo;
  }

  public Integer getGameNo() {
    return gameNo;
  }

  /**
   * Constructor just the message, piece, player and coordinates.
   * @param message message to be set
   * @param piece piece to be set
   * @param coordinates coordinates to be set
   * @param player player to be set
   */
  public ClientMessage(String message, Player player, Piece piece, Coordinates coordinates) {
    this.message = message;
    this.player = player;
    this.piece = piece;
    this.coordinates = coordinates;
  }

  /**
   * Method to get the message.
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Method to get the coordinates.
   * @return the coordinates
   */
  public Coordinates getCoordinates() {
    return coordinates;
  }

  /**
   * Method to get the piece.
   * @return the piece
   */
  public Piece getPiece() {
    return piece;
  }

  /**
   * Method to get the player.
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
}