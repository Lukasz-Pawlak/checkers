package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;
import edu.pwr.checkers.server.ServerMessage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClassicClient implements Client {
    private final Mediator mediator;
    private final Player player;
    private final ByteBuffer buffer;
    protected static int BUFFER_SIZE = 4096;//1024;

    private final SocketChannel socketChannel;

    public ClassicClient(InetAddress hostIP, int port) throws IOException {
        buffer = ByteBuffer.allocate(BUFFER_SIZE);

        InetSocketAddress myAddress = new InetSocketAddress(hostIP, port);
        socketChannel = SocketChannel.open(myAddress);

        //TODO: send init request or smth
        this.player = getPlayer(); // ...

        System.out.println("Client is set up!");
        this.mediator = new Mediator(this);
    }

    public void init() {

    }

    private void send(ClientMessage message) throws IOException {
        buffer.clear();
        buffer.put(SerializationUtils.serialize(message));
        buffer.flip();
        socketChannel.write(buffer);
    }

    private ServerMessage receive() throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        return SerializationUtils.deserialize(buffer.array());
    }

    @Override
    public boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates) {
        try {
            ClientMessage clientMessage = new ClientMessage(
                    "MOVEREQUEST", player, piece, coordinates);
            send(clientMessage);
            ServerMessage serverMessage = receive();
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
    public void sendCancelMoveRequest(Player player) {
        try {
            ClientMessage clientMessage = new ClientMessage("CANCELMOVE", player);
            send(clientMessage);
            ServerMessage serverMessage = receive();
            mediator.setBoard(serverMessage.getBoard());
        } catch (Exception ex) {
            System.out.println("Message couldn't be sent.");
        }
    }

    @Override
    public void sendAcceptMoveRequest(Player player) {
        try {
            ClientMessage clientMessage = new ClientMessage("ACCEPTMOVE", player);
            send(clientMessage);
            ServerMessage serverMessage = receive();
            Player current = serverMessage.getPlayer();
            mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
        } catch (Exception ex) {
            System.out.println("Message couldn't be sent.");
        }
    }

    @Override
    public Player getPlayer() {
        try {
            ClientMessage clientMessage = new ClientMessage("GETPLAYER");
            send(clientMessage);
            ServerMessage serverMessage = receive();
            return serverMessage.getPlayer();
        } catch (Exception ex) {
            System.out.println("Message couldn't be sent.");
        }
        return null;
    }

    @Override
    public Board getBoard() {
        try {
            ClientMessage clientMessage = new ClientMessage("GETBOARD");
            send(clientMessage);
            ServerMessage serverMessage = receive();
            return serverMessage.getBoard();
        } catch (Exception ex) {
            System.out.println("Message couldn't be sent.");
        }
        return null;
    }


//*******************************************************************************


    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(100);
        ClassicClient client = new ClassicClient(InetAddress.getLocalHost(),4444);
        System.out.println("Client is running!");
        try {
            System.out.println("Client is set up!");
        } catch (Exception ex) {
            try {
                client.socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println("Client stopped working due to an error.");
        } finally {
            try {
                client.socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
