package edu.pwr.checkers.model;

import java.util.List;

public class ClassicGame implements Game {
    protected int numberOfPlayers;
    protected Board board;
    protected CyclicGetter<Player> players;

    public ClassicGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        board = new ClassicBoard();
        players = new CyclicGetter<>(numberOfPlayers);
    }

    @Override
    public void init() {
        board.setup();
        board.getColors(numberOfPlayers);
    }

    @Override
    public void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException {

    }

    @Override
    public void confirmMove(Player player) {
        
    }

    @Override
    public void rollback(Player player) {

    }
}
