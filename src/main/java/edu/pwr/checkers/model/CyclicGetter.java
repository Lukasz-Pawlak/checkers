package edu.pwr.checkers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provide basic utility of cyclic linked list.
 * Caution: getter has fixed object count and order.
 * Attempt of using it when not all object are set will
 * result in failure described in methods.
 * @param <T> object type on which getter will work.
 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public class CyclicGetter<T> {
    /** Data structure in which T objects are stored. */
    protected List<T> ring;
    /** Number indicating how much space is not yet used. */
    private int remainingSpace;
    /** Number of objects that should be stored in this getter. */
    private final int noObjects;
    /** The index of current object (last returned). */
    private int current;

    /**
     * The only constructor.
     * @param noObjects number of objects this getter will hold.
     */
    public CyclicGetter(int noObjects) {
        this.noObjects = noObjects;
        ring = new ArrayList<>(noObjects);
        current = 0;
        remainingSpace = noObjects;
    }

    /**
     * The method to get just the list of objects.
     * @return list of objects.
     */
    public List<T> getRing() {
        return ring;
    }

    /**
     * This method adds object to last currently position in the
     * getter. On success true is returned. If the getter is full
     * no action is performed and false is returned.
     * @param obj object ot be added.
     * @return whether adding was successful.
     */
    public boolean addObject(T obj) {
        if (remainingSpace > 0) {
            remainingSpace--;
            ring.add(obj);
            current++;
            return true;
        }
        return false;
    }

    /**
     * This factory method creates new CyclicGetter from a old one.
     * New one will differ exactly by old one's current element.
     * New one will be smaller in size by one.
     * Other element have to be full (if addObject on other was called,
     * false should be returned), otherwise undefined behaviour may occur.
     * @param other cyclic getter to truncate. Have to be at least 1 in size.
     * @param <E> the type of objects in other cyclic getter.
     * @return new CyclicGetter that is a subset of other which does not
     *  contain other's current object.
     */
    public static <E> CyclicGetter<E> truncateCurrent(CyclicGetter<E> other) {
        CyclicGetter<E> newOne = new CyclicGetter<E>(other.noObjects - 1);
        for (int i = 0; i < newOne.noObjects; i++) {
            newOne.addObject(other.getNext());
        }
        return newOne;
    }

    /**
     * This function returns element on ith position in underlying list.
     * Caution: the method does not validate input, so runtime
     * ArrayIndexOutOfBoundsException may be thrown.
     * @param i position in list.
     * @return Element on ith position.
     */
    public T get(int i) {
        return ring.get(i);
    }

    /**
     * This method sets current element to nth from the list.
     * @param n number of new current element.
     * @return whether method failed due to n being out of bounds
     */
    public boolean setCurrent(int n) {
        if (n >= 0 && n < noObjects && remainingSpace == 0) {
            current = n;
            return true;
        }
        return false;
    }

    /**
     * This is the core method of this class, the cyclic getter.
     * Returns next element from the list. If last returned element was
     * the last one, first element is returned, closing the cycle.
     * @return next element in the cycle or null, when cycle hasn't
     * been filled up yet.
     */
    public T getNext() {
        if (remainingSpace > 0) {
            return null;
        }
        current = Math.floorMod(current + 1, noObjects);
        return  ring.get(current);
    }
}
