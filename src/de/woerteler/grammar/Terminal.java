package de.woerteler.grammar;

/**
 * Terminals for the grammar parser.
 * 
 * @author Leo Woerteler
 */
public interface Terminal {
  /** Non-terminal. */
  int NON_TERMINAL = 0;
  /** Terminal. */
  int TERMINAL = 1;
  /** Opening parenthesis. */
  int PAR_L = 2;
  /** Closing parenthesis. */
  int PAR_R = 3;
  /** Plus operator. */
  int PLUS = 4;
  /** Kleene star. */
  int STAR = 5;
  /** Question mark operator. */
  int Q_MARK = 6;
  /** Separator between left-hand and right-hand side. */
  int TO = 7;
  /** Separqator between right-hand sides. */
  int OR = 8;
  /** Separator between productions. */
  int DOT = 9;
  /** End of file. */
  int EOF = 10;
  /** Error. */
  int error = -1;
}
