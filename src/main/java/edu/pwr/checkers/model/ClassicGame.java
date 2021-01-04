package edu.pwr.checkers.model;

import edu.pwr.checkers.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of {@link Game} interface. Represents game of
 * Chinese Checkers with classic rules.
 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public class ClassicGame implements Game {
    /**
     * Number of players participating in this game.
     * Can be 2, 3, 4, or 6.
     */
    protected int numberOfPlayers;
    /**
     * Local saved state of Board object, representing current
     * state of the game.
     */
    protected Board board;
    /** Object making getting next player easy. */
    protected CyclicGetter<Player> activePlayers;
    /** Ranking in which players who won are put. */
    protected ArrayList<Player> ranking;
    /** Type of last partial move performed. */
    protected MoveType lastMove;
    /** Player whose turn is now. */
    protected Player activePlayer;
    /**
     * Saved initial position of piece that is being moved
     * in this turn. Used when board needs to be restored in
     * {@see ClassicGame#cancelMove()}.
     */
    protected Coordinates beginPosition;
    /** Piece that is being moved in this turn. */
    protected Piece movingPiece;
    /** Reference  to the instance of the server that hosts game. */
    private Server server;

    /**
     * The only constructor.
     * @param numberOfPlayers number of players in the game.
     *                        Can be 2, 3, 4, or 6.
     */
    public ClassicGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        board = new ClassicBoard(numberOfPlayers);
        activePlayers = new CyclicGetter<>(numberOfPlayers);
        ranking = new ArrayList<>(numberOfPlayers);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void init() {
        board.setup();
        List<Color> colors = board.getColors();
        for (int i = 0; i < numberOfPlayers; i++) {
            List<Color> playerColors = new ArrayList<>(1);
            playerColors.add(colors.get(i));
            activePlayers.addObject(new ClassicPlayer(playerColors));
        }
        Random rd = new Random();
        int random = rd.nextInt(numberOfPlayers);
        activePlayers.setCurrent(random);
        activePlayer = activePlayers.getNext();
        lastMove = MoveType.NEWTURN;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void move(Player player, Piece piece, Coordinates newPosition)
            throws IllegalMoveException, WrongPlayerException {
        piece = board.getField(piece.getField().getPosition()).getPiece();
        MoveType currMove = getType(piece, newPosition);
        Coordinates betweenPosition = new Coordinates(
                (piece.getField().getPosition().x + newPosition.x) / 2,
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
        } else if (pieceOnNewCor != null) {
            throw new IllegalMoveException();
        } else if (currMove == MoveType.ONESTEP
          && lastMove != MoveType.NEWTURN) {
            throw new IllegalMoveException(); // done ONESTEP cannot do another
        } else if (currMove == MoveType.UNKNOWN) {
            throw new IllegalMoveException(); // UNKNOWN is an error
        } else if (currMove == MoveType.JUMPSEQ && (pieceInBetween == null ||
                lastMove == MoveType.ONESTEP || lastMove == MoveType.UNKNOWN)) {
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

    /**
     * Method needed only for tests.
     * @param piece piece to be illegally moved.
     * @param newPosition New position to which piece is wanted to move.
     */
    public void illegalMove(Piece piece, Coordinates newPosition) {
        piece = board.getField(piece.getField().getPosition()).getPiece();

        Field newField = board.getField(newPosition);
        Field oldField = piece.getField();
        newField.setPiece(piece);
        oldField.setPiece(null);
        piece.setField(newField);
    }

    /**
     * This method returns type of move Piece wants to perform.
     * @param piece piece that will be moved.
     * @param newCor place to which piece will be moved.
     * @return type of move the piece wants to perform.
     */
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

    /**
     * @inheritDoc
     */
    @Override
    public void cancelMove(Player player) {
        if (lastMove != MoveType.NEWTURN) {
            Field oldField = board.getField(beginPosition);
            Field newField = board.getField(movingPiece.getField().getPosition());
            oldField.setPiece(movingPiece);
            newField.setPiece(null);
            movingPiece.setField(oldField);
        }
        lastMove = MoveType.NEWTURN;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void acceptMove(Player player) {
        lastMove = MoveType.NEWTURN;
        if (checkIfWon()) {
            // TODO: check if the whole game is done
        }
        activePlayer = activePlayers.getNext();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Player nextPlayer() {
        return activePlayers.getNext();
    }

    /**
     * This method checks if current player won.
     * If so, puts him in ranking and updates the getter of players.
     * @return whether active player won.
     */
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
        ranking.add(activePlayer);
        activePlayers = CyclicGetter.truncateCurrent(activePlayers);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Board getBoard() {
        return board;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Player getActivePlayer() {
        return activePlayer;
    }


}
