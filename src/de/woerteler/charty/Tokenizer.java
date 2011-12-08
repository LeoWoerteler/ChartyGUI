package de.woerteler.charty;


/**
 * Tokenizer for strings.
 * @author Leo Woerteler
 */
public final class Tokenizer {

  /** Hidden default constructor. */
  private Tokenizer() { /* void */ }

  /**
   * Tokenizes a string.
   * @param str string to tokenize
   * @return tokens
   */
  public static String[] tokenize(final String str) {
    return str.trim().split("\\s+");
  }

}
