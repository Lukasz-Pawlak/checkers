package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Client;
import edu.pwr.checkers.client.Mediator;
import edu.pwr.checkers.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ClientWindowTest {
    protected Client client;
    protected Board board;
    protected CyclicGetter<Player> players;
    protected Player current;
    protected Game game;

    @Before
    public void setup() {
        client = getMockedClient();
        game = new ClassicGame(2);
        game.init();
        players = new CyclicGetter<>(2);
        players.addObject(new ClassicPlayer(game.getBoard().getPiecesOfColor(Color.CYAN)));
        players.addObject(new ClassicPlayer(game.getBoard().getPiecesOfColor(Color.RED)));
        current = players.getNext();
        when(client.getBoard()).thenReturn(game.getBoard());
        when(client.getPlayer()).thenReturn(current);

        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                return Boolean.TRUE;
                /*Object[] args = invocationOnMock.getArguments();
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
                current = players.getNext();
                return null;
            }
        }).when(client).sendAcceptMoveRequest(any(Player.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                game.cancelMove(invocationOnMock.getArgument(0));
                return null;
            }
        }).when(client).sendCancelMoveRequest(any(Player.class));
    }

    // this isn't a proper test, I use it to see if everything displays correctly and rules
    @Test
    public void testRules() {
        new Mediator(client);
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        scanner.close();
    }

    protected Client getMockedClient() {
        return mock(Client.class);
    }
}
