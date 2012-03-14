package de.woerteler.grammar.ast;

/**
 * Sequence of at least two expressions.
 * 
 * @author Leo Woerteler
 */
public class Sequence extends RHS {
  /** Expressions. */
  private final RHS[] exprs;

  /**
   * Constructor taking the expressions.
   * 
   * @param expr subexpressions
   */
  public Sequence(final RHS... expr) {
    exprs = expr;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    final int pos = sb.length() + 1;
    for(int i = 0; i < exprs.length; i++) {
      exprs[i].toString(sb.append(' '));
    }
    sb.setCharAt(pos, '(');
    return sb.append(')');
  }
}
