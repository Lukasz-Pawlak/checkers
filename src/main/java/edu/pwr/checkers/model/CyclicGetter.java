package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class CyclicGetter<T> {
    protected List<T> ring;
    private int remainingSpace;
    private final int noObjects;
    private int current;

    CyclicGetter(int noObjects) {
        this.noObjects = noObjects;
        ring = new ArrayList<>();
        current = 0;
        remainingSpace = noObjects;
    }

    boolean addObject(T obj) {
        if (remainingSpace > 0) {
            remainingSpace--;
            ring.add(obj);
            current++;
            return true;
        }
        return false;
    }

    boolean remove(T obj) {
        return ring.remove(obj);
    }

    T get(int i) {
        return ring.get(i);
    }

    T getNext() {
        if (remainingSpace > 0) {
            return null;
        }
        current = Math.floorMod(current + 1, noObjects);
        return  ring.get(current);
    }
}
