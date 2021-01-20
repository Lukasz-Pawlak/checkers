package edu.pwr.checkers.server;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.ClassicBoard;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Field;

public class Simulator {
    private Board board;

    public Simulator(int numOfPLayers) {
        this.board = new ClassicBoard(numOfPLayers);
        board.setup();
    }

    public Board nextMove(Move move) {
        Coordinates oldCor = new Coordinates(move.getOldX(), move.getOldY());
        Coordinates newCor = new Coordinates(move.getNewX(), move.getNewY());

        Field oldField = board.getField(oldCor);
        Field newField = board.getField(newCor);
        newField.setPiece(oldField.getPiece());
        oldField.setPiece(null);

        return board;
    }
}
