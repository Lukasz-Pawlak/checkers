package edu.pwr.checkers.server;

import edu.pwr.checkers.client.ClientMessage;
import edu.pwr.checkers.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    this.game.init();
    System.out.println("Trying to start server with port 4444...");
    this.serverSocket = new ServerSocket(4444);
    System.out.println("Started server with 4444...");
  }

  @Override
  public void setUp() throws NullPointerException, RejectedExecutionException, IOException {
    ExecutorService pool = Executors.newFixedThreadPool(numOfPlayers);
    System.out.println("There are " + numOfPlayers + " players.");

    for (int i = 1; i <= numOfPlayers; i++) {
      System.out.println("Waiting for player " + i);
      Socket socket = serverSocket.accept();
      SocketHandler handler =  new SocketHandler(socket);
      handler.sendPlayer();
      handler.sendBoard();
      pool.execute(handler);
    }
  }

  private class SocketHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    public SocketHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      process(socket);
    }

    private void process(Socket socket) {
      System.out.println("Wchodzę w process.");
      try {
        System.out.println("Trying to create inout streams.");
        inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("input stream correctly connected");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("output stream correctly connected");
        sendBoard();
      } catch (IOException e) {
    // nothing
      }

      while (true) {
        try {
          ClientMessage message = (ClientMessage) inputStream.readObject();
          processMessage(message);
        } catch (IOException | ClassNotFoundException e) {
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
        }
      }
    }

    private synchronized void processMessage(ClientMessage message) throws IllegalMoveException, WrongPlayerException, IOException {
      String messageType = message.getMessage();
      Player player = message.getPlayer();
      Coordinates coordinates = message.getCoordinates();
      Piece piece = message.getPiece();

      if (messageType.equals("MOVEREQUEST")) {
        System.out.println("Received move request.");
        game.move(player, piece, coordinates);
        sendAcceptedMoveMessage();
        System.out.println("Sent move accepted request.");
      } else if (messageType.equals("CANCELMOVE")) {
        System.out.println("Received cancel move message.");
        game.cancelMove(player);
        sendCanceledMoveMessage();
        System.out.println("Sent cancelled move message.");
      } else if (messageType.equals("ACCEPTMOVE")) {
        System.out.println("Received accept move message.");
        game.acceptMove(player);
        sendMoveAcceptedMessage();
        System.out.println("Sent accepted move message.");
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