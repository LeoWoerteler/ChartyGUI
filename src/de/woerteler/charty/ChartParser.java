package de.woerteler.charty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements a chart parser.
 *
 * @author Leo Woerteler
 */
public final class ChartParser {

  /** Grammar. */
  private final Grammar grammar;
  /** Tokens to parse. */
  final String[] tokens;

  /** Optional listener for parser events. */
  private final ParserInfoListener listener;

  /** List of edges. */
  private final List<Edge> chart = new ArrayList<Edge>();

  /**
   * Constructor taking the grammar an tokens to parse.
   * 
   * @param g grammar
   * @param tok tokens to parse
   * @param list info listener, may be {@code null}
   */
  private ChartParser(final Grammar g, final String[] tok,
      final ParserInfoListener list) {
    grammar = g;
    tokens = tok;
    listener = list;
  }

  /**
   * Parses a sequence of tokens.
   * 
   * @param g grammar definition
   * @param tok tokens to parse
   * @param listener info listener, may be {@code null}
   * @return generated parse trees
   * @throws ParserException if the parser isn't successful
   */
  public static ParseTree[] parse(final Grammar g, final String[] tok,
      final ParserInfoListener listener)
          throws ParserException {
    final List<ParseTree> trees = new ChartParser(g, tok, listener).parse();
    return trees.toArray(new ParseTree[trees.size()]);
  }

  /**
   * Performs the parse.
   *
   * @return list of parse trees
   * @throws ParserException in case of errors
   */
  private List<ParseTree> parse() throws ParserException {

    int pos = 0;
    for(int i = 0; i < tokens.length; i++) {
      final String tok = tokens[i];

      // initialize with input token
      for(final String lhs : grammar.getLHS(tok)) {
        chart.add(new Edge(i, i + 1, 1, lhs, new String[] { tok},
            new ArrayList<Edge>()));
        log("I", "Adding edge: " + chart.get(chart.size() - 1));
      }

      boolean change;
      do {
        final int len = chart.size();
        change = ruleInvocation(pos);
        pos = len;
        change |= fundamentalRule();
      } while(change);
    }

    final ArrayList<ParseTree> res = new ArrayList<ParseTree>();
    for(final Edge e : chart) {
      if(e.isActive()) {
        log("Chart", "Active: " + e);
      } else {
        log("Chart", "Inactive: " + e);
        if(e.isOverspanning()) {
          res.add(new ParseTree(e));
        }
      }

    }

    return res;
  }

  /**
   * The fundamental rule of chart parsing generates new edges by combining
   * fitting active and inactive edges.
   *
   * @return change flag
   */
  private boolean fundamentalRule() {
    boolean change = false;
    for(int i = 0; i < chart.size(); i++) {
      final Edge e = chart.get(i);
      if(e.isActive()) {
        for(int k = 0; k < chart.size(); k++) {
          final Edge e2 = chart.get(k);
          if(!e2.isActive() && e.matches(e2)) {
            final Edge nw = new Edge(e, e2);
            if(!chart.contains(nw)) {
              chart.add(nw);
              change = true;
              log("FR", "Adding edge: " + nw);
            }
          }
        }
      }
    }
    return change;
  }

  /**
   * Add all the rules of the grammar to the chart that are relevant: Find the
   * rule with the LHS of edge as the leftmost RHS symbol and maximally the
   * remaining length of the input.
   *
   * @param pos current position in the chart
   * @return change flag
   */
  private boolean ruleInvocation(final int pos) {
    boolean change = false;
    for(int i = pos; i < chart.size(); i++) {
      final Edge e = chart.get(i);
      if(!e.isActive()) {
        for(final String lhs : grammar.withLeftmost(e.lhs)) {
          for(final String[] rhs : grammar.rhs(lhs)) {
            if(!rhs[0].equals(e.lhs) || rhs.length > tokens.length - e.start) {
              continue;
            }
            final Edge nw = new Edge(e.start, e.end, 1, lhs, rhs,
                new ArrayList<Edge>(Arrays.asList(e)));
            if(!chart.contains(nw)) {
              chart.add(nw);
              change = true;
              log("IV", "Adding edge: " + nw);
            }
          }
        }
      }
    }
    return change;
  }

