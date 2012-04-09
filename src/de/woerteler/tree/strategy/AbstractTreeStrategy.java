package de.woerteler.tree.strategy;

import de.woerteler.tree.DisplayableNode;
import de.woerteler.tree.Measures;
import de.woerteler.tree.TreeNode;

/**
 * Traverses the syntax tree and generates a layouted tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * @param <T> The type of nodes the strategy is working on.
 */
public abstract class AbstractTreeStrategy<T extends LayoutNode<T>> implements
TreeStrategy {

  @Override
  public DisplayableNode generateNodeStructure(
      final TreeNode root, final Measures m) {
    return generateNodeStructure0(root, null, m);
  }

  /**
   * Generates a node structure from a tree node structure.
   * 
   * @param root The current root of the syntax sub-tree.
   * @param parent The current parent or <code>null</code> if we are at the top.
   * @param m The measures for the layout.
   * @return A node.
   */
  private T generateNodeStructure0(final TreeNode root, final T parent,
      final Measures m) {
    final T n = createNode(parent, root.getLabel(), m);
    for(final TreeNode c : root.getChilds()) {
      final T nc = generateNodeStructure0(c, n, m);
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
   * @param m The measures for the layout.
   * @return The newly created node.
   */
  protected abstract T createNode(final T parent, final String str, final Measures m);

}
