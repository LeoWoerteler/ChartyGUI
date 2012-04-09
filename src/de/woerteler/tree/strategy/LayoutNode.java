package de.woerteler.tree.strategy;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import de.woerteler.tree.DisplayableNode;
import de.woerteler.tree.Measures;

/**
 * An abstract node that defines some measures for the final displayable tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * @param <T> The special type of the layout nodes. In order to get the correct
 *          types in the sub classes.
 */
public abstract class LayoutNode<T extends DisplayableNode> implements DisplayableNode {

  /**
   * The label text.
   */
  private final String label;

  /**
   * The width of the rectangle.
   */
  private final double width;

  /**
   * The height of the rectangle.
   */
  private final double height;

  /**
   * The children of the node.
   */
  private final List<T> childs;

  /**
   * The measures to layout the tree.
   */
  protected final Measures measures;

  /**
   * The parent of the node.
   */
  protected final T parent;

  /**
   * Creates a layout node.
   * 
   * @param parent The parent of this node or <code>null</code> if it is the
   *          root node.
   * @param label The label of this node.
   * @param measures The measures that should be used to generate the layout.
   */
  public LayoutNode(final T parent, final String label, final Measures measures) {
    this.parent = parent;
    this.label = label;
    this.measures = measures;
    width = measures.boxWidth(label);
    height = measures.boxHeight();
    childs = new ArrayList<T>();
  }

  @Override
  public void invalidate() {
    bbox = null;
    doInvalidate();
    if(getParent() != null) {
      getParent().invalidate();
    }
  }

  /**
   * Does the invalidation.
   */
  protected abstract void doInvalidate();

  @Override
  public T getParent() {
    return parent;
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

  /**
   * The horizontal center of the nodes rectangle in absolute coordinates.
   * 
   * @return horizontal center
   */
  public abstract double getCenterX();

  /**
   * The vertical center of the nodes rectangle in absolute coordinates.
   * 
   * @return vertical center
   */
  public abstract double getCenterY();

  /**
   * The left side of the nodes rectangle in absolute coordinates.
   * 
   * @return left
   */
  public double getLeft() {
    return getCenterX() - getWidth() * .5;
  }

  /**
   * The top of the nodes rectangle in absolute coordinates.
   * 
   * @return top
   */
  public double getTop() {
    return getCenterY() - getHeight() * .5;
  }

  /**
   * The bottom of the nodes rectangle in absolute coordinates.
   * 
   * @return bottom
   */
  public double getBottom() {
    return getTop() + getHeight();
  }

  @Override
  public Point2D getCenter() {
    return new Point2D.Double(getCenterX(), getCenterY());
  }

  /**
   * Adds a child.
   * 
   * @param n The child.
   * @throws IllegalArgumentException If the node is not the parent of the
   *           child.
   */
  public void addChild(final T n) {
    if(n.getParent() != this) throw new IllegalArgumentException(
        "node must be parent of child");
    childs.add(n);
    invalidate();
  }

  @Override
  public Iterable<T> getChilds() {
    return childs;
  }

  /**
   * Getter.
   * 
   * @return Number of children.
   */
  public int getNumberOfChilds() {
    return childs.size();
  }

  /**
   * Getter.
   * 
   * @return Whether this node is a leaf.
   */
  public boolean isLeaf() {
    return getNumberOfChilds() == 0;
  }

  @Override
  public Rectangle2D getRect() {
    return new Rectangle2D.Double(getLeft(), getTop(), getWidth(), getHeight());
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public float getTextLeft() {
    return (float) (getLeft() + measures.stringLeft());
  }

  @Override
  public float getTextBottom() {
    return (float) (getBottom() + measures.stringFromBelow());
  }

  /** The cached value of the bounding box. */
  private Rectangle2D bbox;

  @Override
  public Rectangle2D getBoundingBox() {
    if(bbox == null) {
      Rectangle2D rect = getRect();
      for(final T c : getChilds()) {
        rect = rect.createUnion(c.getBoundingBox());
      }
      bbox = rect;
    }
    return bbox;
  }

}
