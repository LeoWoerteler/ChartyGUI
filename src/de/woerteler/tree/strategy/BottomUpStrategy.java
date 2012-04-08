package de.woerteler.tree.strategy;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import de.woerteler.tree.DisplayableNode;
import de.woerteler.tree.TreeNode;

/**
 * Provides a visual representation of a syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public final class BottomUpStrategy implements TreeStrategy {

  /**
   * The horizontal space between two nodes.
   */
  public static final double HORIZONTAL_SPACE = 25.0;

  /**
   * The vertical space between two nodes.
   */
  public static final double VERTICAL_SPACE = 30.0;

  /**
   * The horizontal space between rectangle border and text.
   */
  public static final double TEXT_H_SPACE = 2;

  /**
   * The vertical space between rectangle border and text.
   */
  public static final double TEXT_V_SPACE = 1;

  /**
   * A visual node for bottom up positioned trees.
   * 
   * @author Joschi <josua.krause@googlemail.com>
   */
  private static class Node implements DisplayableNode {

    /**
     * The absolute top coordinate of the rectangle.
     */
    public final double y;

    /**
     * The width of the rectangle.
     */
    public final double width;

    /**
     * The height of the rectangle.
     */
    public final double height;

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
    private final String label;

    /**
     * The distance between the bottom of the node and the bottom of the visible
     * text given by the font metric.
     */
    private final double lower;

    /**
     * Creates a node.
     * 
     * @param par The parent of the node.
     * @param w The width of the node.
     * @param h The height of the node.
     * @param top The absolute top position.
     * @param str The label text.
     * @param bot The distance from the bottom of the node to the bottom of the
     *          text given by the font metric.
     */
    public Node(final Node par, final double w, final double h,
        final double top, final String str, final double bot) {
      parent = par;
      lower = bot;
      label = str;
      width = w;
      height = h;
      y = top;
      childs = new ArrayList<Node>();
    }

    /**
     * Adds a child.
     * 
     * @param n The child.
     * @throws IllegalArgumentException If the node is not the parent of the
     *           child.
     */
    public void addChild(final Node n) {
      if(n.parent != this) throw new IllegalArgumentException(
          "node must be parent of child");
      childs.add(n);
      invalidate();
    }

    @Override
    public Iterable<Node> getChilds() {
      return childs;
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
        for(final Node c : childs) {
          if(hs != 0) {
            hs += HORIZONTAL_SPACE;
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
      for(final Node c : childs) {
        c.leftSpace = ownLS + ls;
        ls += c.getHorizontalSpace() + HORIZONTAL_SPACE;
      }
    }

    /**
     * The horizontal center of the nodes rectangle in absolute coordinates.
     * 
     * @return horizontal center
     */
    public double getCenterX() {
      return getLeftSpace() + getRelativeX();
    }

    /**
     * The vertical center of the nodes rectangle in absolute coordinates.
     * 
     * @return vertical center
     */
    public double getCenterY() {
      return getTop() + getHeight() * 0.5;
    }

    /**
     * The top of the nodes rectangle in absolute coordinates.
     * 
     * @return top
     */
    public double getTop() {
      return y;
    }

    /**
     * The left side of the nodes rectangle in absolute coordinates.
     * 
     * @return left
     */
    public double getLeft() {
      return getCenterX() - getWidth() * 0.5;
    }

    /**
     * The bottom of the nodes rectangle in absolute coordinates.
     * 
     * @return bottom
     */
    public double getBottom() {
      return getTop() + getHeight();
    }

    /**
     * The width of the nodes rectangle.
     * 
     * @return width
     */
    public double getWidth() {
      return width;
    }

    /**
     * The height of the nodes rectangle.
     * 
     * @return height
     */
    public double getHeight() {
      return height;
    }

    @Override
    public Rectangle2D getRect() {
      return new Rectangle2D.Double(getLeft(), getTop(), getWidth(), getHeight());
    }

    @Override
    public Point2D getCenter() {
      return new Point2D.Double(getCenterX(), getCenterY());
    }

    @Override
    public float getTextLeft() {
      return (float) (getLeft() + TEXT_H_SPACE);
    }

    @Override
    public float getTextBottom() {
      return (float) (getBottom() - TEXT_V_SPACE - lower);
    }

    @Override
    public String getLabel() {
      return label;
    }

    /** The cached value of the bounding box. */
    private Rectangle2D bbox;

    @Override
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

  @Override
  public DisplayableNode generateNodeStructure(
      final TreeNode root, final FontMetrics fm) {
    return generateNodeStructure0(root, null, fm);
  }

  /**
   * Generates a node structure from a tree node structure.
   * 
   * @param root The current root of the syntax sub-tree.
   * @param parent The current parent or <code>null</code> if we are at the top.
   * @param fm The font-metrics defining the space needed for the labels.
   * @return A node.
   */
  private static Node generateNodeStructure0(final TreeNode root, final Node parent,
      final FontMetrics fm) {
    final Node n = createNode(parent, root.getLabel(), fm);
    for(final TreeNode c : root.getChilds()) {
      final Node nc = generateNodeStructure0(c, n, fm);
      n.addChild(nc);
    }
    return n;
  }

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
  private static Node createNode(final Node parent, final String str,
      final FontMetrics fm) {
    final double width = fm.stringWidth(str) + TEXT_H_SPACE * 2;
    final double height = fm.getHeight() + TEXT_V_SPACE * 2;
    final double lower = fm.getHeight() - fm.getAscent();
    if(parent == null) return new Node(null, width, height, 0, str, lower);
    return new Node(parent, width, height, parent.y + parent.height
        + VERTICAL_SPACE, str, lower);
  }

}
