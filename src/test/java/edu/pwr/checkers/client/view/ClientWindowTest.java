package edu.pwr.checkers.client.view;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.client.Client;
import edu.pwr.checkers.client.Mediator;
import edu.pwr.checkers.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ClientWindowTest {
    protected Client client;
    protected Board board;
    protected Player current;
    protected Game game;
    protected Mediator mediator;

    @Before
    public void setup() throws IOException, ClassNotFoundException {
        client = getMockedClient();
        game = getGame();
        game.init();
        board = cloneBoard(game.getBoard());
        current = game.getActivePlayer();
        when(client.getBoard()).thenReturn(board);
        when(client.getPlayer()).thenReturn(current);

        doAnswer((Answer<Boolean>) invocationOnMock -> {
            //return Boolean.TRUE;
            Object[] args = invocationOnMock.getArguments();
            try {
                game.move((Player) args[0], (Piece) args[1], (Coordinates) args[2]);
                return Boolean.TRUE;
            } catch (Exception ex) {
                return Boolean.FALSE;
            }//*/
        }).when(client).sendMoveRequest(any(Player.class), any(Piece.class), any(Coordinates.class));

        doAnswer((Answer<Void>) invocationOnMock -> {
            game.acceptMove(invocationOnMock.getArgument(0));
            current = game.getActivePlayer();
            mediator.setPlayer(current);
            mediator.setStatus("Now moving:\nPlayer with color " + current.getColors().get(0).toString());
            return null;
        }).when(client).sendAcceptMoveRequest(any(Player.class));

        doAnswer((Answer<Void>) invocationOnMock -> {
            game.cancelMove(invocationOnMock.getArgument(0));
            mediator.setBoard(cloneBoard(game.getBoard()));
            return null;
        }).when(client).sendCancelMoveRequest(any(Player.class));
    }

    // this isn't a proper test, I use it to see if everything displays correctly and rules
    @Test
    public void testRules() throws IOException, ClassNotFoundException {
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

    protected Game getGame() {
        return new ClassicGame(2);
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
            Logger.err("error in serialization: " + e.getClass());
        }
        return null;
    }
}
