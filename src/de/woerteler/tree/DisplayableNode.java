package de.woerteler.tree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a node of a syntax tree that is drawable. The coordinates are in
 * absolute coordinates.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public interface DisplayableNode {

  /**
   * The center of the nodes rectangle in absolute coordinates.
   * 
   * @return the node's center
   */
  Point2D getCenter();

  /**
   * Returns the children of this node.
   * 
   * @return The children of this node.
   */
  Iterable<? extends DisplayableNode> getChilds();

  /**
   * The nodes rectangle in absolute coordinates.
   * 
   * @return the node's rectangle
   */
  Rectangle2D getRect();

  /**
   * The label of this node.
   * 
   * @return The label of this node.
   */
  String getLabel();

  /**
   * The left position of the nodes text in absolute coordinates.
   * 
   * @return left position
   */
  float getTextLeft();

  /**
   * The bottom position of the nodes text in absolute coordinates.
   * 
   * @return text's bottom position
   */
  float getTextBottom();

  /**
   * The bounding box of the subtree given by this node as root of the subtree.
   * 
   * @return bounding box
   */
  Rectangle2D getBoundingBox();

  /**
   * Getter.
   * 
   * @return The parent of the node or <code>null</code> if the node is the
   *         root.
   */
  DisplayableNode getParent();

  /**
   * Resets all cached values.
   */
  void invalidate();

}
