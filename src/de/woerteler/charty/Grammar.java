package de.woerteler.charty;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a context-free grammar.
 * 
 * @author Leo Woerteler
 */
public final class Grammar {

	/** A comparator for string arrays. */
	private static final Comparator<String[]> CMP = new Comparator<String[]>() {
		@Override
		public int compare(final String[] o1, final String[] o2) {
			for (int i = 0, ln = Math.min(o1.length, o2.length); i < ln; i++) {
				final int cmp = o1[i].compareTo(o2[i]);
				if (cmp != 0) {
					return cmp;
				}
			}
			return o1.length - o2.length;
		}
	};

	/** Production map. */
	private final HashMap<String, TreeSet<String[]>> productions;

	/** Map for lookup of productions . */
	private final Map<String, Set<String>> leftmost;

	/** Set of LHS with a single token as RHS. */
	private final Set<String> singletons = new HashSet<String>();

	/**
	 * Constructor.
	 * 
	 * @param in
	 *            reader for the definition
	 * @throws GrammarSyntaxException
	 *             if the definition can't be read
	 */
	public Grammar(final Reader in) throws GrammarSyntaxException {

		productions = new HashMap<String, TreeSet<String[]>>();
		leftmost = new HashMap<String, Set<String>>();

		final Scanner sc = new Scanner(in).useDelimiter("\\s*[\r\n]+\\s*");
		while (sc.hasNext()) {
			final String line = sc.next().trim();
			if (!line.startsWith("#")) {

				final String[] parts = line.split("\\s+");
				if (parts.length < 2 || !"->".equals(parts[1])) {
					throw new GrammarSyntaxException("Can't understand "
							+ "production '" + line + "'.");
				}

				final String lhs = parts[0];
				final String[] rhs = Arrays.copyOfRange(parts, 2, parts.length);
				if (rhs.length == 0) {
					throw new GrammarSyntaxException("Right hand side missing"
							+ " in '" + line + "'.");
				}

				if (!addProduction(lhs, rhs)) {
					throw new GrammarSyntaxException("Production '" + line
							+ "' is declared more than once.");
				}
			}
		}

		if (productions.isEmpty()) {
			throw new GrammarSyntaxException("There must be at least one"
					+ " production.");
		}
	}

	/**
	 * Looks up the left hand sides that can produce the given terminal.
	 * 
	 * @param rhs
	 *            terminal
	 * @return set of left hand sides
	 * @throws ParserException
	 *             if the terminal is unknown
	 */
	public Set<String> getLHS(final String rhs) throws ParserException {
		final Set<String> set = safe(leftmost.get(rhs));
		set.retainAll(singletons);
		if (set.isEmpty()) {
			throw new ParserException("Unknown terminal '" + rhs + "'.");
		}
		return set;
	}

	/**
	 * Set of all LHS that have a production that starts with the given symbol.
	 * 
	 * @param rhs
	 *            symbol
	 * @return set of left-hand sides
	 */
	public Set<String> withLeftmost(final String rhs) {
		return safe(leftmost.get(rhs));
	}

	/**
	 * Set of right-hand sides for the given left-hand side.
	 * 
	 * @param lhs
	 *            left-hand side to look for
	 * @return corresponding right-hand sides
	 */
	public Set<String[]> rhs(final String lhs) {
		return safe(productions.get(lhs));
	}

	/**
	 * Safety wrapper for replacing null references with the empty set.
	 * 
	 * @param <T>
	 *            type of the set
	 * @param set
	 *            the set, potentially {@code null}
	 * @return {@code null}-safe set
	 */
	private <T> Set<T> safe(final Set<T> set) {
		if (set == null) {
			return Collections.emptySet();
		}
		return set;
	}

	/**
	 * Adds a production to this grammar.
	 * 
	 * @param lhs
	 *            left-hand side
	 * @param rhs
	 *            right-hand side
	 * @return success flag
	 */
	private boolean addProduction(final String lhs, final String[] rhs) {
		// insert into production map
		TreeSet<String[]> set = productions.get(lhs);
		if (set == null) {
			set = new TreeSet<String[]>(CMP);
			productions.put(lhs, set);
		}
		// insert into singleton set
		if (rhs.length == 1) {
			singletons.add(lhs);
		}

		// insert into leftmost-token map
		Set<String> lefts = leftmost.get(rhs[0]);
		if (lefts == null) {
			lefts = new HashSet<String>();
			leftmost.put(rhs[0], lefts);
		}
		lefts.add(lhs);

		return set.add(rhs);
	}

}
