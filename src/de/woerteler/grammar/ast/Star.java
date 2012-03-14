package de.woerteler.grammar.ast;

/**
 * The Kleene star.
 * 
 * @author Leo Woerteler
 */
public class Star extends RHS {
  /** The child expression. */
  private final RHS child;

  /**
   * Constructor taking the child expression.
   * 
   * @param sub child expression
   */
  public Star(final RHS sub) {
    child = sub;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    return child.toString(sb).append('*');
  }
}
