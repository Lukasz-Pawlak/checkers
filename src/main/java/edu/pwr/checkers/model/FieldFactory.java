package edu.pwr.checkers.model;

public interface FieldFactory {
    Field getField(Coordinates coordinates);
}

class ClassicFieldFactory implements FieldFactory {
    protected Color homeFor;
    protected boolean withPiece;

    public  ClassicFieldFactory(Color homeFor, boolean withPiece) {
        this.homeFor = homeFor;
        this.withPiece = withPiece;
    }

    @Override
    public Field getField(Coordinates coordinates) {
        Field field = new ClassicField(coordinates, homeFor);
        if (withPiece) {
            field.setPiece(new ClassicPiece(homeFor));
        }
        return field;
    }
}

