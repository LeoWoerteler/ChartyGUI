
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Wed Mar 14 18:48:19 CET 2012
//----------------------------------------------------

package de.woerteler.grammar;

import java.util.ArrayList;
import de.woerteler.grammar.ast.NonTerminal;
import de.woerteler.grammar.ast.Optional;
import de.woerteler.grammar.ast.Plus;
import de.woerteler.grammar.ast.Production;
import de.woerteler.grammar.ast.RHS;
import de.woerteler.grammar.ast.Sequence;
import de.woerteler.grammar.ast.Star;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Wed Mar 14 18:48:19 CET 2012
  */
public class Parser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\022\000\002\011\004\000\002\002\004\000\002\011" +
    "\003\000\002\010\006\000\002\006\005\000\002\006\003" +
    "\000\002\004\003\000\002\004\003\000\002\005\003\000" +
    "\002\005\003\000\002\005\004\000\002\005\004\000\002" +
    "\005\004\000\002\005\005\000\002\007\004\000\002\007" +
    "\004\000\002\002\003\000\002\003\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\032\000\004\014\006\001\002\000\004\011\012\001" +
    "\002\000\004\002\011\001\002\000\026\004\ufff1\005\ufff1" +
    "\006\ufff1\007\ufff1\010\ufff1\011\ufff1\012\ufff1\013\ufff1\014" +
    "\ufff1\015\ufff1\001\002\000\006\002\uffff\014\006\001\002" +
    "\000\004\002\001\001\002\000\004\002\000\001\002\000" +
    "\010\004\016\014\006\015\021\001\002\000\024\004\ufff8" +
    "\005\ufff8\006\ufff8\007\ufff8\010\ufff8\012\ufff8\013\ufff8\014" +
    "\ufff8\015\ufff8\001\002\000\024\004\016\005\ufffa\006\033" +
    "\007\032\010\031\012\ufffa\013\ufffa\014\006\015\021\001" +
    "\002\000\016\004\016\005\ufffb\012\ufffb\013\ufffb\014\006" +
    "\015\021\001\002\000\010\004\016\014\006\015\021\001" +
    "\002\000\006\012\ufffc\013\ufffc\001\002\000\024\004\ufff9" +
    "\005\ufff9\006\ufff9\007\ufff9\010\ufff9\012\ufff9\013\ufff9\014" +
    "\ufff9\015\ufff9\001\002\000\024\004\ufff0\005\ufff0\006\ufff0" +
    "\007\ufff0\010\ufff0\012\ufff0\013\ufff0\014\ufff0\015\ufff0\001" +
    "\002\000\006\012\023\013\024\001\002\000\010\004\016" +
    "\014\006\015\021\001\002\000\006\002\ufffe\014\ufffe\001" +
    "\002\000\006\012\ufffd\013\ufffd\001\002\000\004\005\027" +
    "\001\002\000\024\004\ufff4\005\ufff4\006\ufff4\007\ufff4\010" +
    "\ufff4\012\ufff4\013\ufff4\014\ufff4\015\ufff4\001\002\000\024" +
    "\004\ufff3\005\ufff3\006\033\007\032\010\031\012\ufff3\013" +
    "\ufff3\014\ufff3\015\ufff3\001\002\000\024\004\ufff5\005\ufff5" +
    "\006\ufff5\007\ufff5\010\ufff5\012\ufff5\013\ufff5\014\ufff5\015" +
    "\ufff5\001\002\000\024\004\ufff6\005\ufff6\006\ufff6\007\ufff6" +
    "\010\ufff6\012\ufff6\013\ufff6\014\ufff6\015\ufff6\001\002\000" +
    "\024\004\ufff7\005\ufff7\006\ufff7\007\ufff7\010\ufff7\012\ufff7" +
    "\013\ufff7\014\ufff7\015\ufff7\001\002\000\024\004\ufff2\005" +
    "\ufff2\006\033\007\032\010\031\012\ufff2\013\ufff2\014\ufff2" +
    "\015\ufff2\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\032\000\010\002\003\010\006\011\004\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\010" +
    "\002\003\010\006\011\007\001\001\000\002\001\001\000" +
    "\002\001\001\000\016\002\017\003\012\004\016\005\013" +
    "\006\021\007\014\001\001\000\002\001\001\000\010\002" +
    "\017\003\012\005\033\001\001\000\010\002\017\003\012" +
    "\005\027\001\001\000\014\002\017\003\012\004\025\005" +
    "\013\007\014\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\014\002\017\003" +
    "\012\004\024\005\013\007\014\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
 return getScanner().next_token(); 
    }




}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // term ::= TERMINAL 
            {
              RHS RESULT =null;
		String t = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new de.woerteler.grammar.ast.Terminal(t); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("term",1, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // nonTerminal ::= NON_TERMINAL 
            {
              NonTerminal RESULT =null;
		String n = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new NonTerminal(n); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("nonTerminal",0, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // sequence ::= symbol symbol 
            {
              ArrayList<RHS> RESULT =null;
		RHS a = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RHS b = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new ArrayList<RHS>(); RESULT.add(a); RESULT.add(b); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("sequence",5, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // sequence ::= sequence symbol 
            {
              ArrayList<RHS> RESULT =null;
		ArrayList<RHS> s = (ArrayList<RHS>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = s; RESULT.add(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("sequence",5, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // symbol ::= PAR_L rhs PAR_R 
            {
              RHS RESULT =null;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = r; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // symbol ::= symbol Q_MARK 
            {
              RHS RESULT =null;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new Optional(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // symbol ::= symbol STAR 
            {
              RHS RESULT =null;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new Star(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // symbol ::= symbol PLUS 
            {
              RHS RESULT =null;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new Plus(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // symbol ::= term 
            {
              RHS RESULT =null;
		RHS t = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = t; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // symbol ::= nonTerminal 
            {
              RHS RESULT =null;
		NonTerminal n = (NonTerminal)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = n; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("symbol",3, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // rhs ::= symbol 
            {
              RHS RESULT =null;
		RHS s = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = s; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rhs",2, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // rhs ::= sequence 
            {
              RHS RESULT =null;
		ArrayList<RHS> s = (ArrayList<RHS>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Sequence(s.toArray(new RHS[s.size()])); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rhs",2, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // rhss ::= rhs 
            {
              ArrayList<RHS> RESULT =null;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new ArrayList<RHS>(); RESULT.add(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rhss",4, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // rhss ::= rhss OR rhs 
            {
              ArrayList<RHS> RESULT =null;
		ArrayList<RHS> rs = (ArrayList<RHS>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		RHS r = (RHS)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = rs;                   RESULT.add(r); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rhss",4, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // production ::= nonTerminal TO rhss DOT 
            {
              Production RESULT =null;
		NonTerminal l = (NonTerminal)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		ArrayList<RHS> r = (ArrayList<RHS>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new Production(l, r.toArray(new RHS[r.size()])); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("production",6, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // grammar ::= production 
            {
              ArrayList<Production> RESULT =null;
		Production p = (Production)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new ArrayList<Production>(); RESULT.add(p); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("grammar",7, RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= grammar EOF 
            {
              Object RESULT =null;
		ArrayList<Production> start_val = (ArrayList<Production>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // grammar ::= production grammar 
            {
              ArrayList<Production> RESULT =null;
		Production p = (Production)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		ArrayList<Production> g = (ArrayList<Production>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = g;                           RESULT.add(p); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("grammar",7, RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

