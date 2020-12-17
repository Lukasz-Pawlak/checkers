package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class CyclicGetter<T> {
    protected List<T> ring;
    private int remainingSpace;
    private final int noObjects;
    private int current;

    public CyclicGetter(int noObjects) {
        this.noObjects = noObjects;
        ring = new ArrayList<>();
        current = 0;
        remainingSpace = noObjects;
    }

    public boolean addObject(T obj) {
        if (remainingSpace > 0) {
            remainingSpace--;
            ring.add(obj);
            current++;
            return true;
        }
        return false;
    }

    public boolean remove(T obj) {
        return ring.remove(obj);
    }

    public T get(int i) {
        return ring.get(i);
    }

    public boolean setCurrent(int n) {
        if (n >= 0 && n < noObjects && remainingSpace == 0) {
            current = n;
            return true;
        }
        return false;
    }

    public T getNext() {
        if (remainingSpace > 0) {
            return null;
        }
        current = Math.floorMod(current + 1, noObjects);
        return  ring.get(current);
    }
}
