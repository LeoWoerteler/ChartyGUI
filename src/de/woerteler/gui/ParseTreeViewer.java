package de.woerteler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.woerteler.charty.Displayer;

/**
 * Displays the parse tree(s) of the currently parsed expression.
 * 
 * @author Leo Woerteler
 * @author Joschi <josua.krause@googlemail.com>
 */
public final class ParseTreeViewer extends JPanel {

  /** Serial version UID. */
  private static final long serialVersionUID = -8763835051370605358L;

  /** Label. */
  private final JLabel label;
  /** Left arrow button. */
  private final JButton left;
  /** Right arrow button. */
  private final JButton right;

  /** Parse tree displayer. */
  private Displayer tree;

  /** The display panel. */
  private final JComponent display;

  /** Margin around the image. */
  private static final int MARGIN = 5;

  /** The x offset of the tree. */
  double offX;

  /** The y offset of the tree. */
  double offY;

  /**
   * Constructor.
   * 
   * @param ctrl controller
   */
  ParseTreeViewer(final Controller ctrl) {
    super(new BorderLayout());
    final JPanel nav = new JPanel(new BorderLayout());

    display = new JComponent() {

      /** Serial version UID. */
      private static final long serialVersionUID = -7709453016860715840L;

      @Override
      protected void paintComponent(final Graphics g) {
        final Dimension dim = getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dim.width + 1, dim.height + 1);
        if(dim.width > 2 * MARGIN && dim.height > 2 * MARGIN) {
          final Displayer d = getTree();
          if(d != null) {
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(offX, offY);
            d.drawTree(g2);
            g2.dispose();
          }
        }
      }

    };
    final MouseAdapter mouse = new MouseAdapter() {

      private boolean drag;

      private int sx;

      private int sy;

      private double origX;

      private double origY;

      @Override
      public void mousePressed(final MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
          sx = e.getX();
          sy = e.getY();
          origX = getOffsetX();
          origY = getOffsetY();
          drag = true;
        }
      }

      @Override
      public void mouseDragged(final MouseEvent e) {
        if(drag) {
          move(e.getX(), e.getY());
        }
      }

      @Override
      public void mouseReleased(final MouseEvent e) {
        if(drag) {
          move(e.getX(), e.getY());
          drag = false;
        }
      }

      /**
       * sets the offset according to the mouse position
       * 
       * @param x the mouse x position
       * @param y the mouse y position
       */
      private void move(final int x, final int y) {
        setOffset(origX + (x - sx), origY + (y - sy));
      }

    };
    display.addMouseListener(mouse);
    display.addMouseMotionListener(mouse);
    add(display, BorderLayout.CENTER);

    label = new JLabel("nothing to show");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    nav.add(label, BorderLayout.CENTER);

    left = new JButton(ChartyGUI.icon("arrow_left"));
    left.setEnabled(false);
    left.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.navigate(false);
      }
    });
    nav.add(left, BorderLayout.WEST);

    right = new JButton(ChartyGUI.icon("arrow_right"));
    right.setEnabled(false);
    right.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.navigate(true);
      }
    });
    nav.add(right, BorderLayout.EAST);
    add(nav, BorderLayout.SOUTH);
  }

  /**
   * Displays the given parse tree.
   * 
   * @param disp parse tree
   * @param pos current position
   * @param num current number of parse trees
   */
  void showParseTree(final Displayer disp, final int pos, final int num) {
    if(disp == null) {
      label.setText("nothing to show");
      left.setEnabled(false);
      right.setEnabled(false);
    } else {
      label.setText(pos + " of " + num);
      left.setEnabled(pos > 1);
      right.setEnabled(pos < num);
    }
    setTree(disp);
  }

  /**
   * Sets the tree displayer.
   * 
   * @param t tree
   */
  private synchronized void setTree(final Displayer t) {
    tree = t;
    final Dimension dim = getSize();
    final int nw = dim.width - 2 * MARGIN;
    final int nh = dim.height - 2 * MARGIN;
    final Rectangle2D bbox = tree.getBoundingBox();
    // does repaint
    setOffset(MARGIN + (nw - bbox.getWidth()) / 2,
        MARGIN + (nh - bbox.getHeight()) / 2);
  }

  /**
   * Gets the currently shown tree.
   * 
   * @return tree
   */
  synchronized Displayer getTree() {
    return tree;
  }

  /**
   * @param x the x offset.
   * @param y the y offset.
   */
  public void setOffset(final double x, final double y) {
    offX = x;
    offY = y;
    display.repaint();
  }

  /**
   * @return the x offset
   */
  public double getOffsetX() {
    return offX;
  }

  /**
   * @return the y offset
   */
  public double getOffsetY() {
    return offY;
  }

}
