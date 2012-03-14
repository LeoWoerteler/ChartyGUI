package de.woerteler.grammar.ast;

/**
 * Optional modifier.
 * 
 * @author Leo Woerteler
 */
public class Optional extends RHS {
  /** The child expression. */
  private final RHS child;

  /**
   * Constructor taking the child expression.
   * 
   * @param sub child expression
   */
  public Optional(final RHS sub) {
    child = sub;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    return child.toString(sb).append('?');
  }
}
