package edu.pwr.checkers.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ClassicBoardTest {
    @Test
    public void testGetSize() {
        Board board = new ClassicBoard(6);
        //board.setup();
        assertEquals(17, board.getSize());
    }

    @Test
    public void testGetColor() {
        Board board = new ClassicBoard(-1);
        List<Color> list = board.getColors();
        assertNull(list);
        board = new ClassicBoard(4);
        list = board.getColors();
        assertNotNull(list);
        assertTrue(list.contains(Color.RED));
    }

    @Test
    public void testNeighbours() {
        ClassicBoard board = new ClassicBoard(6);
        board.setup();
        Field field = board.cells[8][8];
        Field field1 = board.cells[7][7];
        Field field2 = board.cells[10][8];
        List<Field> list1 = board.getNeighborsOf(field);
        List<Field> list2 = board.getFurtherNeighborsOf(field);
        assertNotNull(list1);
        assertNotNull(list2);
        assertTrue(list1.contains(field1));
        assertTrue(list2.contains(field2));
    }

    @Test
    public void testGetField() {
        Board board = new ClassicBoard(6);
        board.setup();
        assertNotNull(board.getField(8, 8));
        assertNotNull(board.getField(4, 8));
        assertNotNull(board.getField(3, 7));
        assertNotNull(board.getField(2, 6));
        assertNotNull(board.getField(2, 5));
        assertNotNull(board.getField(2, 4));
        Coordinates cor = new Coordinates(2, 3);
        assertNull(board.getField(cor));
        assertNull(board.getField(2, 7));
        assertNull(board.getField(2, 3));
        assertNull(board.getField(3, 8));
        assertNull(board.getField(13, 8));
        assertNull(board.getField(13, 13));
    }
    @Test
    public void testRandomFields() {
        Board board = new ClassicBoard(6);
        board.setup();
        assertEquals(Color.NOCOLOR, board.getField(8, 8).getHomeForColor());
        assertEquals(Color.NOCOLOR, board.getField(4, 4).getHomeForColor());
        assertEquals(Color.NOCOLOR, board.getField(11, 7).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(13, 9).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(14, 11).getHomeForColor());
        assertEquals(Color.GREEN, board.getField(16, 12).getHomeForColor());
        assertEquals(Color.RED, board.getField(10, 4).getHomeForColor());
        assertEquals(Color.RED, board.getField(12, 7).getHomeForColor());
        assertEquals(Color.RED, board.getField(9, 4).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(11, 14).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(12, 13).getHomeForColor());
        assertEquals(Color.BLUE, board.getField(12, 16).getHomeForColor());
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
        Board board = new ClassicBoard(4);
        board.setup();

        assertEquals(61, countColor(Color.NOCOLOR, board));
        assertEquals(10, countColor(Color.RED, board));
        assertEquals(10, countColor(Color.GREEN, board));
        assertEquals(10, countColor(Color.BLUE, board));
        assertEquals(10, countColor(Color.MAGENTA, board));
        assertEquals(10, countColor(Color.CYAN, board));
        assertEquals(10, countColor(Color.YELLOW, board));

        assertEquals(0, (board.getPiecesOfColor(Color.NOCOLOR)).size());
        assertEquals(10, (board.getPiecesOfColor(Color.RED)).size());
        assertEquals(0, (board.getPiecesOfColor(Color.GREEN)).size());
        assertEquals(10, (board.getPiecesOfColor(Color.BLUE)).size());
        assertEquals(0, (board.getPiecesOfColor(Color.MAGENTA)).size());
        assertEquals(10, (board.getPiecesOfColor(Color.CYAN)).size());
        assertEquals(10, (board.getPiecesOfColor(Color.YELLOW)).size());
    }

    private int countColor (Color color, Board board) {
        board.setup();
        int count = 0;
        for (int x = 0; x < 17; x++) {
            for (int y = 0; y < 17; y++) {
                if (board.getField(x, y) != null && board.getField(x, y).getHomeForColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }
}
