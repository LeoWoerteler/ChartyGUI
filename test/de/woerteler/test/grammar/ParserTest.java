package de.woerteler.test.grammar;

import java.io.StringReader;

import java_cup.runtime.Symbol;

import org.junit.Test;

import de.woerteler.grammar.Lexer;
import de.woerteler.grammar.Parser;

public class ParserTest {
  /**
   * @throws Exception
   */
  @Test
  public void test() throws Exception {
    final Parser p = new Parser(new Lexer(new StringReader("S -> (NP VP) VP \n| V.")));
    final Symbol sym = p.parse();
    System.out.println(sym.value);
  }
}
