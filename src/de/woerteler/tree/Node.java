package de.woerteler.tree;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

class Node {

  public static final double horizontalSpace = 25.0;

  public static final double verticalSpace = 30.0;

  public static Node createNode(final Node parent, final String str,
      final FontMetrics fm) {
    final double width = fm.stringWidth(str) + 4;
    final double height = fm.getHeight() + 2;
    final double lower = fm.getHeight() - fm.getAscent();
    if(parent == null) {
      return new Node(null, width, height, 0, str, lower);
    }
    return new Node(parent, width, height, parent.y + parent.h + verticalSpace,
        str, lower);
  }

  private final double y;

  private final double w;

  private final double h;

  private final List<Node> childs;

  private final Node parent;

  private final String str;

  private final double lower;

  private Node(final Node parent, final double w, final double h,
      final double y, final String str, final double lower) {
    this.parent = parent;
    this.lower = lower;
    this.str = str;
    this.w = w;
    this.h = h;
    this.y = y;
    childs = new ArrayList<Node>();
  }

  public void addChild(final Node n) {
    childs.add(n);
    invalidate();
  }

  protected void invalidate() {
    horSpace = 0;
    leftSpace = 0;
    bbox = null;
    if(parent != null) {
      parent.invalidate();
    }
  }

  private double horSpace = 0;

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

  protected double getRelativeX() {
    return getHorizontalSpace() * 0.5;
  }

  private double leftSpace = 0;

  protected double getLeftSpace() {
    // root has always leftSpace = 0
    if(parent != null && leftSpace == 0) {
      parent.assignLeftSpace();
    }
    return leftSpace;
  }

  private void assignLeftSpace() {
    final double ownLS = getLeftSpace();
    double ls = 0;
    for(final Node c : childs) {
      c.leftSpace = ownLS + ls;
      ls += c.getHorizontalSpace() + horizontalSpace;
    }
  }

  protected double getCenterX() {
    return getLeftSpace() + getRelativeX();
  }

  protected double getCenterY() {
    return getTop() + getHeight() * 0.5;
  }

  protected double getTop() {
    return y;
  }

  protected double getLeft() {
    return getCenterX() - getWidth() * 0.5;
  }

  protected double getBottom() {
    return getTop() + getHeight();
  }

  protected double getWidth() {
    return w;
  }

  protected double getHeight() {
    return h;
  }

  public Rectangle2D getRect() {
    return new Rectangle2D.Double(getLeft(), getTop(), getWidth(), getHeight());
  }

  public Point2D getCenter() {
    return new Point2D.Double(getCenterX(), getCenterY());
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
    g.drawString(str, (float) getLeft() + 2, (float) (getBottom() - 1 - lower));
    for(final Node c : childs) {
      c.draw(g, lc, bc, fc, tc);
    }
  }

  private Rectangle2D bbox = null;

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
