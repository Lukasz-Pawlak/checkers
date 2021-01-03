package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents side panel visible in GUI.
 * It contains status message box, as well as 3 buttons labeled
 * 'Give up turn', 'Cancel move' & 'Accept move'.
 * @version 1.0
 * @author Łukasz Pawlak
 */
public class SidePanel extends JPanel {
    /** The 'Give up turn' button. */
    private final JButton giveUpButton;
    /** The 'Cancel move' button. */
    private final JButton cancelButton;
    /** The 'Accept move' button. */
    private final JButton acceptButton;
    /** The status message box. */
    private final JTextArea statusBox;

    /**
     * The only constructor.
     * @param controller Controller object to communicate with
     *                   model and client.
     */
    SidePanel(Controller controller) {
        // setPreferredSize(new Dimension(140, 300));

        statusBox = new JTextArea();
        statusBox.setEditable(false);
        add(statusBox);

        giveUpButton = new JButton("Give up turn");
        giveUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.sendCancelMoveRequest();
                controller.sendAcceptMoveRequest();
            }
        });
        add(giveUpButton);

        acceptButton = new JButton("Confirm move");
        acceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.sendAcceptMoveRequest();
            }
        });
        add(acceptButton);

        cancelButton = new JButton("Cancel last move");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.sendCancelMoveRequest();
            }
        });
        add(cancelButton);

        int[] ratios = {6, 1, 1, 1};
        setLayout(new SplitLayout(ratios, false));
    }

    /**
     * Method disabling usage of buttons.
     */
    void disableButtons() {
        toggleAllButtonsState(false);
    }

    /**
     * Method enabling usage of buttons.
     */
    void enableButtons() {
        toggleAllButtonsState(true);
    }

    /**
     * This method calls setEnabled() method on all of the buttons,
     * with argument passed as argument to this method. Used by
     * {@link SidePanel#enableButtons()} & {@link SidePanel#disableButtons()}.
     * @param state argument to be passed to all setEnabled() functions.
     */
    private void toggleAllButtonsState(boolean state) {
        acceptButton.setEnabled(state);
        cancelButton.setEnabled(state);
        giveUpButton.setEnabled(state);
    }

    /**
     * This method sets the message of status box.
     * @param status status message to be set.
     */
    void setStatus(String status) {
        statusBox.setText(status);
    }
}