  /**
   * Logs a message.
   *
   * @param cat category
   * @param desc message
   */
  private void log(final String cat, final String desc) {
    if(listener != null) {
      listener.info(cat, desc);
    }
  }

  /**
   * An edge in the chart parser.
   *
   * @author Leo Woerteler
   */
  public final class Edge implements Iterable<Edge> {

    /** Prime number used as factor in hash-code calculation. */
    private static final int HASH_CODE_PRIME = 31;

    /** Start position of the edge. */
    final int start;
    /** End position of the edge. */
    final int end;
    /** position of the dot. */
    final int dot;

    /** Left hand side. */
    public final String lhs;
    /** Right hand side. */
    public final String[] rhs;

    /** Ancestors of this edge. */
    private final List<Edge> children;

    /**
     * Constructor.
     *
     * @param s start
     * @param e end
     * @param d dot position
     * @param lh left hand side
     * @param rh right hand side
     * @param kids children of this edger
     */
    Edge(final int s, final int e, final int d, final String lh,
        final String[] rh, final List<Edge> kids) {
      start = s;
      end = e;
      dot = d;
      lhs = lh;
      rhs = rh;
      children = kids;
    }

    /**
     * Combining constructor.
     *
     * @param a active edge
     * @param i inactive edge
     */
    Edge(final Edge a, final Edge i) {
      this(a.start, i.end, a.dot + 1, a.lhs, a.rhs.clone(),
          new ArrayList<Edge>(a.children));
      children.add(i);
    }

    /**
     * Checks whether this active edge matches the given inactive edge.
     *
     * @param inactive inactive edge
     * @return result of check
     */
    boolean matches(final Edge inactive) {
      return end == inactive.start && rhs[dot].equals(inactive.lhs);
    }

    /**
     * Checks whether this edge is still active.
     *
     * @return {@code true}, if this edge is active, {@code false} otherwise.
     */
    boolean isActive() {
      return dot < rhs.length;
    }

    /**
     * Checks whether this edge spans over the entire input sequence.
     *
     * @return {@code true}, if this edge is overspanning, {@code false}
     */
    boolean isOverspanning() {
      return start == 0 && end == tokens.length;
    }

    @Override
    public boolean equals(final Object obj) {
      if(!(obj instanceof Edge)) {
        return false;
      }
      final Edge o = (Edge) obj;
      return start == o.start && end == o.end && dot == o.dot
          && lhs.equals(o.lhs) && Arrays.equals(rhs, o.rhs)
          && children.equals(o.children);
    }

    @Override
    public int hashCode() {
      int res = 0;
      for(final int i : new int[] { start, end, dot, lhs.hashCode(),
          Arrays.hashCode(rhs), children.hashCode()}) {
        res = HASH_CODE_PRIME * res + i;
      }
      return res;
    }

    @Override
    public String toString() {
      return String.format("(%d, %d, %d, %s, %s)", start, end, dot, lhs,
          Arrays.toString(rhs));
    }

    /**
     * Creates a representation of this subtree in bracketing notation.
     *
     * @return representation
     */
    public String toLaTeX() {
      final StringBuilder sb = new StringBuilder();
      toLaTeX(sb, Collections.singletonList(this));
      return sb.toString().trim();
    }

    /**
     * Recursive {@link #toLaTeX()} helper.
     *
     * @param sb string builder for efficiency
     * @param kids list of children
     */
    private void toLaTeX(final StringBuilder sb, final List<Edge> kids) {
      for(final Edge c : kids) {
        sb.append(" [.").append(c.lhs);
        if(!c.children.isEmpty()) {
          toLaTeX(sb, c.children);
        } else {
          for(final String term : c.rhs) {
            sb.append(" ").append(term);
          }
        }
        sb.append(" ]");
      }
    }

    /**
     * Checks whether the edge has real children.
     * 
     * @return Whether the edge has real children or just text as children.
     */
    public boolean hasRealChildren() {
      return !children.isEmpty();
    }

    @Override
    public Iterator<Edge> iterator() {
      return children.iterator();
    }
  }

}
