package edu.pwr.checkers.model;

/**
 * This interface provides method to crate new Field objects.
 * @version 1.0
 * @author ?? //FIXME: ej bo nie pamiętam, wpisz tu coś i poniżej w autorach też
 */
public interface FieldFactory {
    /**
     * This is a factory method.
     * @param coordinates coordinates which new field should have.
     * @return newly crated field.
     */
    Field getField(Coordinates coordinates);
}

/**
 * This is a implementation of {@link FieldFactory} interface,
 * which creates {@link ClassicField}s. Additionally, it can
 * immediately create piece on newly created field on demand.
 * Suitable for classic game of Chinese Checkers.
 * @version 1.0
 * @author ??
 */
class ClassicFieldFactory implements FieldFactory {
    /** For which piece color generated fields should be home for. */
    protected Color homeFor;
    /** Should the field be generated with new piece or not. */
    protected boolean withPiece;

    /**
     * The only constructor.
     * @param homeFor For which piece color this field is home for.
     * @param withPiece Should this factory generate field with piece.
     */
    public  ClassicFieldFactory(Color homeFor, boolean withPiece) {
        this.homeFor = homeFor;
        this.withPiece = withPiece;
    }

    /**
     * @inheritDoc
     * This particular implementation also has ability to create new Pieces,
     * which sit on top of newly created field.
     */
    @Override
    public Field getField(Coordinates coordinates) {
        Field field = new ClassicField(coordinates, homeFor);
        if (withPiece) {
            field.setPiece(new ClassicPiece(homeFor.getInverse(), field));
        }
        return field;
    }
}

