package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;
import edu.pwr.checkers.server.ServerMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClassicClient implements Client {
  private Mediator mediator;
  private Player player;
  private final Socket clientSocket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  public ClassicClient(Socket socket) {
    this.clientSocket = socket;
    try {
      setUp();
      System.out.println("Client is set up!");
      this.player = getPlayer();
      this.mediator = new Mediator(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setUp() throws IOException {
    inputStream = new ObjectInputStream(clientSocket.getInputStream());
    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    //To receive and set the board.
    //board = sendGetBoard();
    System.out.println("Got the board.");
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
  public synchronized Player getPlayer() {
    try {
      ClientMessage clientMessage = new ClientMessage("GETPLAYER");
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      return serverMessage.getPlayer();
    } catch (Exception ex) {
      System.out.println("Message couldn't be sent.");
    }
    return null;
  }

  @Override
  public synchronized Board getBoard() {
    try {
      ClientMessage clientMessage = new ClientMessage("GETBOARD");
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      return serverMessage.getBoard();
    } catch (Exception ex) {
      System.out.println("Message couldn't be sent.");
    }
    return null;
  }

  public static void main (String[] args) throws IOException {
    System.out.println("Trying to connect with server...");
    ClassicClient client = new ClassicClient(new Socket("localhost", 4444));
    System.out.println("Connected to server.");
  }

  public void run() {
    System.out.println("Client is running!");
    try {
      setUp();
      System.out.println("Client is set up!");
    } catch (Exception ex) {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.err.println("Client stopped working due to an error.");
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
