package edu.pwr.checkers.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

public class Canvas extends JPanel {

    Canvas() {
        // handling window resize event
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // todo: redrawing the graphics
                // just for now, testing if it gets invoked
                Random rd = new Random();
                setBackground(new Color(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255)));
            }
        });
    }
}
