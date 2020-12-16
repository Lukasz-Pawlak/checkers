package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Client;
import edu.pwr.checkers.client.Mediator;
import edu.pwr.checkers.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ClientWindowTest {
    protected Client client;
    protected Board board;
    protected CyclicGetter<Player> players;
    protected Player current;
    protected Game game;
    protected Mediator mediator;

    @Before
    public void setup() {
        client = getMockedClient();
        game = new ClassicGame(2);
        game.init();
        board = cloneBoard(game.getBoard());
        current = game.getActivePlayer(); //players.getNext();
        when(client.getBoard()).thenReturn(board);
        when(client.getPlayer()).thenReturn(current);

        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                //return Boolean.TRUE;
                Object[] args = invocationOnMock.getArguments();
                //System.out.println("hello from mocked move");
                try {
                    game.move((Player) args[0], (Piece) args[1], (Coordinates) args[2]);
                    return Boolean.TRUE;
                } catch (Exception ex) {
                    return Boolean.FALSE;
                }//*/
            }
        }).when(client).sendMoveRequest(any(Player.class), any(Piece.class), any(Coordinates.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                game.acceptMove(invocationOnMock.getArgument(0));
                current = game.getActivePlayer();
                mediator.setPlayer(current);
                mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
                return null;
            }
        }).when(client).sendAcceptMoveRequest(any(Player.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                game.cancelMove(invocationOnMock.getArgument(0));
                mediator.setBoard(cloneBoard(game.getBoard()));
                return null;
            }
        }).when(client).sendCancelMoveRequest(any(Player.class));
    }

    // this isn't a proper test, I use it to see if everything displays correctly and rules
    @Test
    public void testRules() {
        mediator = new Mediator(client);
        mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        scanner.close();
    }

    /** Perhaps for overriding, dunno */
    protected Client getMockedClient() {
        return mock(Client.class);
    }

    /** This method will not be needed in final program, as it will happen by itself */
    protected Board cloneBoard(Board board) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(board);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Board) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("error in serialization: " + e.getClass());
        }
        return null;
    }
}
