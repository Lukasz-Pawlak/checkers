package edu.pwr.checkers.server;

import edu.pwr.checkers.client.ClassicClient;
import edu.pwr.checkers.client.ClientMessage;
import edu.pwr.checkers.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ClassicServer implements Server {
  private final ServerSocket serverSocket;
  private final Game game;
  public final int numOfPlayers;

  public ClassicServer(int numOfPlayers) throws IOException, WrongNumberException {
    if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4 && numOfPlayers != 6) {
      throw new WrongNumberException();
    }
    this.numOfPlayers = numOfPlayers;
    this.game = new ClassicGame(numOfPlayers);
    this.serverSocket = new ServerSocket(4444);
  }

  @Override
  public void setUp() throws NullPointerException, RejectedExecutionException, IOException {
    ExecutorService pool = Executors.newFixedThreadPool(2 * numOfPlayers);
    List<Player> players = game.getActivePlayers();

    for (Player player: players) {
      Socket socket = serverSocket.accept();
      pool.execute(new ClassicClient(player, socket));
      pool.execute(new socketHandler(socket));
    }
  }

  private class socketHandler implements Runnable {
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    public socketHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      process(socket);
    }

    private void process(Socket socket) {
      try {
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
      } catch (IOException e) {
    // nothing
      }

      while (true) {
        try {
          ClientMessage message = (ClientMessage) inputStream.readObject();
          processMessage(message);
        } catch (IOException e) {
          System.out.println("Couldn't read the message.");
        } catch (IllegalMoveException e) {
          try {
            sendIllegalMoveMessage();
          } catch (IOException f) {
            System.out.println("Couldn't send the message.");
          }
        } catch (WrongPlayerException e) {
          try {
            sendWrongPlayerMessage();
          } catch (IOException f) {
            System.out.println("Couldn't send the message.");
          }
        } catch (ClassNotFoundException e) {
          System.out.println("Couldn't read the message.");
        }
      }
    }

    private synchronized void processMessage(ClientMessage message) throws IllegalMoveException, WrongPlayerException, IOException {
      String messageType = message.getMessage();
      Player player = message.getPlayer();
      Coordinates coordinates = message.getCoordinates();
      Piece piece = message.getPiece();

      if (messageType == "MOVEREQUEST") {
        game.move(player, piece, coordinates);
        sendAcceptedMoveMessage();
      } else if (messageType == "CANCELMOVE") {
        game.cancelMove(player);
        sendCanceledMoveMessage();
      } else if (messageType == "ACCEPTMOVE") {
        game.acceptMove(player);
        sendMoveAcceptedMessage();
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

    private void sendMoveAcceptedMessage() throws IOException {
      ServerMessage message = new ServerMessage("MOVEACCEPTED");
      outputStream.writeObject(message);
    }
  }

  public static void main(String[] args) {
    Server server = null;

    if (args.length != 1) {
      System.err.println("Give only the number of players!");
      return;
    }

    try {
      int numOfPlayers = Integer.parseInt(args[0]);
      server = new ClassicServer(numOfPlayers);
      server.setUp();
    } catch(NumberFormatException e) {
      System.err.println("Give an integer!");
    } catch(WrongNumberException e) {
      System.err.println("The number can be: 2,3,4 or 6");
    } catch(IOException e) {
      System.err.println("Couldn't set up the server.");
    } catch(NullPointerException e) {
      System.err.println("Server is null.");
    } catch(RejectedExecutionException e) {
      System.err.println("Execution rejected.");
    }

    System.out.println("Server running...");
  }
}