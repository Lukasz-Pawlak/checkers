package edu.pwr.checkers.model;

public interface Game {
    public void init();
    void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException;
    void confirmMove(Player player);
    void rollback(Player player);
}
