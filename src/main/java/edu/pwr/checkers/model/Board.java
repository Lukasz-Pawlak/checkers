package edu.pwr.checkers.model;

import java.io.Serializable;
import java.util.List;

/**
 * Board object represents the board on which game takes place.
 * Boards might be of various size and shape, but all must implement
 * this interface.
 * Implementation should provide constructor which takes a parameter of
 * number of players.
 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public interface Board extends Serializable {

    /**
     * This method sets up everything in Board, should be called
     * before first use of Board object.
     */
    void setup();

    /**
     * This method should return list of all neighbouring fields of given
     * Field. Implementation of Board decides which fields are neighbours.
     * @param field Field whose neighbours are needed.
     * @return List of neighbours of given field.
     */
    List<Field> getNeighborsOf(Field field);

    /**
     * This method should return list of all fields that lie 2 fields away
     * from given Field. Should return only those, to which a piece could
     * jump, so implementation of it depends on implementation of {@link Game}.
     * @param field Field whose further neighbours are needed.
     * @return List of further neighbours of given field.
     */
    List<Field> getFurtherNeighborsOf(Field field);

    /**
     * This method is suggested to be used internally in Board implementation.
     * It calculates position which lies exactly amount single steps away from
     * initial position, in direction denoted by firs argument.
     * @param direction number of direction in which move must be calculated.
     * @param amount amount of single steps.
     * @param pos initial position.
     * @return calculated new position.
     */
    Coordinates move(int direction, int amount, Coordinates pos);

    /**
     * This method should return side length of square in which
     * whole board can be inscribed.
     * @return side length of square in which whole board can be inscribed.
     */
    int getSize();

    /**
     * This method should return Field on specified position on board in cartesian
     * coordinates, or null when on given position there is no field.
     * @return field on given position.
     * @param x x coordinate
     * @param y y coordinate
     */
    Field getField(int x, int y);

    /**
     * This method should return Field on specified position on board in cartesian
     * coordinates, or null when on given position there is no field.
     * @return field on given position.
     * @param cor position.
     */
    Field getField(Coordinates cor);

    /**
     * This function should return list of colors optimal for number of players
     * specified in constructor.
     * @return List of colors.
     */
    List<Color> getColors();

    /**
     * This function should return all pieces of given color on board.
     * @param color the color.
     * @return all pieces of given color.
     */
    List<Piece> getPiecesOfColor(Color color);
}
