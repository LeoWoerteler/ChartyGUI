package de.woerteler.tree.strategy;

import de.woerteler.tree.Measures;

/**
 * Provides a visual representation of a syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public final class BottomUpStrategy extends AbstractTreeStrategy<BUNode> {

  @Override
  protected BUNode createNode(final BUNode parent,
      final String str, final Measures m) {
    return new BUNode(parent, str, m);
  }

}

/**
 * A visual node for bottom up positioned trees.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
class BUNode extends LayoutNode<BUNode> {

  /**
   * Creates a node.
   * 
   * @param par The parent of the node.
   * @param str The label text.
   * @param m The measures for the layout.
   */
  public BUNode(final BUNode par, final String str, final Measures m) {
    super(par, str, m);
  }

  @Override
  protected void doInvalidate() {
    horSpace = 0;
    leftSpace = 0;
    y = 0;
  }

  /**
   * The cached horizontal space.
   */
  private double horSpace;

  /**
   * The horizontal space of the node. That is the maximum of the width of the
   * node and the overall horizontal space of its descendants.
   * 
   * @return The horizontal space.
   */
  protected double getHorizontalSpace() {
    if(horSpace == 0) {
      double hs = 0;
      for(final BUNode c : getChilds()) {
        if(hs != 0) {
          hs += measures.horizontalSpace();
        }
        hs += c.getHorizontalSpace();
      }
      horSpace = Math.max(getWidth(), hs);
    }
    return horSpace;
  }

  /**
   * The relative center of the horizontal space.
   * 
   * @return relative center
   */
  protected double getRelativeX() {
    return getHorizontalSpace() * 0.5;
  }

  /**
   * The cached value of the left space.
   */
  private double leftSpace;

  /**
   * The left space of the node. That is the horizontal space left of the nodes
   * horizontal space, i.e. the absolute coordinate of the left side of the
   * nodes overall space.
   * 
   * @return The left space.
   */
  protected double getLeftSpace() {
    // root has always leftSpace = 0
    if(parent != null && leftSpace == 0) {
      parent.assignLeftSpace();
    }
    return leftSpace;
  }

  /**
   * Assigns the left space to the children of this node.
   */
  private void assignLeftSpace() {
    final double ownLS = getLeftSpace();
    double ls = 0;
    for(final BUNode c : getChilds()) {
      c.leftSpace = ownLS + ls;
      ls += c.getHorizontalSpace() + measures.horizontalSpace();
    }
  }

  @Override
  public double getCenterX() {
    return getLeftSpace() + getRelativeX();
  }

  /**
   * The y coordinate.
   */
  private double y;

  @Override
  public double getCenterY() {
    if(isLeaf()) return 0;
    if(y == 0) {
      double minY = 0;
      for(final BUNode n : getChilds()) {
        final double ty = n.getCenterY();
        if(ty < minY) {
          minY = ty;
        }
      }
      for(final BUNode n : getChilds()) {
        n.y = minY;
      }
      y = minY - measures.boxHeight() - getHeight();
    }
    return y;
  }

}
