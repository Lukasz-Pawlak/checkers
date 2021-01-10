package edu.pwr.checkers.model;

import edu.pwr.checkers.Logger;

import java.io.Serializable;

/**
 * This interface represents a piece in the game.
 * @version 1.0
 * @author Wojciech Sęk
 */
public interface Piece extends Serializable {
    /**
     * This method return color of the piece.
     * @return color of this piece.
     */
    Color getColor();

    /**
     * This function returns field on which this piece stands.
     * @return field on which this piece stands.
     */
    Field getField();

    /**
     * This function sets field on which this piece stands.
     * @param field field to be set.
     */
    void setField(Field field);
}

/**
 * This is a simple implementation of Piece interface, suitable
 * for possibly many different games. Most importantly: suitable
 * for classic version of Chinese checkers.
 * @version 1.0
 * @author Wojchech Sęk
 */
class ClassicPiece implements Piece {
    /** Color of this piece. */
    private final Color color;
    /** Field in which this piece currently stands. */
    private Field field;

    /**
     * The only constructor.
     * @param color color of this piece.
     * @param field initial field on which this piece will stand.
     */
    public ClassicPiece(Color color, Field field) {
        this.color = color;
        this.field = field;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Field getField() {
        return field;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setField(Field field) {
        this.field = field;
        Logger.debug(this.hashCode() + " >>> zmiana pozycji");
    }
}
