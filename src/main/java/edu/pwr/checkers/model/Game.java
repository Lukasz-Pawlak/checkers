package edu.pwr.checkers.model;

public interface Game {
    void init();
    void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException;
}
