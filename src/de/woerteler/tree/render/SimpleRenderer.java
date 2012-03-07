package de.woerteler.tree.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.woerteler.tree.Node;

public class SimpleRenderer implements NodeRenderer {

  /**
   * The color of the lines.
   */
  public static final Color LINES = Color.BLACK;

  /**
   * The color of the rectangle borders.
   */
  public static final Color BORDER = Color.BLACK;

  /**
   * The filling color of the rectangles.
   */
  public static final Color FILL = Color.ORANGE;

  /**
   * The text color.
   */
  public static final Color TEXT = Color.BLACK;

  @Override
  public void render(final Graphics2D g, final Node root) {
    final Point2D center = root.getCenter();
    g.setColor(LINES);
    for(final Node c : root.getChilds()) {
      final Line2D line = new Line2D.Double(center, c.getCenter());
      g.draw(line);
    }
    final Rectangle2D rect = root.getRect();
    g.setColor(FILL);
    g.fill(rect);
    g.setColor(BORDER);
    g.draw(rect);
    g.setColor(TEXT);
    g.drawString(root.getLabel(), (float) root.getTextLeft(),
        (float) root.getTextBottom());
    for(final Node c : root.getChilds()) {
      render(g, c);
    }
  }

}
