package edu.pwr.checkers.model;

public interface Piece {
    Color getColor();
    Field getField();
    void setField(Field field);
}

class ClassicPiece implements Piece {
    private final Color color;
    private Field field;

    public ClassicPiece(Color color) {
        this.color = color;
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
