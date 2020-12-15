package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;

/**
 * TODO: What I've written here should probably go into a test with mocks
 */

public class Client {
    private Player player;
    private Game game;
    private Board board;

    public Client() {
        game = new ClassicGame(2); // later it will be set by server
        game.init();
        board = game.getBoard();
        player = new ClassicPlayer(board.getPiecesOfColor(Color.RED));
    }

    boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates) {
        return true;
        /*try {
            game.move(player, piece, coordinates);
            return true;
        } catch (IllegalMoveException | WrongPlayerException e) {
            e.printStackTrace();
        }
        return false;*/
    }

    void sendCancelMoveRequest(Player player) {

    }

    void sendAcceptMoveRequest(Player player) {
    }

    Player getPlayer() {
        return player;
    }

    Board getBoard() {
        return board;
    }
}
