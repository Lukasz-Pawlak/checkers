package edu.pwr.checkers.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassicBoardTest {
    @Test
    public void testGetField() {
        Board board = new ClassicBoard();
        board.setup();
        assertNotNull(board.getField(8, 8));
        assertNotNull(board.getField(4, 8));
        assertNotNull(board.getField(3, 7));
        assertNotNull(board.getField(2, 6));
        assertNotNull(board.getField(2, 5));
        assertNotNull(board.getField(2, 4));
        assertNull(board.getField(2, 3));
        assertNull(board.getField(2, 7));
        assertNull(board.getField(2, 3));
        assertNull(board.getField(3, 8));
        assertNull(board.getField(13, 8));
        assertNull(board.getField(13, 13));
    }
    @Test
    public void testRandomFields() {
        Board board = new ClassicBoard();
        board.setup();
        assertEquals(Color.NOCOLOR, board.getField(8, 8).getHomeForColor());
        assertEquals(Color.NOCOLOR, board.getField(4, 4).getHomeForColor());
        assertEquals(Color.NOCOLOR, board.getField(11, 7).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(13, 9).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(0, 11).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(2, 12).getHomeForColor());
        assertEquals(Color.RED, board.getField(10, 4).getHomeForColor());
        assertEquals(Color.RED, board.getField(12, 7).getHomeForColor());
        assertEquals(Color.RED, board.getField(9, 4).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(11, 0).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(12, 13).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(12, 2).getHomeForColor());
        assertEquals(Color.CYAN, board.getField(6, 11).getHomeForColor());
        assertEquals(Color.CYAN, board.getField(7, 12).getHomeForColor());
        assertEquals(Color.CYAN, board.getField(4, 9).getHomeForColor());
        assertEquals(Color.MAGENTA, board.getField(0, 4).getHomeForColor());
        assertEquals(Color.MAGENTA, board.getField(3, 7).getHomeForColor());
        assertEquals(Color.MAGENTA, board.getField(2, 6).getHomeForColor());
        assertEquals(Color.YELLOW, board.getField(4, 0).getHomeForColor());
        assertEquals(Color.YELLOW, board.getField(7, 3).getHomeForColor());
        assertEquals(Color.YELLOW, board.getField(5, 2).getHomeForColor());
    }

    @Test
    public void testQuantity() {
        assertEquals(61, countColor(Color.NOCOLOR));
        assertEquals(10, countColor(Color.RED));
        assertEquals(10, countColor(Color.GREEN));
        assertEquals(10, countColor(Color.BLUE));
        assertEquals(10, countColor(Color.MAGENTA));
        assertEquals(10, countColor(Color.CYAN));
        assertEquals(10, countColor(Color.YELLOW));
    }

    private int countColor (Color color) {
        Board board = new ClassicBoard();
        board.setup();
        int count = 0;
        for (int x = 0; x < 14; x++) {
            for (int y = 0; y < 14; y++) {
                if (board.getField(x, y) != null && board.getField(x, y).getHomeForColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }
}
