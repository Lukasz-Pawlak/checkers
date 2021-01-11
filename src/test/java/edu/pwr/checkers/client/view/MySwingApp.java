package edu.pwr.checkers.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import processing.core.PApplet;

// source:
// https://forum.processing.org/two/discussion/3716/how-to-integrate-java-swing-and-papplet
// but it doesn't work :(
public class MySwingApp extends JFrame{

    JButton btnMakeWindow;
    ControlFrame cf;

    public MySwingApp(){
        this.setLayout(new BorderLayout());

        btnMakeWindow = new JButton("Open Window");
        btnMakeWindow.addActionListener(e -> cf = new ControlFrame("My Random Circles", 400,400));
        add(btnMakeWindow, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    public class ControlFrame extends JFrame {
        private ControlApplet papplet;
        private JButton btnCircles;

        public ControlFrame(String title, int w, int h) {
            this.setLayout(new BorderLayout());
            papplet = new ControlApplet();
            // So we can resize the frame to get the sketch canvas size reqd.
            papplet.frame = this;
            setResizable(true);
            setTitle(title);
            setLocation(100, 100);
            papplet.appWidth = w;
            papplet.appHeight = h;

            // Set the papplet size preferences
            papplet.resize(papplet.appWidth, papplet.appHeight);
            papplet.setPreferredSize(new Dimension(papplet.appWidth, papplet.appHeight));
            papplet.setMinimumSize(new Dimension(papplet.appWidth, papplet.appHeight));
            add(papplet, BorderLayout.CENTER);
            papplet.init();

            btnCircles = new JButton("Different Circles");
            btnCircles.addActionListener(e -> papplet.redraw());
            add(btnCircles, BorderLayout.SOUTH);
            pack();
            setVisible(true);
        }
    }

    public class ControlApplet extends PApplet {

        public int appWidth, appHeight;

        public void setup() {
            size(appWidth, appHeight);
            noLoop();
        }

        public void draw() {
            background(200,255,200);
            fill(255, 100, 100);
            stroke(64,0,0);
            strokeWeight(2);
            for(int i = 0; i < 5; i++){
                float x = (float) (Math.random() * width);
                float y = (float) (Math.random() * height);
                float r = (float) (Math.random() * 30 + 20);
                ellipse(x,y,r,r);
            }
        }
    }

    public static void main(String[] args) {
        new MySwingApp();
    }
}