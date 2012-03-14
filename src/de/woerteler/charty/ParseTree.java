package de.woerteler.charty;

import de.woerteler.charty.ChartParser.Edge;

/**
 * Class representing a parse tree generated by the {@link ChartParser}.
 * 
 * @author Leo Woerteler
 */
public final class ParseTree {

  /** Root edge of this tree. */
  private final Edge edge;

  /** The displayer. */
  private Displayer disp;

  /**
   * Constructor.
   * 
   * @param e root edge
   */
  ParseTree(final Edge e) {
    edge = e;
  }

  /**
   * Get the displayer of this parse tree.
   * 
   * @param method The method to draw the syntax tree.
   * @return parse tree displayer
   * @throws Exception if the conversion fails
   */
  public synchronized Displayer getDisplayer(final DisplayMethod method)
      throws Exception {
    if(disp == null) {
      disp = method.getDisplayer(edge);
    }
    return disp;
  }

}
