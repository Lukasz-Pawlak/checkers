package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class ClassicBoard implements Board {
    private int playerNo;
    protected static int SIDE_LENGTH = 5;       // left not final for inheritance
    protected final static int HEX_RADIUS;
    protected final static int STAR_RADIUS;
    protected final static int TORUS_SIZE;

    static {
        HEX_RADIUS = SIDE_LENGTH - 1;
        STAR_RADIUS = HEX_RADIUS * 2;
        TORUS_SIZE = STAR_RADIUS + SIDE_LENGTH + 1;
    }

    // idk if this will be useful, just leaving it for now since it's already here
    public final static int RIGHT = 0;
    public final static int RIGHT_UP = 1;
    public final static int UP = 2;
    public final static int LEFT = 3;
    public final static int LEFT_DOWN = 4;
    public final static int DOWN = 5;

    protected final static int DIRECTIONS_NO = 6;

    protected Field[][] cells;

    @Override
    public Coordinates move(int direction, int amount, Coordinates pos) {
        if (Math.floorMod(direction, DIRECTIONS_NO) > 2) {
            amount *= -1;
        }
        int x = 0, y = 0;
        switch (Math.floorMod(direction, 3)) { // we use the symmetry to write less cases
            case 0:
                x = Math.floorMod(pos.x + amount, TORUS_SIZE);
                y = pos.y;
                break;
            case 1:
                x = Math.floorMod(pos.x + amount, TORUS_SIZE);
                y = Math.floorMod(pos.y + amount, TORUS_SIZE);
                break;
            case 2:
                x = pos.x;
                y = Math.floorMod(pos.y + amount, TORUS_SIZE);
                break;
        }
        return new Coordinates(x, y);
    }

    @Override
    public int getSize() {
        return 2 * STAR_RADIUS + 1;
    }

    @Override
    public Field getField(int x, int y) {
        return cells[Math.floorMod(x - STAR_RADIUS, TORUS_SIZE)]
                    [Math.floorMod(y - STAR_RADIUS, TORUS_SIZE)];
    }

    @Override
    public Field getField(Coordinates cor) {
        return getField(cor.x, cor.y);
    }

    @Override
    public List<Color> getColors() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        switch (playerNo) {
            case 2:
                colors.add(Color.CYAN);
                break;
            case 3:
                colors.add(Color.MAGENTA);
                colors.add(Color.BLUE);
                break;
            case 4:
                colors.add(Color.YELLOW);
                colors.add(Color.CYAN);
                colors.add(Color.BLUE);
                break;
            case 6:
                colors.add(Color.YELLOW);
                colors.add(Color.MAGENTA);
                colors.add(Color.CYAN);
                colors.add(Color.BLUE);
                colors.add(Color.GREEN);
                break;
            default:
                return null;
        }
        return colors;
    }

    public ClassicBoard(int playerNo) {
        cells = new Field[TORUS_SIZE][TORUS_SIZE];
        this.playerNo = playerNo;
    }

    @Override
    public void setup() {
        FieldFactory currentFactory = new ClassicFieldFactory(Color.NOCOLOR, false);
        cells[0][0] = currentFactory.getField(new Coordinates(0, 0));
        for (int rad = 1; rad <= HEX_RADIUS; rad++) {               // for each ring
            for (int direction = 0; direction < DIRECTIONS_NO; direction++) {
                Coordinates curr = new Coordinates(0, 0);           // starting from the center
                curr = move(direction, rad, curr);                  // move to the starting position
                for (int i = 0; i < rad; i++) {                     // create proper amount of Fields
                    cells[curr.x][curr.y] = currentFactory.getField(curr);
                    curr = move(direction + 2, 1, curr);
                }
            }
        }
        CyclicGetter<FieldFactory> cycle = new CyclicGetter<>(DIRECTIONS_NO);
        switch (playerNo) {
            case 2:
                cycle.addObject(new ClassicFieldFactory(Color.RED, true));
                cycle.addObject(new ClassicFieldFactory(Color.GREEN, false));
                cycle.addObject(new ClassicFieldFactory(Color.BLUE, false));
                cycle.addObject(new ClassicFieldFactory(Color.CYAN, true));
                cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, false));
                cycle.addObject(new ClassicFieldFactory(Color.YELLOW, false));                break;
            case 3:
                cycle.addObject(new ClassicFieldFactory(Color.RED, true));
                cycle.addObject(new ClassicFieldFactory(Color.GREEN, false));
                cycle.addObject(new ClassicFieldFactory(Color.BLUE, true));
                cycle.addObject(new ClassicFieldFactory(Color.CYAN, false));
                cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, true));
                cycle.addObject(new ClassicFieldFactory(Color.YELLOW, false));
                break;
            case 4:
                cycle.addObject(new ClassicFieldFactory(Color.RED, true));
                cycle.addObject(new ClassicFieldFactory(Color.GREEN, false));
                cycle.addObject(new ClassicFieldFactory(Color.BLUE, true));
                cycle.addObject(new ClassicFieldFactory(Color.CYAN, true));
                cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, false));
                cycle.addObject(new ClassicFieldFactory(Color.YELLOW, true));
                break;
            case 6:
                cycle.addObject(new ClassicFieldFactory(Color.RED, true));
                cycle.addObject(new ClassicFieldFactory(Color.GREEN, true));
                cycle.addObject(new ClassicFieldFactory(Color.BLUE, true));
                cycle.addObject(new ClassicFieldFactory(Color.CYAN, true));
                cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, true));
                cycle.addObject(new ClassicFieldFactory(Color.YELLOW, true));
                break;
            default:
                cycle.addObject(new ClassicFieldFactory(Color.RED, false));
                cycle.addObject(new ClassicFieldFactory(Color.GREEN, false));
                cycle.addObject(new ClassicFieldFactory(Color.BLUE, false));
                cycle.addObject(new ClassicFieldFactory(Color.CYAN, false));
                cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, false));
                cycle.addObject(new ClassicFieldFactory(Color.YELLOW, false));
        }
        cycle.addObject(new ClassicFieldFactory(Color.RED, true));
        cycle.addObject(new ClassicFieldFactory(Color.GREEN, true));
        cycle.addObject(new ClassicFieldFactory(Color.BLUE, true));
        cycle.addObject(new ClassicFieldFactory(Color.CYAN, true));
        cycle.addObject(new ClassicFieldFactory(Color.MAGENTA, true));
        cycle.addObject(new ClassicFieldFactory(Color.YELLOW, true));
        for (int rad = HEX_RADIUS + 1; rad <= STAR_RADIUS; rad++) { // for each outer ring
            for (int direction = 0; direction < DIRECTIONS_NO; direction++) {
                Coordinates curr = new Coordinates(0, 0);           // starting from the center
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

    public List<Field> getNeigboursOfDistance (Field field, int distance) {
        List<Field> list = new ArrayList<>();
        Coordinates pos = field.getPosition();
        for (int i = 0; i < DIRECTIONS_NO; i++) {
            Coordinates other = move(i, distance, pos);
            if (cells[other.x][other.y] != null) {
                list.add(cells[other.x][other.y]);
            }
        }
        return list;
    }

    @Override
    public List<Field> getNeighborsOf(Field field) {
        return getNeigboursOfDistance(field, 1);
    }

    @Override
    public List<Field> getFurtherNeighborsOf(Field field) {
        return getNeigboursOfDistance(field, 2);
    }
}
