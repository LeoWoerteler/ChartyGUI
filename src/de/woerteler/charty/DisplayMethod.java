package de.woerteler.charty;

import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;

/**
 * Defines a callback for different {@link Edge} drawing methods.
 *
 * @author Joschi
 */
public interface DisplayMethod {

  /**
   * Draws a graphical representation of the given syntax tree.
   *
   * @param e The root node/edge of the syntax tree.
   * @return A {@link BufferedImage} representing the syntax tree.
   * @throws Exception If an exception occurs during drawing.
   */
  BufferedImage getImage(Edge e) throws Exception;

}
