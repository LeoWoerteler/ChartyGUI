package de.woerteler.charty;

import de.woerteler.charty.ChartParser.Edge;

/**
 * Defines a callback for different {@link Edge} drawing methods.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public interface DisplayMethod {

  /**
   * Returns a graphical representation drawer of the given syntax tree.
   * 
   * @param e The root node/edge of the syntax tree.
   * @return A {@link Displayer} to draw the syntax tree.
   * @throws Exception If an exception occurs during the generation of the
   *           drawer.
   */
  Displayer getDisplayer(Edge e) throws Exception;

}
