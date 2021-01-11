package edu.pwr.checkers.server;

import java.io.IOException;

/**
 * Interface providing all methods a server needs.

 * @version 1.0
 * @author Wojciech Sęk
 */
public interface Server {
  /**
   * Mehtod to set up the server basic properties including the clients.
   * @throws IOException used when there are problems with socket connection.
   */
  void setUp() throws IOException;

  /**
   * Used to send the ranking at the end of game.
   */
  void sendRanking();
}