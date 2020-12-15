package edu.pwr.checkers.client;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;

public class Mediator {
    private final Player player;
    private final Controller controller;
    private final Client client;

    public Mediator() {
        this.client = new Client();
        this.player = client.getPlayer();
        controller = new Controller(this);
    }

    public void sendAcceptMoveRequest() {
        client.sendAcceptMoveRequest(player);
    }

    public void sendCancelMoveRequest() {
        client.sendCancelMoveRequest(player);
    }

    public Board getBoard() {
        return client.getBoard();
    }


    public boolean movePiece(Piece piece, Coordinates cor) {
        if (player.notMyPiece(piece)) {
            controller.showMessage("You cant move this piece!");
            return false;
        }
        else {
            return client.sendMoveRequest(player, piece, cor);
        }
    }
}
