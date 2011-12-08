package de.woerteler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.woerteler.util.ImageUtils;

/**
 * Displays the parse tree(s) of the currently parsed expression.
 *
 * @author Leo Woerteler
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

  /** Parse tree image. */
  private BufferedImage tree;

  /** The image panel. */
  private final JPanel image;

  /** Margin around the image. */
  private static final int MARGIN = 5;

  /**
   * Constructor.
   *
   * @param ctrl controller
   */
  ParseTreeViewer(final Controller ctrl) {
    super(new BorderLayout());
    final JPanel nav = new JPanel(new BorderLayout());

    image = new JPanel() {

      /** Serial version UID. */
      private static final long serialVersionUID = -7709453016860715840L;

      @Override
      protected void paintComponent(final Graphics g) {
        final Dimension dim = getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dim.width, dim.height);
        if (dim.width > 2 * MARGIN && dim.height > 2 * MARGIN) {
          final BufferedImage i = getTree();
          if (i != null) {
            final int nw = dim.width - 2 * MARGIN;
            final int nh = dim.height - 2 * MARGIN;
            final Image scaled = ImageUtils.scaleToFit(i, nw, nh);
            g.drawImage(scaled, (nw - scaled.getWidth(null)) / 2
                + MARGIN, (nh - scaled.getHeight(null)) / 2
                + MARGIN, null);
          }
        }

      }
    };

    add(image, BorderLayout.CENTER);

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
   * @param img parse tree
   * @param pos current position
   * @param num current number of parse trees
   */
  void showParseTree(final BufferedImage img, final int pos, final int num) {
    if (img == null) {
      label.setText("nothing to show");
      left.setEnabled(false);
      right.setEnabled(false);
    } else {
      label.setText(pos + " of " + num);
      left.setEnabled(pos > 1);
      right.setEnabled(pos < num);
    }
    setTree(img);
  }

  /**
   * Sets the tree image.
   *
   * @param t tree
   */
  private synchronized void setTree(final BufferedImage t) {
    tree = t;
    repaint();
  }

  /**
   * Gets the currently shown tree.
   *
   * @return tree
   */
  synchronized BufferedImage getTree() {
    return tree;
  }
}
