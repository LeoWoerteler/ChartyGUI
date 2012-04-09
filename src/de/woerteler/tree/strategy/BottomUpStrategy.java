package de.woerteler.tree.strategy;

import java.awt.geom.Rectangle2D;

import de.woerteler.tree.Measures;

/**
 * Provides a visual representation of a syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public final class BottomUpStrategy extends AbstractTreeStrategy<Node> {

  @Override
  protected Node createNode(final Node parent, final String str,
      final Measures m) {
    if(parent == null) return new Node(null, 0, str, m);
    return new Node(parent, parent.y + parent.getHeight() + m.verticalSpace(), str, m);
  }

}

/**
 * A visual node for bottom up positioned trees.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
final class Node extends LayoutNode<Node> {

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
  public Node(final Node par, final double top, final String str, final Measures m) {
    super(par, str, m);
    y = top;
  }

  @Override
  protected void invalidate() {
    horSpace = 0;
    leftSpace = 0;
    bbox = null;
    if(parent != null) {
      parent.invalidate();
    }
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
      for(final Node c : getChilds()) {
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
    for(final Node c : getChilds()) {
      c.leftSpace = ownLS + ls;
      ls += c.getHorizontalSpace() + measures.horizontalSpace();
    }
  }

  @Override
  public double getCenterX() {
    return getLeftSpace() + getRelativeX();
  }

  @Override
  public double getCenterY() {
    return y;
  }

  /** The cached value of the bounding box. */
  private Rectangle2D bbox;

  @Override
  public Rectangle2D getBoundingBox() {
    if(bbox == null) {
      Rectangle2D rect = getRect();
      for(final Node c : getChilds()) {
        rect = rect.createUnion(c.getBoundingBox());
      }
      bbox = rect;
    }
    return bbox;
  }

}
