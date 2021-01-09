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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 *
 */
public class ClassicServer implements Server {
  private final ServerSocket serverSocket;
  private final Game game;
  public final int numOfPlayers;
  private Map<SocketHandler, Player> map;

  public ClassicServer(int numOfPlayers) throws IOException, WrongNumberException {
    if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4 && numOfPlayers != 6) {
      throw new WrongNumberException();
    }
    this.numOfPlayers = numOfPlayers;
    this.game = new ClassicGame(numOfPlayers);
    game.setup();
    Logger.info("Trying to start server with port 4444...");
    this.serverSocket = new ServerSocket(4444);
    Logger.info("Started server with 4444...");
  }

  @Override
  public void setUp() throws NullPointerException, RejectedExecutionException, IOException {
    this.game.init();
    ExecutorService pool = Executors.newFixedThreadPool(numOfPlayers);
    Logger.debug("There are " + numOfPlayers + " players.");

    for (int i = 1; i <= numOfPlayers; i++) {
      Logger.info("Waiting for player " + i);
      synchronized (serverSocket) {
        Logger.debug("Przyjmuję pierwsze gniazdo klienta " + i);
        Socket socket = serverSocket.accept();
        Logger.debug("Przyjąłem pierwsze gniazdo klienta " + i);
        SocketHandler handler =  new SocketHandler(socket);
        // the 2. socket to listen to status
        Logger.debug("Przyjmuję drugie gniazdo klienta " + i);
        //handler.sendPlayer(); /// yoo, here streams aren't set up yet dude
        //handler.sendBoard();
        pool.execute(handler);
      }
    }
  }

  private class SocketHandler implements Runnable {
    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public SocketHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        Logger.debug("Trying to create inout streams.");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        Logger.debug("output stream correctly connected");
        inputStream = new ObjectInputStream(socket.getInputStream());
        Logger.debug("input stream correctly connected");
        sendGreeting();
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
          System.exit(1); // TODO: check if client got disconnected here and process it properly
        } catch (IllegalMoveException e) {
          try {
            sendIllegalMoveMessage();
          } catch (IOException f) {
            Logger.err("Couldn't send the message.");
          }
        } catch (WrongPlayerException e) {
          try {
            sendWrongPlayerMessage();
          } catch (IOException f) {
            Logger.err("Couldn't send the message.");
          }
        }
      }
    }

    /**
     * used only once
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
          }
          case "CANCELMOVE": {
            Logger.info("Received cancel move message.");
            game.cancelMove(player);
            sendCanceledMoveMessage();
            Logger.info("Sent cancelled move message.");
          }
          case "ACCEPTMOVE": {
            Logger.info("Received accept move message.");
            game.acceptMove(player);
            sendMoveAcceptedMessage();
            Logger.info("Sent accepted move message.");
          }
        }
      }
    }

    private void sendIllegalMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("ILLEGALMOVE");
      outputStream.writeObject(message);
    }

    private void sendWrongPlayerMessage() throws IOException {
      ServerMessage message = new ServerMessage("WRONGPLAYER");
      outputStream.writeObject(message);
    }

    private void sendAcceptedMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("VALIDMOVE");
      outputStream.writeObject(message);
    }

    private void sendCanceledMoveMessage() throws IOException {
      ServerMessage message = new ServerMessage("CANCELLEDMOVE", game.getBoard());
      outputStream.writeObject(message);
    }

    private void sendBoard() throws IOException {
      ServerMessage message = new ServerMessage("SETBOARD", game.getBoard());
      outputStream.writeObject(message);
    }

    private void sendPlayer() throws IOException {
      ServerMessage message = new ServerMessage("SETPLAYER", game.nextPlayer());
      outputStream.writeObject(message);
    }

    private void sendMoveAcceptedMessage() throws IOException {
      ServerMessage message = new ServerMessage("MOVEACCEPTED");
      outputStream.writeObject(message);
    }
  }

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