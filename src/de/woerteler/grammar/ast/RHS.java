package de.woerteler.grammar.ast;

/**
 * Superclass for AST nodes.
 * 
 * @author Leo Woerteler
 */
public abstract class RHS {
  /**
   * Recursive {@link Object#toString()} helper.
   * 
   * @param sb string builder
   * @return the string builder for convenience
   */
  abstract StringBuilder toString(final StringBuilder sb);
}
