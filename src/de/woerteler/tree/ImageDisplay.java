package de.woerteler.tree;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;

/**
 * Generates a graphical representation of a syntax tree via direct drawing.
 * 
 * @author Joschi
 * 
 */
public class ImageDisplay implements DisplayMethod {

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
  public BufferedImage getImage(final Edge e) throws Exception {
    final Node n = generateNodeStructure(e);
    final Rectangle2D bbox = n.getBoundingBox();
    final BufferedImage img = new BufferedImage(
        (int) Math.ceil(bbox.getWidth()) + 1,
        (int) Math.ceil(bbox.getHeight()) + 1, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D gfx = (Graphics2D) img.getGraphics();
    gfx.translate(-bbox.getMinX(), -bbox.getMinY());
    n.draw(gfx, LINES, BORDER, FILL, TEXT);
    gfx.dispose();
    return img;
  }

  /**
   * Builds a {@link Node} structure out of syntax tree edges.
   * 
   * @param e The root node.
   * @return A draw-able node structure.
   */
  protected Node generateNodeStructure(final Edge e) {
    // generate a dummy image to get the font metrics of the font
    final BufferedImage dummy = new BufferedImage(1, 1,
        BufferedImage.TYPE_INT_ARGB);
    final Graphics dummyGfx = dummy.getGraphics();
    final FontMetrics fm = dummyGfx.getFontMetrics();
    final Node n = generateNodeStructure(e, null, fm);
    dummyGfx.dispose();
    dummy.flush();
    return n;
  }

  /**
   * Builds a {@link Node} structure out of parts of a syntax tree.
   * 
   * @param e A syntax tree node.
   * @param parent The parent of the current node.
   * @param fm The font metrics for displaying the labels correctly.
   * @return The node structure.
   */
  private Node generateNodeStructure(final Edge e, final Node parent,
      final FontMetrics fm) {
    final Node n = Node.createNode(parent, e.lhs, fm);
    for(final Edge c : e.children()) {
      final Node nc = generateNodeStructure(c, n, fm);
      n.addChild(nc);
    }
    return n;
  }

}
