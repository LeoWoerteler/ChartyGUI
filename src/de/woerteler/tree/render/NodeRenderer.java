package de.woerteler.tree.render;

import java.awt.Graphics2D;

import de.woerteler.tree.Node;

/**
 * Renders a syntax tree.
 * 
 * @author Joschi
 */
public interface NodeRenderer {

  /**
   * Renders the syntax tree.
   * 
   * @param gfx The graphics to render to.
   * @param root The root node of the tree.
   */
  void render(Graphics2D gfx, Node root);

}
