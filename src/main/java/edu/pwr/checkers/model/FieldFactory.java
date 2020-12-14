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
            switch (homeFor) {
                case RED:
                    field.setPiece(new ClassicPiece(Color.CYAN));
                    break;
                case CYAN:
                    field.setPiece(new ClassicPiece(Color.RED));
                    break;
                case YELLOW:
                    field.setPiece(new ClassicPiece(Color.BLUE));
                    break;
                case BLUE:
                    field.setPiece(new ClassicPiece(Color.YELLOW));
                    break;
                case MAGENTA:
                    field.setPiece(new ClassicPiece(Color.GREEN));
                    break;
                case GREEN:
                    field.setPiece(new ClassicPiece(Color.MAGENTA));
                    break;
                default:
                    return null;
            }
        }
        return field;
    }
}

