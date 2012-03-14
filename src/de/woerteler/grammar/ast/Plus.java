package de.woerteler.grammar.ast;

/**
 * The {@code +} modifier, {@code A+} is equivalent to {@code AA*}.
 * 
 * @author Leo Woerteler
 */
public class Plus extends RHS {
  /** The child expression. */
  private final RHS child;

  /**
   * Constructor taking the child expression.
   * 
   * @param sub child expression
   */
  public Plus(final RHS sub) {
    child = sub;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    return child.toString(sb).append('+');
  }
}
