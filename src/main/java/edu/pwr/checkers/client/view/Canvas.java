package edu.pwr.checkers.client.view;

import edu.pwr.checkers.Logger;
import edu.pwr.checkers.client.Controller;
import edu.pwr.checkers.model.Board;
import edu.pwr.checkers.model.Coordinates;
import edu.pwr.checkers.model.Field;
import edu.pwr.checkers.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * This class represents canvas on which Board is displayed.
 * Is capable of displaying any hexagonal grid based Board object.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class Canvas extends JPanel {
    /** Board displayed. */
    private Board board;
    /** Image representing empty board. */
    private BufferedImage boardLayer;
    /** Image representing pieces that are not currently being moved. */
    private BufferedImage stillPiecesLayer;
    /** Image representing piece that is currently being moved.*/
    private BufferedImage movingPieceLayer;
    /** Clear image. */
    private BufferedImage CLEAR;
    /** Piece currently being moved.*/
    private Piece movingPiece;
    /** Position on screen of moving piece relative to this panel */
    private Point movingPiecePosition;
    /** Side in pixels of maximal square whose transformation fits in this panel */
    private int squareSideLength;
    /** Size in pixels of representation on screen of cell in Fields array */
    private int stepSize;
    /** this.board.getSize() */
    private int boardSize;
    /** Controller object used to communicate with model and client. */
    private final Controller controller;

    /**
     * The only constructor.
     * @param controller controller object to be used.
     */
    public Canvas(Controller controller) {
        this.controller = controller;
        movingPiece = null;
        //initialPosition = null;
        setBackground(Color.LIGHT_GRAY);
    }

    public void init() {
        if (board != null) Logger.debug("Pobrano board prawidłowo");

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
        canvasSizeChanged();
    }

    /**
     * This method is called whenever mouse is pressed.
     * It tries to grab piece on top of which mouse cursor was.
     * If no such piece exists, null is returned.
     * Otherwise grabbed piece is returned and field on which it
     * was standing is marked as empty (but piece still remembers
     * the field it was standing on before).
     * @param point coordinates on panel under which piece should be seeked.
     * @return grabbed piece of null when not found.
     */
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
                //initialPosition = field;
                return piece;
            }
        }
        return null;
    }

    /**
     * This method is called whenever muse is released.
     * It tries to put moving piece on field located under the point.
     * If no piece is currently being moved, it does nothing.
     * If piece was released over void, it will be put back to it's
     * original position (before it was grabbed). Otherwise it will
     * send move request to the server to check if move is legal.
     * If it is, piece will be put on new position. If it isn't, it will
     * return to the original position.
     * @param point point of mouse release.
     */
    private void putMovingPiece(Point point) {
        if (movingPiece == null)
            return;

        //super.paint(movingPieceLayer.getGraphics());        // dunno why but it is needed; like it needs to be called early enough
        point.translate(- squareSideLength / 2, 0);
        point = inverseTransform(point.x, point.y);

        Coordinates coords = new Coordinates(point.x / stepSize, point.y / stepSize);
        if (coords.x < boardSize && coords.y < boardSize && coords.x >= 0 && coords.y >= 0) {
            Field field = board.getField(coords.x, coords.y);
            if (field != null) {
                controller.movePiece(movingPiece, coords);
                /*
                if (!controller.movePiece(movingPiece, coords)) { // this references to the server already
                    //movingPiece.setField(initialPosition);
                    //initialPosition.setPiece(movingPiece);
                }
                else {
                    Logger.debug("canvas: putMovingPiece: setting piece's field");
                    Logger.debug("\twas: " + movingPiece.getField().toString());
                    field.setPiece(movingPiece);
                    movingPiece.setField(field);
                    Logger.debug("\tis: " + movingPiece.getField().toString());
                }*/
            }
            else {
                //movingPiece.setField(initialPosition);
                //initialPosition.setPiece(movingPiece);
                movingPiece.getField().setPiece(movingPiece);
            }
        }
        else {
            //movingPiece.setField(initialPosition);
            //initialPosition.setPiece(movingPiece);
            movingPiece.getField().setPiece(movingPiece);
        }
        movingPiece = null;
        //initialPosition = null;
    }

    /**
     * This method is called when size of canvas changes.
     * It redraws images representing state of the board.
     */
    public void canvasSizeChanged() {
        squareSideLength = (int) Math.min(2.0 * getWidth() / 3, 2 * getHeight() / Math.sqrt(3.0));
        int width = (int) (squareSideLength * 1.5);
        int height = (int) (squareSideLength * Math.sqrt(3.0)  * 0.5);
        int type = BufferedImage.TYPE_INT_ARGB;

        boardLayer = new BufferedImage(width, height, type);
        stillPiecesLayer = new BufferedImage(width, height, type);
        movingPieceLayer = new BufferedImage(width, height, type);
        CLEAR = new BufferedImage(width, height, type);

        redrawAll();
    }

    /**
     * This method redraws all layer images.
     */
    public void redrawAll() {
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

    /**
     * This method redraws layer images containing pieces.
     */
    private void redrawAllPieces() {
        Graphics g = stillPiecesLayer.getGraphics();
        super.paint(g);

        stillPiecesLayer.setData(CLEAR.getRaster());

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

    /**
     * This method redraws layer image containing moving piece.
     */
    public void redrawMovingPiece() {
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


    /**
     * @inheritDoc
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardLayer, 0, 0, null);
        g.drawImage(stillPiecesLayer, 0, 0, null);
        g.drawImage(movingPieceLayer, 0, 0, null);
    }

    /**
     * Board setter.
     * After board is set, everything is redrawn.
     * @param board non null new board to be set.
     */
    void setBoard(Board board) {
        this.board = board;
        //redrawAllPieces();
    }

    /**
     * Function converting enum Color to awt Color object.
     * @param col color to be translated.
     * @return translated color.
     */
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

    /**
     * Linear transformation used to straighten the board.
     */
    private Point transform(int initialX, int initialY) {
        return new Point(
                (int) (initialX - 0.5 * initialY),
                (int) (Math.sqrt(3.0) * 0.5 * initialY)
        );
    }

    /**
     * Inverse of the transformation used to straighten the board.
     */
    private Point inverseTransform(int initialX, int initialY) {
        return new Point(
                (int) (initialX + (initialY / Math.sqrt(3.0))),
                (int) (2 * initialY / Math.sqrt(3.0))           // no invSqrt in Java standards :(
        );
    }

}
