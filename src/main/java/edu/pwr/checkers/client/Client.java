package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;

/**
 * This interface provides basic functionality of client-server communication
 * for the game of Chinese Checkers.
 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public interface Client {
    /**
     * This method sends partial move request.
     * @param player player who performs partial move.
     * @param piece piece moved.
     * @param coordinates new coordinates of the piece.
     * @return if move was acknowledged and permitted by server.
     */
    boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates);

    /**
     * This method sends a request to the server informing that player wants
     * to abort all his partial mover and restore game to the state before he
     * performed any. After that, he can start his move over.
     * @param player player who wants to cancel move.
     */
    void sendCancelMoveRequest(Player player);

    /**
     * This method sends a request to the server informing that player wants
     * to end his move. After that, it's next players move.
     * @param player player who wants to send the request.
     */
    void sendAcceptMoveRequest(Player player);

    /**
     * This function is used at the program startup.
     * It asks server which player this client will correspond to.
     * @return player associated with this client.
     */
    Player getPlayer();

    /**
     * This method asks server to send current version of board.
     * @return board send by server.
     */
    Board getBoard();
}
