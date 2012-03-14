package de.woerteler.grammar.ast;

/**
 * Non-terminal symbol.
 * 
 * @author Leo Woerteler
 */
public class NonTerminal extends RHS {
  /** The non-terminal's name. */
  private final String name;

  /**
   * Constructor taking the name.
   * 
   * @param nm name
   */
  public NonTerminal(final String nm) {
    name = nm;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    if(sb.length() > 0) {
      sb.append(' ');
    }
    return sb.append(name);
  }
}
