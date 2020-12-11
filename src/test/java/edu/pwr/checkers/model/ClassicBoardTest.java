package edu.pwr.checkers.model;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

// import edu.pwr.checkers.model.ClassicBoard;

public class ClassicBoardTest {
    @Test
    public void testGetField() {
        Board board = new ClassicBoard();
        board.setup();
        assertNotEquals(board.getField(0, 0), null);
    }
}
