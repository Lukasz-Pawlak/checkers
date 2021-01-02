package edu.pwr.checkers.model;

import java.io.Serializable;

/**
 * This is a simple representation of pair of numbers.
 * Used to refer to position in square in which board is inscribed.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class Coordinates implements Serializable {
    /** X coordinate. */
    public final int x;
    /** Y coordinate. */
    public final int y;

    /**
     * The only constructor.
     * Sets internal x and y, which are public and final.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
