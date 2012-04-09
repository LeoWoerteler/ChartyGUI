package de.woerteler.tree.strategy;

import de.woerteler.tree.Measures;

/**
 * Provides a visual representation of a syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class TopDownStrategy extends AbstractTreeStrategy<BUNode> {

  @Override
  protected BUNode createNode(final BUNode parent, final String str,
      final Measures m) {
    if(parent == null) return new TDNode(null, 0, str, m);
    return new TDNode(parent, parent.getCenterY() + parent.getHeight()
        + m.verticalSpace(), str, m);
  }

}

/**
 * A visual node for top down positioned trees.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
final class TDNode extends BUNode {

  /**
   * The absolute top coordinate of the rectangle.
   */
  public final double y;

  /**
   * Creates a node.
   * 
   * @param par The parent of the node.
   * @param top The absolute top position.
   * @param str The label text.
   * @param m The measures for the layout.
   */
  public TDNode(final BUNode par, final double top, final String str, final Measures m) {
    super(par, str, m);
    y = top;
  }

  @Override
  public double getCenterY() {
    return y;
  }

}
