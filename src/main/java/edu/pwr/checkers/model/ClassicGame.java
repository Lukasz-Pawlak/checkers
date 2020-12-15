package edu.pwr.checkers.model;

import edu.pwr.checkers.server.Server;

import java.util.ArrayList;
import java.util.List;

public class ClassicGame implements Game {
    protected int numberOfPlayers;
    protected Board board;
    protected CyclicGetter<Player> players;
    protected MoveType lastMove;
    protected Player activePlayer;
    private Server server;

    public ClassicGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        board = new ClassicBoard(numberOfPlayers);
        players = new CyclicGetter<>(numberOfPlayers);
    }

    @Override
    public void init() {
        board.setup();
        List<Color> colors = board.getColors();
        for (int i = 0; i < numberOfPlayers; i++) {
           players.addObject(new ClassicPlayer(board.getPiecesOfColor(colors.get(i))));
        }
    }

    @Override
    public void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException {
        Coordinates betweenPosition = new Coordinates((piece.getField().getPosition().x + newPosition.x) / 2,
          (piece.getField().getPosition().y + newPosition.y) / 2);
        Piece pieceInBetween = board.getField(betweenPosition).getPiece();
        Piece pieceOnNewCor = board.getField(newPosition).getPiece();

        if (!player.getPieces().contains(piece)) {
            throw new WrongPlayerException();
        } else if (lastMove == MoveType.ONESTEP
        && (pieceOnNewCor != null || lastMove != null)) {
            throw new IllegalMoveException();
        } else if (lastMove == MoveType.JUMPSEQ
          && (pieceOnNewCor != null || pieceInBetween == null
          || (lastMove != MoveType.JUMPSEQ
          && lastMove != null))) {
            throw new IllegalMoveException();
        } else if (lastMove == MoveType.UNKNOWN) {
            throw new IllegalMoveException();
        } else {
            lastMove = getType(piece, newPosition);
        }
    }

    private int getPlayerNum(Player player) {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (players.get(i) == player) {
                return i;
            }
        }
        return -1;
    }

    public MoveType getType (Piece piece, Coordinates newCor) {
        Field field = piece.getField();
        Field newField = board.getField(newCor.x, newCor.y);
        List<Field> neighbours = board.getNeighborsOf(field);
        List<Field> furtherNeighbours = board.getFurtherNeighborsOf(field);
        if (neighbours.contains(newField)) {
            return MoveType.ONESTEP;
        } else if (furtherNeighbours.contains(newField)) {
            return MoveType.JUMPSEQ;
        }
        return MoveType.UNKNOWN;
    }

    @Override
    public void cancelMove(Player player) {
        int index = getPlayerNum(player);
        lastMove = MoveType.NEWTURN;
    }

    @Override
    public void acceptMove(Player player) {
        board =  player.getCurrState();
        lastMove = MoveType.NEWTURN;
    }

    @Override
    public Board getBoard() {
        return board;
    }
}
