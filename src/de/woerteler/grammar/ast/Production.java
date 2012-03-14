package de.woerteler.grammar.ast;

/**
 * A grammar production, consisting of a left-hand side being a single
 * non-terminal and a list of right-hand sides.
 * 
 * @author Leo Woerteler
 */
public class Production {
  /** The left-hand side. */
  private final NonTerminal lhs;
  /** The right-hand sides. */
  private final RHS[] rhs;

  /**
   * Constructor.
   * 
   * @param lh left-hand side
   * @param rh right-hand side
   */
  public Production(final NonTerminal lh, final RHS[] rh) {
    lhs = lh;
    rhs = rh;
  }

  @Override
  public String toString() {
    final StringBuilder sb = lhs.toString(new StringBuilder());
    sb.append(" ->");
    for(int i = 0; i < rhs.length; i++) {
      if(i > 0) {
        sb.append(" |");
      }
      rhs[i].toString(sb);
    }
    return sb.append('.').toString();
  };
}
