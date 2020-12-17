package edu.pwr.checkers.client;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;

public class Mediator {
    private /*final*/ Player player;
    private final Controller controller;
    private final Client client;

    public Mediator(Client client) {
        this.client = client;
        this.player = client.getPlayer();
        controller = new Controller(this);
    }

    public void sendAcceptMoveRequest() {
        client.sendAcceptMoveRequest(player);
        controller.showMessage("Next players turn!");
    }

    public void sendCancelMoveRequest() {
        client.sendCancelMoveRequest(player);
    }

    public Board getBoard() {
        return client.getBoard();
    }

    public void setBoard(Board board) {
        controller.setBoard(board);
    }


    public boolean movePiece(Piece piece, Coordinates cor) {
        if (player.notMyPiece(piece)) {
            controller.showMessage("You cant move this piece!");
            return false;
        }
        else {
            boolean b = client.sendMoveRequest(player, piece, cor);
            if (b) {
                controller.showMessage("");
            }
            else {
                controller.showMessage("This is illegal move!");
            }
            return b;
        }
    }

    public void setStatus(String status) {
        controller.setStatus(status);
    }

    // for testing
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void refresh() {
        controller.refresh();
    }
}
