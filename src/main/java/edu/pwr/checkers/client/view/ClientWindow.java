package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Controller;
import edu.pwr.checkers.model.Board;
import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame {
    private final Canvas canvas;
    private final JTextArea messageBox;
    private final SidePanel sidePanel;
    private final Controller controller;

    public ClientWindow(Controller controller) {
        super("Chinese Checkers");
        setBounds(300, 200, 800, 400);
        setResizable(true);
        setMinimumSize(new Dimension(600, 400));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.controller = controller;

        canvas = new Canvas(controller);
        messageBox = new JTextArea();
        sidePanel = new SidePanel(controller);

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
