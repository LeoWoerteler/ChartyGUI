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
import de.woerteler.tree.render.NodeRenderer;
import de.woerteler.tree.strategy.TreeStrategy;

/**
 * Generates a graphical representation of a syntax tree via direct drawing.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class DirectDisplay implements DisplayMethod {

  /**
   * The font to draw the labels or <code>null</code> if the default font should
   * be used.
   */
  public static Font font = Font.decode("times new roman BOLD 12");

  /**
   * The renderer that is used to draw the tree.
   */
  private final NodeRenderer renderer;

  /**
   * The strategy that is used to place the nodes.
   */
  private final TreeStrategy strategy;

  /**
   * Creates a direct on screen display.
   * 
   * @param renderer The renderer to draw the tree.
   * @param strategy The strategy to place the nodes.
   */
  public DirectDisplay(final NodeRenderer renderer, final TreeStrategy strategy) {
    this.renderer = renderer;
    this.strategy = strategy;
  }

  @Override
  public Displayer getDisplayer(final Edge e) throws Exception {
    final Font curFont = font;
    final NodeRenderer render = renderer;
    final DisplayableNode n = generateNodeStructure(e, curFont);
    final Rectangle2D bbox = n.getBoundingBox();
    return new Displayer() {

      @Override
      public void drawTree(final Graphics2D gfx) {
        if(curFont != null) {
          gfx.setFont(curFont);
        }
        render.render(gfx, n);
      }

      @Override
      public Rectangle2D getBoundingBox() {
        return bbox;
      }

    };
  }

  /**
   * Builds a {@link DisplayableNode} structure out of syntax tree edges.
   * 
   * @param e The root node.
   * @param curFont The font that will be used to draw.
   * @return A draw-able node structure.
   */
  private DisplayableNode generateNodeStructure(final Edge e, final Font curFont) {
    // generate a dummy image to get the font metrics of the font
    final BufferedImage dummy = new BufferedImage(1, 1,
        BufferedImage.TYPE_INT_ARGB);
    final Graphics dummyGfx = dummy.getGraphics();
    if(curFont != null) {
      dummyGfx.setFont(curFont);
    }
    final FontMetrics fm = dummyGfx.getFontMetrics();
    final TreeNode tn = generateNodeStructure(e, (TreeNode) null);
    final DisplayableNode n = strategy.generateNodeStructure(tn, new Measures(fm));
    dummyGfx.dispose();
    dummy.flush();
    return n;
  }

  /**
   * Builds a {@link TreeNode} structure out of parts of a syntax tree.
   * 
   * @param e A syntax tree node.
   * @param parent The parent of the current node.
   * @return The node structure.
   */
  private static TreeNode generateNodeStructure(final Edge e, final TreeNode parent) {
    final TreeNode n = new TreeNode(parent, e.lhs);
    if(e.hasRealChildren()) {
      for(final Edge c : e) {
        final TreeNode nc = generateNodeStructure(c, n);
        n.addChild(nc);
      }
    } else {
      for(final String label : e.rhs) {
        n.addChild(new TreeNode(n, label));
      }
    }
    return n;
  }

}
