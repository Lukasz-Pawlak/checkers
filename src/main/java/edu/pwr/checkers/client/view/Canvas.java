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

    Canvas(Board board) {
        this.board = board;
        boardLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        stillPiecesLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        movingPieceLayer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        setBackground(Color.WHITE);
        // handling window resize event
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redrawAll();
                repaint();
            }
        });
    }

    private void redrawAll() {
        int minSize = Math.min(getWidth(), getHeight());
        boardLayer = new BufferedImage(minSize, minSize,  BufferedImage.TYPE_INT_ARGB);

        int size = board.getSize();
        int panelSize = Math.min(getWidth(), getHeight());
        int step = panelSize / size;
        int outerDiam = (int) (0.8 * step);

        Graphics g = boardLayer.getGraphics();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Field f = board.getField(x, y);
                if (f != null) {
                    g.setColor(translateColor(f.getHomeForColor()));
                    ((Graphics2D) g).setStroke(new BasicStroke(3));
                    g.drawOval(x * step, y * step, outerDiam, outerDiam);
                }
            }
        }
        redrawStillPieces();
    }

    private void redrawStillPieces() {
        int minSize = Math.min(getWidth(), getHeight());
        stillPiecesLayer = new BufferedImage(minSize, minSize,  BufferedImage.TYPE_INT_ARGB);

        int size = board.getSize();
        int panelSize = Math.min(getWidth(), getHeight());
        int step = panelSize / size;
        int outerDiam = (int) (0.7 * step);

        Graphics g = stillPiecesLayer.getGraphics();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Field f = board.getField(x, y);
                if (f != null && f.getPiece() != null) {
                    g.setColor(translateColor(f.getPiece().getColor()));
                    g.fillOval(x * step, y * step, outerDiam, outerDiam);
                }
            }
        }
    }

    //*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardLayer, 0, 0, null);
        g.drawImage(stillPiecesLayer, 0, 0, null);
        g.drawImage(movingPieceLayer, 0, 0, null);
    }//*/


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
}
