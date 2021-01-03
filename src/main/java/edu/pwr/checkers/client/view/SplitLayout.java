package edu.pwr.checkers.client.view;

import java.awt.*;

/**
 * Layout Manger enabling uneven horizontal or vertical distribution of components.
 * Displays the exact number of components as length of param ratios in constructor.
 * They are displayed based on ratios passed in constructor.
 * CAUTION: if not exactly ratios.length components are present ArrayIndexOutOfBoundsException may be thrown.
 * @version 1.1
 * @author Łukasz Pawlak
 */
class SplitLayout implements LayoutManager {
    /** Array of ratios */
    private final int[] ratios;
    /** Is split horizontal (false means vertical split) */
    private final boolean horizontal;
    /** Sum of ratios array */
    private final int sum;

    /**
     * The only constructor.
     * @param ratios parts od whole space (sum of this array) given component will occupy
     * @param horizontal is split horizontal
     */
    public SplitLayout(int[] ratios, boolean horizontal) {
        this.ratios = ratios;
        this.horizontal = horizontal;
        int tmp = 0;
        for (int element : ratios) {
            tmp += element;
        }
        sum = tmp;
    }

    /** This particular method does nothing. */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        // nothing
    }

    /**
     * Method displaying contents of parent container
     * @param parent container using this layout manager
     */
    @Override
    public void layoutContainer(Container parent) throws ArrayIndexOutOfBoundsException {
        synchronized (parent.getTreeLock()) {
            Component[] comps = parent.getComponents();
            if (comps.length != ratios.length) {
                throw new ArrayIndexOutOfBoundsException("Improper amount of components");
            }
            Dimension size = parent.getSize();
            Insets ins = parent.getInsets();
            int remWidth = (int)(size.getWidth() - ins.left - ins.right);
            int remHeight = (int)(size.getHeight() - ins.top - ins.bottom);
            int x = ins.left;
            int y = ins.top;
            int remainingSum = sum;

            if (horizontal) {
                for (int i = 0; i < ratios.length; i++) {
                    int curWidth = (int)(remWidth * ((double)(ratios[i])) / remainingSum);
                    comps[i].setBounds(x, y, curWidth, remHeight);
                    remainingSum -= ratios[i];
                    remWidth -= curWidth;
                    x += curWidth;
                }
            }
            else { // vertical
                for (int i = 0; i < ratios.length; i++) {
                    int curHeight = (int)(remHeight * ((double)(ratios[i])) / remainingSum);
                    comps[i].setBounds(x, y, remWidth, curHeight);
                    remainingSum -= ratios[i];
                    remHeight -= curHeight;
                    y += curHeight;
                }
            }
        }
    }

    /**
     * Function returning minimum size
     * @param parent container using this layout manager
     * @return Dimension(10, 10) as default value
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * Function returning preferred size
     * @param parent container using this layout manager
     * @return dimension based on components of parent's preferred sizes
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Component[] comps = parent.getComponents();
        double x = 0, y = 0;
        if (horizontal) {
            for (Component cmp : comps) {
                Dimension d = cmp.getPreferredSize();
                y = Math.max(y, d.getHeight());
                x += d.getWidth();
            }
        }
        else{
            for (Component cmp : comps) {
                Dimension d = cmp.getPreferredSize();
                x = Math.max(x, d.getWidth());
                y += d.getHeight();
            }
        }
        return new Dimension((int) x, (int) y);
    }

    /** This particular method does nothing. */
    @Override
    public void removeLayoutComponent(Component comp) {
        // nothing
    }
}
