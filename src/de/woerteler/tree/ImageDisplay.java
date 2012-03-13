package de.woerteler.tree;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;
import de.woerteler.charty.Displayer;
import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.tree.render.NodeRenderer;

/**
 * Generates a graphical representation of a syntax tree via direct drawing.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class ImageDisplay implements DisplayMethod {

  /**
   * The renderer that is used to draw the tree.
   */
  public static NodeRenderer renderer = new DefaultRenderer();

  /**
   * The font to draw the labels or <code>null</code> if the default font should
   * be used.
   */
  public static Font font = Font.decode("times new roman BOLD 12");

  @Override
  public Displayer getDisplayer(final Edge e) throws Exception {
    final Node n = generateNodeStructure(e);
    final Rectangle2D bbox = n.getBoundingBox();
    return new Displayer() {

      @Override
      public void drawTree(final Graphics2D gfx) {
        if(font != null) {
          gfx.setFont(font);
        }
        n.draw(gfx, renderer);
      }

      @Override
      public Rectangle2D getBoundingBox() {
        return bbox;
      }

    };
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
    if(font != null) {
      dummyGfx.setFont(font);
    }
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
    if(e.hasRealChildren()) {
      for(final Edge c : e) {
        final Node nc = generateNodeStructure(c, n, fm);
        n.addChild(nc);
      }
    } else {
      for(final String label : e.rhs) {
        n.addChild(Node.createNode(n, label, fm));
      }
    }
    return n;
  }

}
