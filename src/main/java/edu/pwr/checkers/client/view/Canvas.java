package edu.pwr.checkers.client.view;

import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Field;
import edu.pwr.checkers.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private Board board;
    private BufferedImage boardLayer;
    private BufferedImage stillPiecesLayer;
    private BufferedImage movingPieceLayer;
    private Piece movingPiece;
    private Point movingPiecePosition;
    private int squareSideLength;

    Canvas(Board board) {
        this.board = board;
        boardLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        stillPiecesLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        movingPieceLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        setBackground(Color.LIGHT_GRAY);
        // handling window resize event
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvasSizeChanged();
                repaint();
            }
        });
    }

    /** This method is called when size of canvas changes */
    private void canvasSizeChanged() {
        squareSideLength = (int) Math.min(2.0 * getWidth() / 3, 2 * getHeight() / Math.sqrt(3.0));
        boardLayer = new BufferedImage(
                (int) (squareSideLength * 1.5),
                (int) (squareSideLength * Math.sqrt(3.0)  * 0.5),
                BufferedImage.TYPE_INT_ARGB);

        stillPiecesLayer = new BufferedImage(
                (int) (squareSideLength * 1.5),
                (int) (squareSideLength * Math.sqrt(3.0)  * 0.5),
                BufferedImage.TYPE_INT_ARGB);

        movingPieceLayer = new BufferedImage(
                (int) (squareSideLength * 1.5),
                (int) (squareSideLength * Math.sqrt(3.0)  * 0.5),
                BufferedImage.TYPE_INT_ARGB);

        redrawAll();
    }

    /** This method redraws all layers */
    private void redrawAll() {
        int size = board.getSize();
        int step = squareSideLength / size;
        int diam = (int) (0.8 * step);

        Graphics g = boardLayer.getGraphics();
        //super.paint(g);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Field f = board.getField(x, y);
                if (f != null) {
                    Point coords = transform(x * step, y * step);
                    coords.translate((squareSideLength) / 2 , 0);
                    g.setColor(translateColor(f.getHomeForColor()));
                    ((Graphics2D) g).setStroke(new BasicStroke(3));
                    g.drawOval(coords.x, coords.y, diam, diam);
                }
            }
        }
        redrawAllPieces();
    }

    private void redrawAllPieces() {
        int size = board.getSize();
        int step = squareSideLength / size;
        int diam = (int) (0.7 * step);

        Graphics g = stillPiecesLayer.getGraphics();
        //super.paint(g);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Field f = board.getField(x, y);
                if (f != null && f.getPiece() != null) {
                    Point coords = transform(x * step, y * step);
                    coords.translate((squareSideLength) / 2 , 0);
                    g.setColor(translateColor(f.getPiece().getColor()));
                    g.fillOval(coords.x, coords.y, diam, diam);
                }
            }
        }
        redrawMovingPiece();
    }

    private void redrawMovingPiece() {
        int size = board.getSize();
        int step = squareSideLength / size;
        int diam = (int) (0.7 * step);

        Graphics g = movingPieceLayer.getGraphics();
        //super.paint(g);

        if (movingPiece != null) {
            Point coords = transform(movingPiecePosition.x * step, movingPiecePosition.y * step);
            coords.translate((squareSideLength) / 2 , 0);
            g.setColor(translateColor(movingPiece.getColor()));
            g.fillOval(coords.x, coords.y, diam, diam);
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardLayer, 0, 0, null);
        g.drawImage(stillPiecesLayer, 0, 0, null);
        g.drawImage(movingPieceLayer, 0, 0, null);
    }

    void setBoard(Board board) {
        this.board = board;
        redrawAll();
    }

    private Color translateColor(edu.pwr.checkers.model.Color col) {
        switch (col) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case CYAN:
                return Color.CYAN;
            case GREEN:
                return Color.GREEN;
            case YELLOW:
                return  Color.YELLOW;
            case MAGENTA:
                return  Color.MAGENTA;
            case NOCOLOR:
                return Color.DARK_GRAY;
            default:
                return null;
        }
    }

    /** Linear transformation to straighten the star */
    private Point transform(int initialX, int initialY) {
        return new Point(
                (int) (initialX - 0.5 * initialY),
                (int) (Math.sqrt(3.0) * 0.5 * initialY)
        );
    }

    /** Inverse of the transformation used to straighten the star */
    private Point inverseTransform(int initialX, int initialY) {

        return new Point(
                (int) (initialX + initialY / Math.sqrt(3.0)),
                (int) (2 * initialY / Math.sqrt(3.0))           // no invSqrt in Java standards :(
        );
    }

}
