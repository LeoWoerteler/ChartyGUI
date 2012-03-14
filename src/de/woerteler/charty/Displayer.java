package de.woerteler.charty;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Draws a syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 */
public interface Displayer {

  /**
   * Draws a syntax tree.
   * 
   * @param gfx The device to draw on.
   */
  void drawTree(Graphics2D gfx);

  /**
   * Getter.
   * 
   * @return the bounding box of the tree.
   */
  Rectangle2D getBoundingBox();

}
