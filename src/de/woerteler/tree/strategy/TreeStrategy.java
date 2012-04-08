package de.woerteler.tree.strategy;

import java.awt.FontMetrics;

import de.woerteler.tree.DisplayableNode;
import de.woerteler.tree.TreeNode;

/**
 * A tree strategy converts a logical syntax tree given by {@link TreeNode
 * TreeNodes} into a drawable representation.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public interface TreeStrategy {

  /**
   * Generates a displayable representation of the given syntax tree.
   * 
   * @param root The root node of the syntax tree.
   * @param fm The font metrics defining the measures for the font that will
   *          display the labels.
   * @return A displayable node structure that represents the syntax tree.
   */
  DisplayableNode generateNodeStructure(TreeNode root, FontMetrics fm);

}
