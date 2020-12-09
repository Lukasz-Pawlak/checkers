package edu.pwr.checkers.model;

import java.util.List;

public interface Board {
    /**
     * This method sets up everything in Board, should be called
     * before first use of Board object.
     */
    void setup();
    List<Field> getNeighborsOf(Field field);
    Coordinates move(int direction, int amount, Coordinates pos); // TODO: maybe this should be done differently, for example using abstract class
    /**
     * This method should return side length of square in which
     * whole board can be inscribed.
     */
    int getSize();
    Field getField(int x, int y);
}
