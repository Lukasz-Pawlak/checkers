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
        MoveType type = player.getCurrentMove().getMovetype();
        Piece pieceOnNewCor = board.getField(newPosition).getPiece();
        Coordinates betweenPosition = new Coordinates((piece.getField().getPosition().x + newPosition.x) / 2,
          (piece.getField().getPosition().y + newPosition.y) / 2);
        Piece pieceInBetween = board.getField(betweenPosition).getPiece();

        if (!player.getPieces().contains(piece)) {
            throw new WrongPlayerException();
        } else if (type == MoveType.ONESTEP
        && (pieceOnNewCor != null || player.getPreviousMove().getMovetype() != null)) {
            throw new IllegalMoveException();
        } else if (type == MoveType.JUMPSEQ
          && (pieceOnNewCor != null || pieceInBetween == null
          || (player.getPreviousMove().getMovetype() != MoveType.JUMPSEQ
          && player.getPreviousMove().getMovetype() != null))) {
            throw new IllegalMoveException();
        } else if (type == MoveType.UNKNOWN) {
            throw new IllegalMoveException();
        } else {
            piece.setField(board.getField(newPosition));
            player.setPreviousMove(player.getCurrentMove());
            player.setCurrentMove(null);
        }
    }

    @Override
    public void confirmMove(Player player) {

    }

    @Override
    public void rollback(Player player) {
        player.setCurrentMove(player.getPreviousMove());
    }
}
