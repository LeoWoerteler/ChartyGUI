package de.woerteler.tree.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import de.woerteler.tree.Node;

/**
 * The default render method for trees. The design is adapted from the LaTeX way
 * to draw trees.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class DefaultRenderer implements NodeRenderer {

  /**
   * The stroke to draw lines.
   */
  public static Stroke STROKE = new BasicStroke(1.2f);

  @Override
  public void render(final Graphics2D g, final Node root) {
    g.setColor(Color.BLACK);
    g.drawString(root.getLabel(), (float) root.getTextLeft(),
        (float) root.getTextBottom());
    final double x = root.getCenterX();
    final double y = root.getBottom();
    for(final Node c : root.getChilds()) {
      final Line2D line = new Line2D.Double(x, y, c.getCenterX(), c.getTop());
      final Graphics2D g2 = (Graphics2D) g.create();
      g2.setStroke(STROKE);
      g2.draw(line);
      g2.dispose();
      render(g, c);
    }
  }

}
