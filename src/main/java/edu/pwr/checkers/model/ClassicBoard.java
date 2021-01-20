package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Board}.
 * Represents classic board, in which fields are arranged in hexagonal
 * grid in a pattern of star. See artifacts.model.Board for more
 * detailed information.
 * @version 1.1
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public class ClassicBoard implements Board {
    //private final long int SE
    /**
     * Number of players needed for {@link ClassicBoard#setup()} and
     * {@link ClassicBoard#setup()}
     */
    private int playerNo;
    /**
     * Constant specifying side length of hexagon which is at the core
     * of the star. Measured in number of fields.
     * Left not final for possible future inheritance.
     */
    protected static int SIDE_LENGTH = 2; // change back to 5 when we're done with testing moves
    /**
     * Constant representing distance from central field to corner of the
     * core hexagon. Measured in number of fields, does not include
     * central field.
     */
    protected final static int HEX_RADIUS;
    /**
     * Constant representing distance from central field to corner of the
     * core hexagon. Measured in number of fields, does not include
     * central field.
     */
    protected final static int STAR_RADIUS;
    //protected final static int TORUS_SIZE;
    /**
     * Size of square in which whole board can be inscribed.
     */
    protected final static int SQUARE_SIZE;

    static {
        HEX_RADIUS = SIDE_LENGTH - 1;
        STAR_RADIUS = HEX_RADIUS * 2;
        //TORUS_SIZE = STAR_RADIUS + SIDE_LENGTH + 1;
        SQUARE_SIZE = 2 * STAR_RADIUS + 1;
    }

    // idk if this will be useful, just leaving it for now since it's already here
    public final static int RIGHT = 0;
    public final static int RIGHT_UP = 1;
    public final static int UP = 2;
    public final static int LEFT = 3;
    public final static int LEFT_DOWN = 4;
    public final static int DOWN = 5;

    /**
     * Number of directions.
     */
    protected final static int DIRECTIONS_NO = 6;

    /**
     * This variable holds all fields of this board.
     */
    protected Field[][] cells;

    /**
     * @inheritDoc
     *
     * @return new Coordinates object or null, when position is
     * outside square in which board is inscribed
     */
    @Override
    public Coordinates move(int direction, int amount, Coordinates pos) {
        if (Math.floorMod(direction, DIRECTIONS_NO) > 2) {
            amount *= -1;
        }
        int x = 0, y = 0;
        switch (Math.floorMod(direction, 3)) { // we use the symmetry to write less cases
            case 0:
                x = pos.x + amount;
                y = pos.y;
                break;
            case 1:
                x = pos.x + amount;
                y = pos.y + amount;
                break;
            case 2:
                x = pos.x;
                y = pos.y + amount;
                break;
        }
        if (!withinSquare(x, y))
            return null;
        return new Coordinates(x, y);
    }

    /**
     * Function checking if given position is not out of bounds.
     * @param x x coordinate
     * @param y y coordinate
     * @return is given position within board's square
     */
    private boolean withinSquare(int x, int y) {
        return x >= 0 && y >= 0 && x < SQUARE_SIZE && y < SQUARE_SIZE;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getSize() {
        return 2 * STAR_RADIUS + 1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Field getField(int x, int y) {
        if (!withinSquare(x, y))
            return null;
        return cells[x][y];
    }

    /**
     * @inheritDoc
     */
    @Override
    public Field getField(Coordinates cor) {
        return getField(cor.x, cor.y);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Color> getColors() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.CYAN);
        switch (playerNo) {
            case 2:
                colors.add(Color.RED);
                break;
            case 3:
                colors.add(Color.GREEN);
                colors.add(Color.YELLOW);
                break;
            case 4:
                colors.add(Color.BLUE);
                colors.add(Color.RED);
                colors.add(Color.YELLOW);
                break;
            case 6:
                colors.add(Color.BLUE);
                colors.add(Color.GREEN);
                colors.add(Color.RED);
                colors.add(Color.YELLOW);
                colors.add(Color.MAGENTA);
                break;
            default:
                return null;
        }
        return colors;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Piece> getPiecesOfColor(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int j = 0; j < SQUARE_SIZE; j++) {
                if (cells[i][j] != null
                  && cells[i][j].getPiece() != null
                  && cells[i][j].getPiece().getColor() == color) {
                    pieces.add(cells[i][j].getPiece());
                }
            }
        }
        return pieces;
    }

    /**
     * The only constructor.
     * As demanded by {@link Board}, requires number of players.
     * @param playerNo number of players.
     */
    public ClassicBoard(int playerNo) {
        cells = new Field[SQUARE_SIZE][SQUARE_SIZE];
        this.playerNo = playerNo;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setup() {
        FieldFactory currentFactory = new ClassicFieldFactory(Color.NOCOLOR, false);
        cells[STAR_RADIUS][STAR_RADIUS] = currentFactory.getField(new Coordinates(STAR_RADIUS, STAR_RADIUS));
        for (int rad = 1; rad <= HEX_RADIUS; rad++) {               // for each ring
            for (int direction = 0; direction < DIRECTIONS_NO; direction++) {
                Coordinates curr = new Coordinates(STAR_RADIUS, STAR_RADIUS);           // starting from the center
                curr = move(direction, rad, curr);                  // move to the starting position
                for (int i = 0; i < rad; i++) {                     // create proper amount of Fields
                    cells[curr.x][curr.y] = currentFactory.getField(curr);
                    curr = move(direction + 2, 1, curr);
                }
            }
        }
        CyclicGetter<FieldFactory> cycle = new CyclicGetter<>(DIRECTIONS_NO);
        boolean redFactory = (playerNo == 2 || playerNo == 3 || playerNo == 4 || playerNo == 6);
        boolean greenFactory = (playerNo == 6);
        boolean blueFactory = (playerNo == 3 ||playerNo == 4 || playerNo == 6);
        boolean cyanFactory = (playerNo == 2 || playerNo == 4 || playerNo == 6);
        boolean magentaFactory = (playerNo == 3 ||playerNo == 6);
        boolean yellowFactory = (playerNo == 4 || playerNo == 6);

        cycle.addObject(new ClassicFieldFactory(Color.RED, redFactory));
        cycle.addObject(new ClassicFieldFactory(Color.GREEN, greenFactory));
        cycle.addObject(new ClassicFieldFactory(Color.BLUE, blueFactory));
        cycle.addObject(new ClassicFieldFactory(Color.CYAN, cyanFactory));
        cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, magentaFactory));
        cycle.addObject(new ClassicFieldFactory(Color.YELLOW, yellowFactory));

        for (int rad = HEX_RADIUS + 1; rad <= STAR_RADIUS; rad++) { // for each outer ring
            for (int direction = 0; direction < DIRECTIONS_NO; direction++) {
                Coordinates curr = new Coordinates(STAR_RADIUS, STAR_RADIUS);           // starting from the center
                currentFactory = cycle.getNext();
                curr = move(direction, rad, curr);                  // move to the starting position
                curr = move(direction + 2, rad - HEX_RADIUS, curr);
                for (int i = STAR_RADIUS; i >= rad; i--) {           // create proper amount of Fields
                    cells[curr.x][curr.y] = currentFactory.getField(curr);
                    curr = move(direction + 2, 1, curr);
                }
            }
        }
    }

    /**
     * Generic function used in {@link ClassicBoard#getNeighborsOf(Field)}
     * and in {@link ClassicBoard#getFurtherNeighborsOf(Field)}
     * @param field as in links.
     * @param distance how far from initial field should we seek.
     * @return list of neighbors that are exactly distance apart from
     * the field
     */
    private List<Field> getNeighborsOfDistance(Field field, int distance) {
        List<Field> list = new ArrayList<>();
        Coordinates pos = field.getPosition();
        for (int i = 0; i < DIRECTIONS_NO; i++) {
            Coordinates other = move(i, distance, pos);
            if (other != null && cells[other.x][other.y] != null) {
                list.add(cells[other.x][other.y]);
            }
        }
        return list;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Field> getNeighborsOf(Field field) {
        return getNeighborsOfDistance(field, 1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Field> getFurtherNeighborsOf(Field field) {
        return getNeighborsOfDistance(field, 2);
    }
}
