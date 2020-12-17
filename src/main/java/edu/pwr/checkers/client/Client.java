package edu.pwr.checkers.client;

import edu.pwr.checkers.model.*;

public interface Client {
    boolean sendMoveRequest(Player player, Piece piece, Coordinates coordinates);
    void sendCancelMoveRequest(Player player);
    boolean sendAcceptMoveRequest(Player player);
    Player getPlayer();
    Board getBoard();
}
