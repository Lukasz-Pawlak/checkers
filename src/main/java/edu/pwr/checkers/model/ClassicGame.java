package edu.pwr.checkers.model;

import java.util.List;

public class ClassicGame implements Game {
    protected int numberOfPlayers;
    protected Board board;
    protected CyclicGetter<Player> players;
    protected Player activePlayer;

    public ClassicGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        board = new ClassicBoard();
        players = new CyclicGetter<>(numberOfPlayers);
    }

    @Override
    public void init() {
        board.setup();
        List<Color> colors = board.getColors(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
           players.addObject(new ClassicPlayer(colors.get(i)));
        }
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
