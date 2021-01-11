package edu.pwr.checkers.server;

import edu.pwr.checkers.model.ClassicGame;
import edu.pwr.checkers.model.Color;
import edu.pwr.checkers.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Server {
  /**
   * Mehtod to set up the server basic properites including the clients.
   * @throws IOException
   */
  void setUp() throws IOException;

  /**
   * Used to send the ranking at the end of game.
   */
  void sendRanking();
}