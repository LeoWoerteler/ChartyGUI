package de.woerteler.charty;

/**
 * This exception is thrown if the parser can't continue parsing because of
 * errors.
 *
 * @author Leo Woerteler
 */
public class ParserException extends Exception {

  /** Serial version UID. */
  private static final long serialVersionUID = -2214108043799364335L;

  /**
   * Constructs a new exception with the specified detail message. The cause
   * is not initialized, and may subsequently be initialized by a call to
   * {@link #initCause}.
   *
   * @param message
   *            the detail message. The detail message is saved for later
   *            retrieval by the {@link #getMessage()} method.
   */
  ParserException(final String message) {
    super(message);
  }

}
