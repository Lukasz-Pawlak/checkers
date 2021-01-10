package edu.pwr.checkers.client;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.model.*;
import edu.pwr.checkers.server.Server;
import edu.pwr.checkers.server.ServerMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;


/**
 * Implementation of {@link Client} interface.
 * It is a class provided to work with {@see ClassicServer}
 * especially connect with it properly.

 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public class ClassicClient implements Client {
  private Mediator mediator;
  private final Socket clientSocket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  private Integer numMsgReceived = 0;
  private volatile ServerMessage requestMessageAnswer;

  /**
   * Constructor that sets the client's socket.
   * @param socket The socket to be set.
   */
  public ClassicClient(Socket socket) {
    this.clientSocket = socket;
  }

  /**
   * The basic setUp of the client:
   * - it sets the basic
   * @throws IOException appears when there is a problem with the socket.
   */
  private void setUp() throws IOException {
    inputStream = new ObjectInputStream(clientSocket.getInputStream());
    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
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
    if (piece == null) {
      Logger.err("client: sendMoveRequest: null piece passed");
      System.exit(1);
    }
    try {
      ClientMessage clientMessage = new ClientMessage("MOVEREQUEST", player, piece, coordinates);
      int begin;
      synchronized (numMsgReceived) {
        begin = numMsgReceived;
      }
      send(clientMessage);
      Logger.debug("Tu MoveRequest, czekam na wiadomość!");
      do {
        sleep(1);
        //Logger.debug("in loop");
        synchronized (numMsgReceived) {
          if (numMsgReceived > begin) {
            break;
          }
        }
      } while (true);

        Logger.debug("Tu MoveRequest, otrzymałem wiadomość!");
        mediator.setBoard(requestMessageAnswer.getBoard());
        return requestMessageAnswer.getMessage().equals("VALIDMOVE");
      } catch (Exception ex) {
      Logger.err("sendMoveRequest: Message couldn't be sent.");
      ex.printStackTrace();
    }
    return false;
  }

  private void send(ClientMessage message) throws IOException {
      outputStream.writeObject(message);
  }

  @Override
  public synchronized void sendCancelMoveRequest(Player player) {
    try {
      ClientMessage clientMessage = new ClientMessage("CANCELMOVE", player);
      int begin;
      synchronized (numMsgReceived) {
        begin = numMsgReceived;
      }
      outputStream.writeObject(clientMessage);
      Logger.debug("Tu CancelRequest, czekam na wiadomość!");
      do {
        sleep(1);
        synchronized (numMsgReceived) {
          if (numMsgReceived > begin) {
            break;
          }
        }
      } while (true);
        Logger.debug("Tu CancelRequest, otrzymałem wiadomość!");
        mediator.setBoard(requestMessageAnswer.getBoard());
      } catch (Exception ex) {
      Logger.err("sendCancelMoveRequest: Message couldn't be sent.");
    }
  }

  @Override
  public synchronized void sendAcceptMoveRequest(Player player) {
    try {
      ClientMessage clientMessage = new ClientMessage("ACCEPTMOVE", player);
      int begin;
      synchronized (numMsgReceived) {
        begin = numMsgReceived;
      }
      outputStream.writeObject(clientMessage);
      Logger.debug("Tu AcceptMove, czekam na wiadomość!");
      do {
        Thread.sleep(1);
        synchronized (numMsgReceived) {
          if (numMsgReceived > begin) {
            break;
          }
        }
      } while (true);
      Logger.debug("Tu AcceptMove, otrzymałem wiadomość");
      Player current = requestMessageAnswer.getPlayer();
      mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
    } catch (Exception ex) {
      Logger.err("sendAcceptMoveRequest: Message couldn't be sent.");
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
      Logger.err("getPlayer: Message couldn't be sent.");
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
      Logger.err("getBoard: Message couldn't be sent.");
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
      ServerMessage serverMessage = null;
      String message;
      serverMessage = (ServerMessage) inputStream.readObject();
      message = serverMessage.getMessage();
      while (!message.equals("ENDOFGAME")) {
        if (message.equals("SETACTIVE")) {
          Logger.debug("Dostałem wiadomość SETACTIVE");
          Player player = serverMessage.getPlayer();
          Color color = player.getColors().get(0);
          mediator.setStatus("NOW PLAYING " + color.toString());
        }  else {
          synchronized (numMsgReceived) {
            requestMessageAnswer = serverMessage;
            numMsgReceived++;
          }
        }
        serverMessage = (ServerMessage) inputStream.readObject();
        message = serverMessage.getMessage();
      }
      mediator.setStatus(serverMessage.getStatus());
    } catch (Exception ex) {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Logger.err("Client stopped working due to an error.");
      ex.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
