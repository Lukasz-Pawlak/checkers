package edu.pwr.checkers.model;

import java.io.Serializable;

/**
 * This interface represents field on a {@link Board}.
 * May not be as general as {@link Game} or {@link Board}, but
 * should be suitable for all Chinese Checkers - like games.
 * @version 1.0
 * @author Wojciech Sęk
 */
public interface Field extends Serializable {
    /**
     * This function returns position of this field as in
     * a cartesian coordinates on the square in which board
     * containing this field can be inscribed.
     * @return position of this field.
     */
    Coordinates getPosition();

    /**
     * This method returns a color for what kind of piece this
     * field is home for.
     * @return color for what kind of piece this field is home for.
     */
    Color getHomeForColor();

    /**
     * This function returns piece standing on this field.
     * If no piece stands on this field, null should be returned.
     * @return piece standing on this field.
     */
    Piece getPiece();

    /**
     * This method sets piece on this field to the one passed in argument.
     * @param piece piece to be set.
     */
    void setPiece(Piece piece);
}

/**
 * This is simple implementation of {@link Field} interface.
 * Written to suit classic version of Chinese Checkers.
 * @version 1.0
 * @author Wojciech Sęk
 */
class ClassicField implements Field {
    /** Position on board. {@see Field#getPosition()} */
    private final Coordinates position;
    /** For what piece color this field is home for. */
    private final Color home;
    /** Piece standing on this filed. */
    private Piece piece;

    /**
     * The only constructor.
     * @param pos position of this field on {@link Board}.
     * @param home for which color this field is home for.
     */
    public ClassicField(Coordinates pos, Color home) {
        position = pos;
        this.home = home;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Coordinates getPosition() {
        return position;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Color getHomeForColor() {
        return home;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Piece getPiece() {
        return piece;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
