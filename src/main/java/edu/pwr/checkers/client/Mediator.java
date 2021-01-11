package edu.pwr.checkers.client;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Piece;
import edu.pwr.checkers.model.Player;

import java.io.IOException;

/**
 * This class is responsible for communication between client and
 * controller. Also it provides information about player this client
 * is corresponding to.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class Mediator {
    /** Player to which this client is corresponding. */
    private /*final*/ Player player;
    /** Controller used to communicate with GUI. */
    private final Controller controller;
    /** Client object used to communicate with server. */
    private final Client client;

    public void startGame() {
        controller.startGame();
    }

    /**
     * Thew only constructor. Mediator is bound to one client
     * and one Controller, therefore one GUI.
     * @param client client to which mediator is bound.
     */
    public Mediator(Client client) {
        this.client = client;
        controller = new Controller(this);
    }

    /**
     * Method responsible for sending accept move request via client.
     */
    public void sendAcceptMoveRequest() {
        client.sendAcceptMoveRequest(player);
        controller.showMessage("Next players turn!");
    }

    /**
     * Method responsible for sending cancel move request via client.
     */
    public void sendCancelMoveRequest() {
        client.sendCancelMoveRequest(player);
    }

    /**
     * Board getter.
     * @return board currently used by the GUI.
     */
    public Board getBoard() {
        return client.getBoard();
    }

    /**
     * Method setting board used by GUI.
     * @param board board to be set.
     */
    public void setBoard(Board board) {
        Logger.debug("mediator: set board");
        if (board != null)
            controller.setBoard(board);
    }

    /**
     * Used to refresh the board.
     */
    public void refresh() {
        controller.refresh();
    }


    /**
     * Method used to pass information about move Piece request
     * to client. Provides additional request validation.
     * @param piece piece to be moved.
     * @param cor new coordinates of piece.
     * @return if move was successful.
     */
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
            return true;
        }
    }

    /**
     * This method sets status of the game.
     * @param status status to be set.
     */
    public void setStatus(String status) {
        controller.setStatus(status);
    }

    /**
     * Player setter.
     * @param player player to be set.
     */
    public void setPlayer(Player player) {
        Logger.debug("mediator: set player: " + player);
        this.player = player;
        this.controller.showMessage("YOU ARE " + player.getColors().get(0).toString());
    }
}
