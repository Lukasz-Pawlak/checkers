package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidePanel extends JPanel {
    private final JButton giveUpButton;
    private final JButton cancelButton;
    private final JButton acceptButton;
    private final JTextArea statusBox;
    private final Controller controller;

    SidePanel(Controller controller) {
        // setPreferredSize(new Dimension(140, 300));
        this.controller = controller;

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

    void disableButtons() {
        toggleAllButtonsState(false);
    }

    void enableButtons() {
        toggleAllButtonsState(true);
    }

    private void toggleAllButtonsState(boolean state) {
        acceptButton.setEnabled(state);
        cancelButton.setEnabled(state);
        giveUpButton.setEnabled(state);
    }

    void setStatus(String status) {
        statusBox.setText(status);
    }
}
