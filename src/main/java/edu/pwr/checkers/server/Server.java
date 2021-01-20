package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Coordinates;

import java.io.IOException;
import java.util.List;

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
   * Method to set up the client we will show the saved game to.
   * @throws IOException used when there are problems with socket connection.
   */
  void displayingSetUp() throws IOException;

  /**
   * Used to send the ranking at the end of game.
   */
  void sendRanking();

  /**
   * Method used for writing set of moves to database.
   * Moves won't be validated.
   * @param moves provides information about consecutive displacements of
   *              one piece. If the piece was moved n times, it should contain
   *              exactly n+1 entries, describing next positions of the piece.
   */
  void saveMoveList(List<Coordinates> moves);
}