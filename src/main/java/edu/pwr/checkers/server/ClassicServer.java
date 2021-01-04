package edu.pwr.checkers.server;

import edu.pwr.checkers.client.ClientMessage;
import edu.pwr.checkers.model.*;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ClassicServer implements Server {
    private ServerSocketChannel serverSocket;
    private final Game game;
    public final int numOfPlayers;
    private final ByteBuffer buffer;
    private final InetAddress hostIP;
    private final int port;

    private static final int BUFFER_SIZE = 4096;//1024;
    private Selector selector;

    public ClassicServer(int numOfPlayers, InetAddress address, int port) throws IOException, WrongNumberException {
        if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4 && numOfPlayers != 6) {
            throw new WrongNumberException();
        }
        this.numOfPlayers = numOfPlayers;
        this.game = new ClassicGame(numOfPlayers);
        this.game.init();
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        System.out.println("Trying to start server with port " + port);
        this.port = port;
        this.hostIP = address;
        System.out.println("Started server with " + port);
    }

    @Override
    public void setUp() throws NullPointerException, RejectedExecutionException, IOException {
        System.out.println("There are " + numOfPlayers + " players.");
    }

    public static void main(String[] args) {
        Server server;

        if (args.length != 1) {
            System.err.println("Give only the number of players!");
            return;
        }

        try {
            int numOfPlayers = Integer.parseInt(args[0]);
            server = new ClassicServer(numOfPlayers, InetAddress.getLocalHost(), 4444);
            server.setUp();
            System.out.println("Server running...");
            server.run();
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
    }
//***********************************************************************************


    public void run() {
        try {

            logger(String.format("Trying to accept connections on %s:%d...",
                    hostIP.getHostAddress(), port));
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            ServerSocket socket = serverSocket.socket();
            InetSocketAddress address = new InetSocketAddress(hostIP, port);
            socket.bind(address);

            serverSocket.configureBlocking(false);
            int ops = serverSocket.validOps();
            serverSocket.register(selector, ops, null);
            while (true) {

                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> i = selectedKeys.iterator();

                while (i.hasNext()) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {
                        processAcceptEvent(serverSocket, key);
                    } else if (key.isReadable()) {
                        processReadEvent(key);
                    }
                    i.remove();
                }
            }
        } catch (IOException e) {
            logger(e.getMessage());
            e.printStackTrace();
        }
    }

    private void processAcceptEvent(ServerSocketChannel mySocket,
                                           SelectionKey key) throws IOException {

        logger("Connection Accepted...");

        // Accept the connection and make it non-blocking
        SocketChannel myClient = mySocket.accept();
        myClient.configureBlocking(false);

        // Register interest in reading this channel
        myClient.register(selector, SelectionKey.OP_READ);
    }

    private void processReadEvent(SelectionKey key)
            throws IOException {
        logger("Inside processReadEvent...");
        // create a ServerSocketChannel to read the request
        SocketChannel client = (SocketChannel) key.channel();
        ClientMessage message = read(client);
        // TODO: actual message processing


    }

    public static void logger(String msg) {
        System.out.println(msg);
    }

    private ClientMessage read(SocketChannel channel) throws IOException {
        buffer.clear();
        channel.read(buffer);
        return SerializationUtils.deserialize(buffer.array());
    }
}

/*

   private class SocketHandler implements Runnable {
        private final Socket socket;
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

            switch (messageType) {
                case "MOVEREQUEST": {
                    System.out.println("Received move request.");
                    game.move(player, piece, coordinates);
                    sendAcceptedMoveMessage();
                    System.out.println("Sent move accepted request.");
                }
                case "CANCELMOVE": {
                    System.out.println("Received cancel move message.");
                    game.cancelMove(player);
                    sendCanceledMoveMessage();
                    System.out.println("Sent cancelled move message.");
                }
                case "ACCEPTMOVE": {
                    System.out.println("Received accept move message.");
                    game.acceptMove(player);
                    sendMoveAcceptedMessage();
                    System.out.println("Sent accepted move message.");
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
 */