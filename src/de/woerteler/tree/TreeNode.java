package de.woerteler.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a node of the syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class TreeNode {

  /**
   * The children of the node.
   */
  private final List<TreeNode> childs;

  /**
   * The parent of the node.
   */
  private final TreeNode parent;

  /**
   * The label text.
   */
  private final String label;

  /**
   * Creates a node.
   * 
   * @param parent The parent of the node.
   * @param label The label text.
   */
  public TreeNode(final TreeNode parent, final String label) {
    this.parent = parent;
    this.label = label;
    childs = new ArrayList<TreeNode>();
  }

  /**
   * Adds a child.
   * 
   * @param n The child.
   * @throws IllegalArgumentException If the node is not the parent of the
   *           child.
   */
  public void addChild(final TreeNode n) {
    if(n.parent != this) throw new IllegalArgumentException(
        "node must be parent of child");
    childs.add(n);
  }

  /**
   * Returns the children of this node.
   * 
   * @return The children of this node.
   */
  public Iterable<TreeNode> getChilds() {
    return childs;
  }

  /**
   * Getter.
   * 
   * @return The parent of the node.
   */
  public TreeNode getParent() {
    return parent;
  }

  /**
   * The label of this node.
   * 
   * @return The label of this node.
   */
  public String getLabel() {
    return label;
  }

}
