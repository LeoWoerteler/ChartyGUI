package de.woerteler.grammar.ast;

/**
 * Terminal symbol.
 * 
 * @author Leo
 */
public class Terminal extends RHS {
  /** The terminal's string. */
  private final String string;

  /**
   * Constructor taking the terminal string.
   * 
   * @param str terminal string
   */
  public Terminal(final String str) {
    string = str;
  }

  @Override
  StringBuilder toString(final StringBuilder sb) {
    if(sb.length() > 0) {
      sb.append(' ');
    }
    return sb.append('"').append(
        string.replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
  }
}
