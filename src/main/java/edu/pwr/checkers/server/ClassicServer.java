package edu.pwr.checkers.server;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.client.ClientMessage;
import edu.pwr.checkers.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * The class representing implementing server interface
 * used in a standard game of Chinese checkers.
 * It is supposed to work with ClassicClients.
 */
public class ClassicServer implements Server {
  private final ServerSocket serverSocket;
  private final Game game;
  public final int numOfPlayers;
  private Map<SocketHandler, Player> map;
  private ArrayList<SocketHandler> handlers;

  /**
   * Constructor that creates server socket and starts a game
   * with a given number of players.
   *
   * @param numOfPlayers number of players to play the game.
   * @throws IOException thrown when there are problems with creating socket.
   * @throws WrongNumberException thrown when numOfPlayers is bad.
   */
  public ClassicServer(int numOfPlayers) throws IOException, WrongNumberException {
    if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4 && numOfPlayers != 6) {
      throw new WrongNumberException();
    }
    this.numOfPlayers = numOfPlayers;
    this.handlers = new ArrayList<>(numOfPlayers);
    this.game = new ClassicGame(numOfPlayers);
    game.setup();
    Logger.info("Trying to start server with port 4444...");
    this.serverSocket = new ServerSocket(4444);
    Logger.info("Started server with 4444...");
  }

  /**
   * The function to set up the server including
   * connecting with clients and creating threads to handle them.
   *
   * @throws NullPointerException thrown when game is null.
   * @throws RejectedExecutionException thrown given problems with threads.
   * @throws IOException problems with connection.
   */
  @Override
  public void setUp() throws NullPointerException, RejectedExecutionException, IOException {
    this.game.init();
    this.game.setServer(this);
    ExecutorService pool = Executors.newFixedThreadPool(numOfPlayers);
    Logger.debug("There are " + numOfPlayers + " players.");

    for (int i = 1; i <= numOfPlayers; i++) {
      Logger.info("Waiting for player " + i);
      synchronized (serverSocket) {
        Socket socket = serverSocket.accept();
        Logger.debug("Przyjąłem pierwsze gniazdo klienta " + i);
        SocketHandler handler =  new SocketHandler(socket);
        // the 2. socket to listen to status
        //handler.sendPlayer(); /// yoo, here streams aren't set up yet dude
        //handler.sendBoard();
        handlers.add(handler);
        pool.execute(handler);
      }
    }
  }

  /**
   * Function to pass a specific message to all the players in the game.
   * @param message message to be sent
   */
  private void sendToAllPlayers(ServerMessage message) {
    for (SocketHandler handler: handlers) {
      try {
        handler.sendMessage(message);
      } catch (IOException e) {
        Logger.err("nie można wysłać ogólnej wiadmości do jednego z graczy");
      }
    }
  }

  /**
   * Function to send the active player to all the players.
   */
  private void setActivePlayerAll() {
    Player activePlayer = game.getActivePlayer();
    Logger.debug(activePlayer.toString()); // tmp
    ServerMessage message = new ServerMessage("SETACTIVE",game.getBoard(), activePlayer);
    sendToAllPlayers(message);
    Logger.info("Server sent a message about active player");
  }

  /**
   * Sets the new board in all clients.
   */
  private void setBoardAll() {
    Player activePlayer = game.getActivePlayer();
    Logger.debug(activePlayer.toString()); // tmp
    ServerMessage message = new ServerMessage("SETBOARD",game.getBoard());
    sendToAllPlayers(message);
    Logger.info("Server sent a message about active player");
  }

  /**
   * Used to send the ranking at the end of game.
   */
  @Override
  public void sendRanking() {
    String ranking  = "";
    int place = 1;
    List<Player> rankingList = game.getRanking();
    for (Player player: rankingList) {
      ranking = ranking + (place++) + " place: " + player.getColors().get(0).toString() + "\n";
    }
    sendToAllPlayers(new ServerMessage("ENDGAME", ranking));
  }


  /**
   * Inner class to handle each client's socket.
   */
  private class SocketHandler implements Runnable {
    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Constructor that sets the socket.
     * @param socket socket to be set.
     */
    public SocketHandler(Socket socket) {
      this.socket = socket;
    }

    /**
     * Main method of the handler, it is used to treat it as a thread
     * in order to execute the tasks simultaneously.
     */
    @Override
    public void run() {
      try {
        Logger.debug("Trying to create inout streams.");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        Logger.debug("output stream correctly connected");
        inputStream = new ObjectInputStream(socket.getInputStream());
        Logger.debug("input stream correctly connected");
        sendGreeting();
        Logger.debug("Próbuję wysłać SETPLAYER do jednego klienta");
        sendMessage(new ServerMessage("SETACTIVE", game.getActivePlayer()));
        Logger.debug("Wysłałem setplayer do jednego klienta");
      } catch (IOException e) {
    // nothing
      }


      while (true) {
        try {
          Logger.debug("Trying read the message from client.");
          ClientMessage message = (ClientMessage) inputStream.readObject();
          Logger.debug("Message is about to be processed.");
          processMessage(message);
          Logger.debug("Message processed properly.");
        } catch (IOException | ClassNotFoundException e) {
          Logger.err("Couldn't read the message.");
          sendAllDisconnected();
          System.exit(1); // TODO: check if client got disconnected here and process it properly
        } catch (IllegalMoveException e) {
          try {
            Logger.info("IllegalMoveException");
            sendIllegalMoveMessage();
          } catch (IOException f) {
            Logger.err("Couldn't send the message.");
          }
        } catch (WrongPlayerException e) {
          try {
            Logger.info("IllegalMoveException");
            sendWrongPlayerMessage();
          } catch (IOException f) {
            Logger.err("Couldn't send the message.");
          }
        }
      }
    }

    /**
     * Used to pass the player at the beginning of the game.
     * @throws IOException
     */
    private synchronized void sendGreeting() throws IOException {
      synchronized (game) {
        Player p = game.nextPlayer();
        Logger.debug(p.toString() + " <- Player");
        ServerMessage message = new ServerMessage("INIT", game.getBoard(), p);
        outputStream.writeObject(message);
      }
    }

    /**
     * Used to send specific message to the client.
     * @param message message to be sent.
     * @throws IOException
     */
    public void sendMessage(ServerMessage message) throws IOException {
      outputStream.reset();
      outputStream.writeObject(message);
    }

    /**
     * Method to send a message about a disconnected client to all.
     */
    public void sendAllDisconnected() {
      ServerMessage message = new ServerMessage("DISCONNECTED");
      sendToAllPlayers(message);
    }

    /**
     * It is used to process the message received from the server depending on
     * the communicate.
     * @param message message to be processed.
     * @throws IllegalMoveException
     * @throws WrongPlayerException
     * @throws IOException
     */
    private synchronized void processMessage(ClientMessage message) throws IllegalMoveException, WrongPlayerException, IOException {
      String messageType = message.getMessage();
      Player player = message.getPlayer();
      Coordinates coordinates = message.getCoordinates();
      Piece piece = message.getPiece();
      synchronized (game) {
        switch (messageType) {
          case "MOVEREQUEST": {
            Logger.info("Received move request.");
            game.move(player, piece, coordinates);
            sendAcceptedMoveMessage();
            Logger.info("Sent move accepted request.");
            break;
          }
          case "CANCELMOVE": {
            Logger.info("Received cancel move message.");
            game.cancelMove(player);
            sendCanceledMoveMessage();
            Logger.info("Sent cancelled move message.");
            break;
          }
          case "ACCEPTMOVE": {
            Logger.info("Received accept move message.");
            game.acceptMove(player);
            sendMoveAcceptedMessage();
            Logger.info("Sent accepted move message.");
            break;
          }
        }
      }
    }

    /**
     * Method to send information about illegal move.
     * @throws IOException
     */
    private void sendIllegalMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("ILLEGALMOVE", game.getBoard());
      sendMessage(message);
      setBoardAll();
    }

    /**
     * Method to send information that the player doing
     * the action is not supposed to do it.
     * @throws IOException
     */
    private void sendWrongPlayerMessage() throws IOException {
      ServerMessage message = new ServerMessage("WRONGPLAYER", game.getBoard());
      sendMessage(message);
      setBoardAll();
    }

    /**
     * Send when the move was accepted by the game, it contains the board.
     * @throws IOException
     */
    private void sendAcceptedMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("VALIDMOVE", game.getBoard());
      sendMessage(message);
      setBoardAll();
      setBoardAll();
    }

    /**
     * Send when client has cancelled his or her move, it contains the board.
     * @throws IOException
     */
    private void sendCanceledMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("CANCELLEDMOVE", game.getBoard());
      sendMessage(message);
      setBoardAll();
    }

    /**
     * Message to send when the player confirmed his or her move,
     * sends the new active player to all.
     * @throws IOException
     */
    private void sendMoveAcceptedMessage() throws IOException {
      ServerMessage message = new ServerMessage("MOVEACCEPTED");
      sendMessage(message);
      setActivePlayerAll();
    }
  }

  /**
   * Function to be run when we start the programme.
   * @param args
   */
  public static void main(String[] args) {
    Server server;

    if (args.length != 1) {
      Logger.err("Give only the number of players!");
      return;
    }

    try {
      int numOfPlayers = Integer.parseInt(args[0]);
      server = new ClassicServer(numOfPlayers);
      server.setUp();
      Logger.info("Server running...");
    } catch(NumberFormatException e) {
      Logger.err("Give an integer!");
    } catch(WrongNumberException e) {
      Logger.err("The number can be: 2,3,4 or 6");
    } catch(IOException e) {
      Logger.err("Couldn't set up the server.");
    } catch(NullPointerException e) {
      Logger.err("Server is null.");
      e.printStackTrace();
    } catch(RejectedExecutionException e) {
      Logger.err("Execution rejected.");
    }
  }
}