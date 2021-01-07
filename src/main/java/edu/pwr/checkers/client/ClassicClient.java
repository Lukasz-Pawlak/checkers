package edu.pwr.checkers.client;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.model.*;
import edu.pwr.checkers.server.Server;
import edu.pwr.checkers.server.ServerMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClassicClient implements Client {
  private Mediator mediator;
  private final Socket clientSocket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  public ClassicClient(Socket socket) {
    this.clientSocket = socket;
  }


  private void setUp() throws IOException {
    inputStream = new ObjectInputStream(clientSocket.getInputStream());
    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    //To receive and set the board.
    //board = sendGetBoard();
    ServerMessage greeting = getMessage();
    Logger.debug("client: server message: " + greeting.getMessage());
    mediator = new Mediator(this);
    mediator.setBoard(greeting.getBoard());
    mediator.setPlayer(greeting.getPlayer());
    Logger.debug("client: Player got: " + greeting.getPlayer());
    Logger.debug("client: Board got:" + greeting.getBoard());
    mediator.startGame();
  }

  @Override
  public synchronized boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates) {
    try {
      ClientMessage clientMessage = new ClientMessage("MOVEREQUEST", player, piece, coordinates);
      outputStream.writeObject(clientMessage);
      ServerMessage serverMessage;
      serverMessage = (ServerMessage) inputStream.readObject();
      String messageType = serverMessage.getMessage();

      return messageType.equals("VALIDMOVE");
    } catch (Exception ex) {
      Logger.err("Message couldn't be sent.");
      ex.printStackTrace();
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
      Logger.err("Message couldn't be sent.");
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
      //mediator.setPlayer(current);
      mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
    } catch (Exception ex) {
      Logger.err("Message couldn't be sent.");
    }
  }

  private ServerMessage getMessage() {
    ServerMessage message = null;
    try {
      message = (ServerMessage) inputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return message;
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
      Logger.err("Message couldn't be sent.");
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
      Logger.err("Message couldn't be sent.");
    }
    return null;
  }

  public static void main (String[] args) throws IOException {
    Logger.info("Trying to connect with server...");
    new ClassicClient(new Socket("localhost", 4444)).run();
  }

  public void run() {
    Logger.info("Client is running!");
    try {
      setUp();
      Logger.info("Client is set up!");
    } catch (Exception ex) {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Logger.err("Client stopped working due to an error.");
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
