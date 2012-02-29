package de.woerteler.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a visual representation of a syntax tree.
 * 
 * @author Joschi
 * 
 */
class Node {

  /**
   * The horizontal space between two nodes.
   */
  public static final double horizontalSpace = 25.0;

  /**
   * The vertical space between two nodes.
   */
  public static final double verticalSpace = 30.0;

  /**
   * The horizontal space between rectangle border and text.
   */
  public static final double textHSpace = 2;

  /**
   * The vertical space between rectangle border and text.
   */
  public static final double textVSpace = 1;

  /**
   * Creates a single new node.
   * 
   * @param parent The parent of the node or <code>null</code> if it is the root
   *          node.
   * @param str The string that will be the label.
   * @param fm The font metrics defining the size of the node. Note that the
   *          {@link Font} of this font metric should also be used to draw the
   *          nodes later. If this is not the case the text may be at the wrong
   *          position or has the wrong size.
   * @return The newly created node.
   */
  public static Node createNode(final Node parent, final String str,
      final FontMetrics fm) {
    final double width = fm.stringWidth(str) + textHSpace * 2;
    final double height = fm.getHeight() + textVSpace * 2;
    final double lower = fm.getHeight() - fm.getAscent();
    if(parent == null) {
      return new Node(null, width, height, 0, str, lower);
    }
    return new Node(parent, width, height, parent.y + parent.h + verticalSpace,
        str, lower);
  }

  /**
   * The absolute top coordinate of the rectangle.
   */
  private final double y;

  /**
   * The width of the rectangle.
   */
  private final double w;

  /**
   * The height of the rectangle.
   */
  private final double h;

  /**
   * The children of the node.
   */
  private final List<Node> childs;

  /**
   * The parent of the node.
   */
  private final Node parent;

  /**
   * The label text.
   */
  private final String str;

  /**
   * The distance between the bottom of the node and the bottom of the visible
   * text given by the font metric.
   */
  private final double lower;

  /**
   * Creates a node.
   * 
   * @param _parent The parent of the node.
   * @param _w The width of the node.
   * @param _h The height of the node.
   * @param _y The absolute top position.
   * @param _str The label text.
   * @param _lower The distance from the bottom of the node to the bottom of the
   *          text given by the font metric.
   */
  private Node(final Node _parent, final double _w, final double _h,
      final double _y, final String _str, final double _lower) {
    parent = _parent;
    lower = _lower;
    str = _str;
    w = _w;
    h = _h;
    y = _y;
    childs = new ArrayList<Node>();
  }

  /**
   * Adds a child.
   * @param n The child.
   * @throws IllegalArgumentException If the node is not the parent of the
   *           child.
   */
  public void addChild(final Node n) {
    if(n.parent != this) {
      throw new IllegalArgumentException("node must be parent of child");
    }
    childs.add(n);
    invalidate();
  }

  /**
   * Resets all cached values.
   */
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
  private double horSpace = 0;

  /**
   * The horizontal space of the node. That is the maximum of the width of the
   * node and the overall horizontal space of its descendants.
   * 
   * @return The horizontal space.
   */
  protected double getHorizontalSpace() {
    if(horSpace == 0) {
      double hs = 0;
      for(final Node c : childs) {
        if(hs != 0) {
          hs += horizontalSpace;
        }
        hs += c.getHorizontalSpace();
      }
      horSpace = Math.max(getWidth(), hs);
    }
    return horSpace;
  }

  /**
   * @return The relative center of the horizontal space.
   */
  protected double getRelativeX() {
    return getHorizontalSpace() * 0.5;
  }

  /**
   * The cached value of the left space.
   */
  private double leftSpace = 0;

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
    for(final Node c : childs) {
      c.leftSpace = ownLS + ls;
      ls += c.getHorizontalSpace() + horizontalSpace;
    }
  }

  /**
   * @return The horizontal center of the nodes rectangle in absolute
   *         coordinates.
   */
  protected double getCenterX() {
    return getLeftSpace() + getRelativeX();
  }

  /**
   * @return The vertical center of the nodes rectangle in absolute coordinates.
   */
  protected double getCenterY() {
    return getTop() + getHeight() * 0.5;
  }

  /**
   * @return The top of the nodes rectangle in absolute coordinates.
   */
  protected double getTop() {
    return y;
  }

  /**
   * @return The left side of the nodes rectangle in absolute coordinates.
   */
  protected double getLeft() {
    return getCenterX() - getWidth() * 0.5;
  }

  /**
   * @return The bottom of the nodes rectangle in absolute coordinates.
   */
  protected double getBottom() {
    return getTop() + getHeight();
  }

  /**
   * @return The width of the nodes rectangle.
   */
  protected double getWidth() {
    return w;
  }

  /**
   * @return The height of the nodes rectangle.
   */
  protected double getHeight() {
    return h;
  }

  /**
   * @return The nodes rectangle in absolute coordinates.
   */
  public Rectangle2D getRect() {
    return new Rectangle2D.Double(getLeft(), getTop(), getWidth(), getHeight());
  }

  /**
   * @return The center of the nodes rectangle in absolute coordinates.
   */
  public Point2D getCenter() {
    return new Point2D.Double(getCenterX(), getCenterY());
  }

  /**
   * @return The left position of the nodes text in absolute coordinates.
   */
  protected double getTextLeft() {
    return getLeft() + textHSpace;
  }

  /**
   * @return The bottom position of the nodes text in absolute coordinates.
   */
  protected double getTextBottom() {
    return getBottom() - textVSpace - lower;
  }

  /**
   * Draws the node and its childs.
   * 
   * @param g The graphics context.
   * @param lc The line color.
   * @param bc The border color.
   * @param fc The fill color.
   * @param tc The text color.
   */
  public void draw(final Graphics2D g, final Color lc, final Color bc,
      final Color fc, final Color tc) {
    final Point2D center = getCenter();
    g.setColor(lc);
    for(final Node c : childs) {
      final Line2D line = new Line2D.Double(center, c.getCenter());
      g.draw(line);
    }
    final Rectangle2D rect = getRect();
    g.setColor(fc);
    g.fill(rect);
    g.setColor(bc);
    g.draw(rect);
    g.setColor(tc);
    g.drawString(str, (float) getTextLeft(), (float) getTextBottom());
    for(final Node c : childs) {
      c.draw(g, lc, bc, fc, tc);
    }
  }

  /**
   * The cached value of the bounding box.
   */
  private Rectangle2D bbox = null;

  /**
   * @return The bounding box of the subtree given by this node as root of the
   *         subtree.
   */
  public Rectangle2D getBoundingBox() {
    if(bbox == null) {
      Rectangle2D rect = getRect();
      for(final Node c : childs) {
        rect = rect.createUnion(c.getBoundingBox());
      }
      bbox = rect;
    }
    return bbox;
  }

}
