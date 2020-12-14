package edu.pwr.checkers.client.view;

import edu.pwr.checkers.model.Board;
import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame {
    private final Canvas canvas;
    private final JTextArea messageBox;
    private final SidePanel sidePanel;

    public ClientWindow(Board board) {
        super("Chinese Checkers");
        setBounds(300, 200, 600, 400);
        setResizable(true);
        setMinimumSize(new Dimension(600, 400));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas(board);
        messageBox = new JTextArea();
        sidePanel = new SidePanel();

        messageBox.setEditable(false);

        add(sidePanel, BorderLayout.EAST);
        add(canvas, BorderLayout.CENTER);
        add(messageBox, BorderLayout.NORTH);

        messageBox.setText("test");
        sidePanel.setStatus("to jest status");

        sidePanel.disableButtons();
        sidePanel.enableButtons();

        setVisible(true);
    }

    public void setStatus(String status) {
        sidePanel.setStatus(status);
    }

    public void setMessage(String msg) {
        messageBox.setText(msg);
    }

    public void useThisBoard(Board board) {
        canvas.setBoard(board);
    }
}
