package edu.pwr.checkers.model;

import edu.pwr.checkers.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassicGame implements Game {
    protected int numberOfPlayers;
    protected Board board;
    protected CyclicGetter<Player> activePlayers;
    protected ArrayList<Player> ranking;
    protected MoveType lastMove;
    protected Player activePlayer;
    protected Coordinates beginPosition;
    protected Piece movingPiece;
    private Server server;

    public ClassicGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        board = new ClassicBoard(numberOfPlayers);
        activePlayers = new CyclicGetter<>(numberOfPlayers);
        ranking = new ArrayList<>(numberOfPlayers);
    }

    @Override
    public void init() {
        board.setup();
        List<Color> colors = board.getColors();
        for (int i = 0; i < numberOfPlayers; i++) {
            List<Color> playerColors = new ArrayList<>(1);
            playerColors.add(colors.get(i));
            activePlayers.addObject(new ClassicPlayer(playerColors));
            // player gets a set of pieces of one color
        }
        Random rd = new Random();
        int random = rd.nextInt(numberOfPlayers);
        activePlayer = activePlayers.get(random);
        lastMove = MoveType.NEWTURN;
    }

    @Override
    public void move(Player player, Piece piece, Coordinates newPosition) throws IllegalMoveException, WrongPlayerException {
        MoveType currMove = getType(piece, newPosition);
        Coordinates betweenPosition = new Coordinates((piece.getField().getPosition().x + newPosition.x) / 2,
          (piece.getField().getPosition().y + newPosition.y) / 2);

        if (lastMove == MoveType.NEWTURN) {
            movingPiece = piece;
            beginPosition = piece.getField().getPosition();
        }
        if (board.getField(newPosition) == null) {
            throw new IllegalMoveException();
        }

        Piece pieceInBetween = board.getField(betweenPosition).getPiece();
        Piece pieceOnNewCor = board.getField(newPosition).getPiece();

        if (player.notMyPiece(piece)) {
            throw new WrongPlayerException();
        } else if (currMove == MoveType.ONESTEP
          && (lastMove != MoveType.NEWTURN
          || pieceOnNewCor != null)) {
            throw new IllegalMoveException(); // done ONESTEP cannot do another
        } else if (currMove == MoveType.UNKNOWN) {
            throw new IllegalMoveException(); // UNKNOWN is an error
        } else if (currMove == MoveType.JUMPSEQ
          && (pieceOnNewCor != null || pieceInBetween == null
        || lastMove == MoveType.ONESTEP || lastMove == MoveType.UNKNOWN)) {
            throw new IllegalMoveException();
        } else {
            lastMove = currMove;
            Field newField = board.getField(newPosition);
            Field oldField = piece.getField();
            newField.setPiece(piece);
            oldField.setPiece(null);
            piece.setField(newField);
        }
    }

    public MoveType getType(Piece piece, Coordinates newCor) {
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
        // TODO: send board from the beginning of turn to client and set it
        if (lastMove != MoveType.NEWTURN) {
            Field oldField = board.getField(beginPosition);
            Field newField = movingPiece.getField();
            oldField.setPiece(movingPiece);
            newField.setPiece(null);
            movingPiece.setField(oldField);
        }
        lastMove = MoveType.NEWTURN;
    }

    @Override
    public void acceptMove(Player player) {
        lastMove = MoveType.NEWTURN;
        checkIfWon();
        activePlayer = activePlayers.getNext();
    }

    private boolean checkIfWon() {
        List<Color> colors = activePlayer.getColors();
        for (Color color: colors) {
            List<Piece> pieces = board.getPiecesOfColor(color);
            for (Piece piece: pieces) {
                if (piece.getField().getHomeForColor() != color) {
                    return false;
                }
            }
        }
        activePlayers.remove(activePlayer);
        ranking.add(activePlayer);
        return true;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public Player getActivePlayer() {
        return activePlayer;
    }
}
