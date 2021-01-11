package edu.pwr.checkers.client;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.client.Client;
import edu.pwr.checkers.client.Mediator;
import edu.pwr.checkers.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientTest {
    private Client client;
    private Socket server;

    private class SimplifiedServer implements Runnable {
        @Override
        public void run() {

        }
    }

    @Before
    public void prepare() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(4444));
            server = serverSocket.accept();
            client = getClient();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Ignore
    public void testClient() {

    }

    protected Client getClient() throws IOException {
        return new ClassicClient(new Socket("localhost", 4444));
    }
}
