package edu.pwr.checkers.model;

public interface Field {
    Coordinates getPosition();
    Color getHomeForColor();
    Piece getPiece();
    void setPiece(Piece piece);
}

class ClassicField implements Field {
    private final Coordinates position;
    private final Color home;
    private Piece piece;

    public ClassicField(Coordinates pos, Color home) {
        position = pos;
        this.home = home;
    }

    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public Color getHomeForColor() {
        return home;
    }

    @Override
    public Piece getPiece() {
        return piece;
    }

    @Override
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
