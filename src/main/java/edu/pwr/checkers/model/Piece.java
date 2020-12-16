package edu.pwr.checkers.model;

import java.io.Serializable;

public interface Piece extends Serializable {
    Color getColor();
    Field getField();
    void setField(Field field);
}

class ClassicPiece implements Piece {
    private final Color color;
    private Field field;

    public ClassicPiece(Color color, Field field) {
        this.color = color;
        this.field = field;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public void setField(Field field) {
        this.field = field;
    }
}
