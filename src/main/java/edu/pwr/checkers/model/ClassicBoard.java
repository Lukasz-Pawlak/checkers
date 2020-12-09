package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class ClassicBoard implements Board {
    protected static int SIDE_LENGTH = 5;       // left not final for inheritance
    protected final static int HEX_RADIUS;
    protected final static int STAR_RADIUS;
    protected final static int TORUS_SIZE;

    static {
        HEX_RADIUS = SIDE_LENGTH - 1;
        STAR_RADIUS = HEX_RADIUS * 2;
        TORUS_SIZE = STAR_RADIUS + SIDE_LENGTH + 1;
    }

    public final static int RIGHT = 0;
    public final static int RIGHT_DOWN = 1;
    public final static int LEFT_DOWN = 2;
    public final static int LEFT = 3;
    public final static int LEFT_UP = 4;
    public final static int RIGHT_UP = 5;

    protected Field[][] cells;

    static protected class ClassicMove extends Board.Move {
        @Override
        Coordinates move(int direction, int amount, Coordinates pos) {
            if (Math.floorMod(direction, 6) > 2) {
                amount *= -1;
            }
            int x = 0, y = 0;
            switch (Math.floorMod(direction, 3)) {
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
                    pos.y = Math.floorMod(pos.y + amount, TORUS_SIZE);
                    break;
            }
            return new Coordinates(x, y);
        }
    }

    protected final ClassicMove movePattern;

    public ClassicBoard() {
        cells = new Field[TORUS_SIZE][TORUS_SIZE];
        movePattern = new ClassicMove();
    }

    @Override
    public void setup() {
        // TODO: write Factory for Field, and substitute nulls below with Factory requests
        cells[0][0] = null;
        for (int rad = 1; rad <= HEX_RADIUS; rad++) {               // for each ring
            for (int direction = 0; direction < 6; direction++) {   // for each direction
                Coordinates curr = new Coordinates(0, 0);           // starting from the center
                movePattern.move(direction, rad, curr);             // move to the starting position
                for (int i = 0; i < rad; i++) {                     // create proper amount of Fields
                    cells[curr.x][curr.y] = null;
                    movePattern.move(direction + 2, 1, curr);
                }
            }
        }
        // TODO: write triangles part
    }

    @Override
    public List<Field> getNeighborsOf(Field field) {
        List<Field> list = new ArrayList<>();
        Coordinates pos = field.getPosition();
        for (int i = 0; i < 6; i++) {
            Coordinates other = movePattern.move(i, 1, pos);
            if (cells[other.x][other.y] != null) {
                list.add(cells[other.x][other.y]);
            }
        }
        return list;
    }
}
