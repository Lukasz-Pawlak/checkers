package edu.pwr.checkers.model;

import java.util.List;

public interface Board {
    void setup();
    List<Field> getNeighborsOf(Field field);
}
