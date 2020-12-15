package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Controller;
import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Field;
import edu.pwr.checkers.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private Board board;
    private BufferedImage boardLayer;
    private BufferedImage stillPiecesLayer;
    private BufferedImage movingPieceLayer;
    private Piece movingPiece;
    /** Position on screen of moving piece relative to this panel */
    private Point movingPiecePosition;
    /** Side in pixels of maximal square whose transformation fits in this panel */
    private int squareSideLength;
    /** Size in pixels of representation on screen of cell in Fields array */
    private int stepSize;
    /** this.board.getSize() */
    private int boardSize;
    private Controller controller;

    Canvas(Controller controller) {
        this.controller = controller;
        this.board = controller.getBoard();
        boardLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        stillPiecesLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        movingPieceLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        movingPiece = null;
        setBackground(Color.LIGHT_GRAY);
        // handling window resize event
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvasSizeChanged();
                repaint();
            }
        });

        // handling mouse events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (movingPiece == null) {
                    movingPiece = tryToGetPiece(e.getPoint());
                }
                if (movingPiece != null) {
                    movingPiecePosition = e.getPoint();
                    redrawAllPieces();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (movingPiece != null) {
                    putMovingPiece(e.getPoint());
                    redrawAllPieces();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (movingPiece != null) {
                    movingPiecePosition = e.getPoint();
                    redrawMovingPiece();
                }
            }
        });
    }

    private Piece tryToGetPiece(Point point) {
        super.paint(stillPiecesLayer.getGraphics());
        point.translate(- squareSideLength / 2, 0);
        point = inverseTransform(point.x, point.y);
        Coordinates coords = new Coordinates(point.x / stepSize, point.y / stepSize);
        if (coords.x < boardSize && coords.y < boardSize && coords.x >= 0 && coords.y >= 0) {
            Field field = board.getField(coords.x, coords.y);
            if (field != null) {
                Piece piece = field.getPiece();
                field.setPiece(null);
                return piece;
            }
        }
        return null;
    }

    private void putMovingPiece(Point point) {
        super.paint(movingPieceLayer.getGraphics());        // dunno why but it is needed; like it needs to be called early enough
        point.translate(- squareSideLength / 2, 0);
        point = inverseTransform(point.x, point.y);

        Coordinates coords = new Coordinates(point.x / stepSize, point.y / stepSize);
        if (coords.x < boardSize && coords.y < boardSize && coords.x >= 0 && coords.y >= 0) {
            Field field = board.getField(coords.x, coords.y);
            if (field != null && controller.movePiece(movingPiece, coords)) { // this references to the server already
                field.setPiece(movingPiece);
                movingPiece.setField(field);
            }
            else {
                movingPiece.getField().setPiece(movingPiece);
            }
        }
        else {
            movingPiece.getField().setPiece(movingPiece);
        }
        movingPiece = null;
    }

    /** This method is called when size of canvas changes */
    private void canvasSizeChanged() {
        squareSideLength = (int) Math.min(2.0 * getWidth() / 3, 2 * getHeight() / Math.sqrt(3.0));
        int width = (int) (squareSideLength * 1.5);
        int height = (int) (squareSideLength * Math.sqrt(3.0)  * 0.5);
        int type = BufferedImage.TYPE_INT_ARGB;

        boardLayer = new BufferedImage(width, height, type);
        stillPiecesLayer = new BufferedImage(width, height, type);
        movingPieceLayer = new BufferedImage(width, height, type);

        redrawAll();
    }

    /** This method redraws all layers */
    private void redrawAll() {
        Graphics g = boardLayer.getGraphics();
        super.paint(g);

        boardSize = board.getSize();
        stepSize = squareSideLength / boardSize;
        int diam = (int) (0.8 * stepSize);

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Field f = board.getField(x, y);
                if (f != null) {
                    Point coords = transform(x * stepSize, y * stepSize);
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
        Graphics g = stillPiecesLayer.getGraphics();
        super.paint(g);

        int diam = (int) (0.7 * stepSize);

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Field f = board.getField(x, y);
                if (f != null && f.getPiece() != null) {
                    Point coords = transform(x * stepSize, y * stepSize);
                    coords.translate((squareSideLength) / 2 , 0);
                    g.setColor(translateColor(f.getPiece().getColor()));
                    g.fillOval(coords.x, coords.y, diam, diam);
                }
            }
        }
        redrawMovingPiece();
    }

    private void redrawMovingPiece() {
        int diam = (int) (0.7 * stepSize);

        Graphics g = movingPieceLayer.getGraphics();
        super.paint(g);

        if (movingPiece != null) {
            Point coords = movingPiecePosition;
            g.setColor(translateColor(movingPiece.getColor()));
            g.fillOval(coords.x - diam / 2, coords.y - diam / 2, diam, diam);
        }
        repaint();
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
                (int) (initialX + (initialY / Math.sqrt(3.0))),
                (int) (2 * initialY / Math.sqrt(3.0))           // no invSqrt in Java standards :(
        );
    }

}
