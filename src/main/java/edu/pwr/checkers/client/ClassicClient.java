package edu.pwr.checkers.client;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;
import edu.pwr.checkers.server.ServerMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClassicClient implements Client, Runnable {
  private final Mediator mediator;
  private final Player player;
  private final Socket clientSocket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  public ClassicClient(Player player, Socket socket) {
    this.player = player;
    this.clientSocket = socket;
    this.mediator = new Mediator(this);
  }

  @Override
  public synchronized boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates) {
    try {
      ClientMessage clientMessage = new ClientMessage("MOVEREQUEST", player, piece, coordinates);
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      String messageType = serverMessage.getMessage();

      if (messageType.equals("VALIDMOVE")) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      System.out.println("Message couldn't be sent.");
    }
    return false;
  }

  @Override
  public synchronized void sendCancelMoveRequest(Player player) {
    try {
      ClientMessage clientMessage = new ClientMessage("CANCELMOVE", player);
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      mediator.setBoard(serverMessage.getBoard());
    } catch (Exception ex) {
      System.out.println("Message couldn't be sent.");
    }
  }

  @Override
  public synchronized void sendAcceptMoveRequest(Player player) {
    try {
      ClientMessage clientMessage = new ClientMessage("ACCEPTMOVE", player);
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      Player current = serverMessage.getPlayer();
      mediator.setPlayer(current);
      mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
    } catch (Exception ex) {
      System.out.println("Message couldn't be sent.");
    }
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public Board getBoard() {
    return null;
  }

  public static void main (String[] args) throws IOException {
    System.out.println("Trying to connect with server...");
    new Socket("4444", 4444);
    System.out.println("Connected to server.");
  }

  @Override
  public void run() {
    System.out.println("Client is running!");
    try {
      setUp();
    } catch (Exception ex) {
      System.err.println("Client stopped working due to an error.");
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void setUp() throws IOException {
    inputStream = new ObjectInputStream(clientSocket.getInputStream());
    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
  }
}
