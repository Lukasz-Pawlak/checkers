package edu.pwr.checkers.model;

import java.io.Serializable;

/** This is a simple representation of pair of numbers */
public class Coordinates implements Serializable {
    public final int x;
    public final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
