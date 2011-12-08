package de.woerteler.charty;

/**
 * Interface for receivers of info messages from the parser.
 *
 * @author Leo Woerteler
 */
public interface ParserInfoListener {

  /**
   * Method that's called when the parser emits an info message.
   *
   * @param category category of the message
   * @param message the message
   */
  void info(final String category, final String message);

}
