package de.woerteler.test.grammar;

import static de.woerteler.grammar.Terminal.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import java_cup.runtime.Symbol;

import org.junit.Test;

import de.woerteler.grammar.Lexer;
import de.woerteler.grammar.Terminal;

/**
 * Tests the grammar lexer.
 * 
 * @author Leo Woerteler
 */
public class LexerTest {
  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void test() throws IOException {
    run("S -> NP VP .", new int[] { NON_TERMINAL, TO, NON_TERMINAL,
        NON_TERMINAL, DOT}, new String[] { "S", null, "NP", "VP", null});
  }

  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void orTest() throws IOException {
    run("VP -> V | V NP .", new int[] { NON_TERMINAL, TO, NON_TERMINAL, OR,
        NON_TERMINAL, NON_TERMINAL, DOT}, new String[] { "VP", null, "V", null,
        "V", "NP", null});
  }

  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void modTest() throws IOException {
    run("NP -> D? A* N.", new int[] { NON_TERMINAL, TO, NON_TERMINAL, Q_MARK,
        NON_TERMINAL, STAR, NON_TERMINAL, DOT}, new String[] { "NP", null, "D",
        null, "A", null, "N", null});
  }

  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void parTest() throws IOException {
    run("NP -> N (P N)*.", new int[] { NON_TERMINAL, TO, NON_TERMINAL, PAR_L,
        NON_TERMINAL, NON_TERMINAL, PAR_R, STAR, DOT}, new String[] { "NP",
        null, "N", null, "P", "N", null, null, null});
  }

  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void commentTest() throws IOException {
    run("NP -> N // foobar \n (P N)*.", new int[] { NON_TERMINAL, TO,
        NON_TERMINAL, PAR_L, NON_TERMINAL, NON_TERMINAL, PAR_R, STAR, DOT},
        new String[] { "NP", null, "N", null, "P", "N", null, null, null});
  }

  /**
   * Tests if a simple production can be parsed.
   * 
   * @throws IOException in case of fire
   */
  @Test
  public void literalTest() throws IOException {
    run("V -> \"\\\"kissed\\\"\" | \"killed\" | \"hates\".",
 new int[] {
        NON_TERMINAL, TO, TERMINAL, OR, TERMINAL, OR, TERMINAL, DOT},
        new String[] { "V", null, "\"kissed\"", null, "killed", null, "hates",
        null});
  }

  /**
   * Test runner.
   * 
   * @param in input string
   * @param terms terminal IDs
   * @param vals bound values
   * @throws IOException in case of zombie apocalypse
   */
  private static void run(final String in, final int[] terms,
      final String[] vals) throws IOException {
    final Lexer lex = new Lexer(new StringReader(in));
    for(int i = 0; i < terms.length; i++) {
      final Symbol sym = lex.next_token();
      assertEquals(terms[i], sym.sym);
      assertEquals(vals[i], sym.value);
    }
    assertEquals(lex.next_token().sym, Terminal.EOF);
  }
}
