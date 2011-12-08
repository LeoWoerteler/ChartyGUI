package de.woerteler.charty;

/**
 * This exception is thrown when the {@link GrammarReader} can't read a given
 * grammar.
 * 
 * @author Leo Woerteler
 */
public class GrammarSyntaxException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 7855486465397999969L;

	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public GrammarSyntaxException(final String message) {
		super(message);
	}

}
