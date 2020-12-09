package edu.pwr.checkers.model;

public class CyclicFieldFactory {
    protected FieldFactory[] factories;
    private int remainingSpace;
    private final int size;
    private int current;

    CyclicFieldFactory(int noFactories) {
        size = noFactories;
        factories = new FieldFactory[size];
        current = 0;
        remainingSpace = noFactories;
    }

    boolean addFactory(FieldFactory factory) {
        if (remainingSpace > 0) {
            remainingSpace--;
            factories[current] = factory;
            current++;
            return true;
        }
        return false;
    }

    FieldFactory getNextFactory() {
        if (remainingSpace > 0) {
            return null;
        }
        current = Math.floorMod(current + 1, size);
        return  factories[current];
    }
}
