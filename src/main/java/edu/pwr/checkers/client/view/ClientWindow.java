package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Controller;
import edu.pwr.checkers.model.Board;
import javax.swing.*;
import java.awt.*;

/**
 * This class represents GUI window on client's side.
 * Typically, only one object of this kind is created
 * per one client.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class ClientWindow extends JFrame {
    /** Canvas object used to display Board. */
    private final Canvas canvas;
    /** Message box used to display server info. */
    private final JTextArea messageBox;
    /** Side Panel containing basic control buttons & status box */
    private final SidePanel sidePanel;

    /**
     * The only constructor.
     * @param controller Controller object to communicate with
     *                   model and client.
     */
    public ClientWindow(Controller controller) {
        super("Chinese Checkers");
        setBounds(300, 200, 800, 400);
        setResizable(true);
        setMinimumSize(new Dimension(600, 400));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    /**
     * This method sets status of the game in side panel.
     * @param status status to be set.
     */
    public void setStatus(String status) {
        sidePanel.setStatus(status);
    }

    /**
     * This method sets message send by server, typically
     * telling about some error.
     * @param msg message to be set.
     */
    public void setMessage(String msg) {
        messageBox.setText(msg);
    }

    /**
     * This method sets board used by canvas.
     * @param board board to bee set.
     */
    public void useThisBoard(Board board) {
        canvas.setBoard(board);
    }

    /**
     * This method refreshes the view.
     */
    //TODO: is it even needed?
    public void refresh() {
        canvas.refresh(getWidth(), getHeight());
    }
}
