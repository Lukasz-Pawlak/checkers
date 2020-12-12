package edu.pwr.checkers.model;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ClassicBoardTest {
    @Test
    public void testGetField() {
        Board board = new ClassicBoard();
        board.setup();
        assertNotNull(board.getField(0, 0));
    }
}
