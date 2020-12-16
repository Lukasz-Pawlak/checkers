package edu.pwr.checkers.model;

import java.io.Serializable;

public interface Game extends Serializable {
    void init();
    void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException;
    void cancelMove(Player player);
    void acceptMove(Player player);
    Board getBoard();
}
