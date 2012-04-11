package de.woerteler.gui;

import static de.woerteler.gui.ChartyGUI.*;
import static de.woerteler.gui.GUIActions.ActionID.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import de.woerteler.charty.ChartParser;
import de.woerteler.charty.DisplayMethod;
import de.woerteler.charty.Displayer;
import de.woerteler.charty.Grammar;
import de.woerteler.charty.GrammarSyntaxException;
import de.woerteler.charty.ParseTree;
import de.woerteler.charty.ParserException;
import de.woerteler.charty.ParserInfoListener;
import de.woerteler.charty.Tokenizer;
import de.woerteler.gui.GUIActions.ActionID;
import de.woerteler.latex.LaTeXDisplay;
import de.woerteler.tree.DirectDisplay;
import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.tree.render.NodeRenderer;
import de.woerteler.tree.strategy.BottomUpStrategy;
import de.woerteler.tree.strategy.TreeStrategy;
import de.woerteler.util.IOUtils;

/**
 * The controller class. The controller should be used to alter the gui
 * environment.
 * 
 * @author Leo Woerteler
 */
public final class Controller implements ParserInfoListener {

  /**
   * A custom renderer as defined in the ini file.
   */
  public static final NodeRenderer CUSTOM_RENDERER = INI.getInstance("custom",
      "renderer", NodeRenderer.class, "");

  /**
   * A custom strategy as defined in the ini file.
   */
  public static final TreeStrategy CUSTOM_STRATEGY = INI.getInstance("custom",
      "strategy", TreeStrategy.class, "");

  /** The info text for saving editor changes when closing the window. */
  private static final String SAVE_INFO = "<html>The grammar has unsaved changes." +
      "<br>Would you like to save it now?";

  /** The holder of all actions. */
  private final GUIActions actions;

  /** The data model. */
  private final DataModel model;

  /** The GUI. */
  private final ChartyGUI gui;

  /** The renderer menu item lookup. */
  private final Map<String, JRadioButtonMenuItem> displayMenu;

  /** Lock for the parse method. */
  private final Object parseLock = new Object();

  /** The syntax tree render method. */
  private NodeRenderer renderer = INI.getInstance("last", "renderer", NodeRenderer.class,
      DefaultRenderer.class);

  /** The syntax tree positioning strategy. */
  private TreeStrategy strategy = INI.getInstance("last", "strategy", TreeStrategy.class,
      BottomUpStrategy.class);

  /** Whether to use the LaTeX display method. */
  private boolean latexMethod = INI.getBoolean("last", "latex");

  /** The method to display the syntax tree. */
  private DisplayMethod method;

  /**
   * Creates a display method out of the configuration.
   * 
   * @param renderer The node renderer.
   * @param strategy The node placing strategy.
   * @param latexMethod Whether to use the LaTeX display method.
   * @return The created display method.
   */
  private static DisplayMethod createMethod(final NodeRenderer renderer,
      final TreeStrategy strategy, final boolean latexMethod) {
    return latexMethod ? new LaTeXDisplay() : new DirectDisplay(renderer, strategy);
  }

  /**
   * Constructor taking the application's {@link DataModel model}.
   * 
   * @param mod data model
   * @param g gui
   */
  public Controller(final ChartyGUI g, final DataModel mod) {
    gui = g;
    model = mod;
    actions = new GUIActions(this);
    displayMenu = new HashMap<String, JRadioButtonMenuItem>();
    setMethod();
  }

  /**
   * Getter.
   * 
   * @param id The action id.
   * @return The action associated with the given id.
   */
  public Action getActionFor(final ActionID id) {
    return actions.getAction(id);
  }

  /**
   * Sets the LaTeX render method.
   */
  public void setLaTeXMethod() {
    latexMethod = true;
    setMethod();
  }

  /**
   * Sets the current tree display method.
   */
  private void setMethod() {
    method = createMethod(renderer, strategy, latexMethod);
    refresh();
    checkDisplayMenu();
  }

  /**
   * Sets the strategy to position nodes of the syntax tree. This clears the
   * LaTeX flag.
   * 
   * @param strategy The new strategy.
   */
  public void setStrategy(final TreeStrategy strategy) {
    latexMethod = false;
    this.strategy = strategy;
    setMethod();
  }

  /**
   * Sets the syntax tree render method. This clears the LaTeX flag.
   * 
   * @param renderer The new renderer.
   */
  public void setRenderer(final NodeRenderer renderer) {
    latexMethod = false;
    this.renderer = renderer;
    setMethod();
  }

  /**
   * Registers a display menu item.
   * 
   * @param r The associated class.
   * @param item The item.
   */
  public void registerDisplayMenuItem(final Class<?> r,
      final JRadioButtonMenuItem item) {
    final String name = r.getName();
    displayMenu.put(name, item);
    checkDisplayMenu();
  }

  /**
   * Ensures that the display menu selection are correct, i.e. the correct
   * renderer, the correct strategy, or the latex button is selected.
   */
  private void checkDisplayMenu() {
    if(!latexMethod) {
      if(renderer != null) {
        final String name = renderer.getClass().getName();
        if(displayMenu.containsKey(name)) {
          displayMenu.get(name).setSelected(true);
        }
      }
      if(strategy != null) {
        final String name = strategy.getClass().getName();
        if(displayMenu.containsKey(name)) {
          displayMenu.get(name).setSelected(true);
        }
      }
    } else {
      final String ri = NodeRenderer.class.getName();
      if(displayMenu.containsKey(ri)) {
        displayMenu.get(ri).setSelected(true);
      }
      final String si = TreeStrategy.class.getName();
      if(displayMenu.containsKey(si)) {
        displayMenu.get(si).setSelected(true);
      }
    }
    final String name = LaTeXDisplay.class.getName();
    if(displayMenu.containsKey(name)) {
      displayMenu.get(name).setSelected(latexMethod);
    }
  }

  /**
   * Closes the current grammar. Asks to save the file if the grammar has
   * unsaved changes. The close operation can be aborted.
   * 
   * @return Whether to abort the close operation.
   */
  public boolean closeGrammar() {
    if(!model.grammarHasChanged()) return false;
    final int result = JOptionPane.showConfirmDialog(gui, SAVE_INFO,
        "Unsaved changes...", JOptionPane.YES_NO_CANCEL_OPTION);
    if(result == JOptionPane.CANCEL_OPTION) return true;
    if(result == JOptionPane.YES_OPTION) {
      // since it is our own action we are safe to pass no action event
      getActionFor(GRAMMAR_SAVE).actionPerformed(null);
    }
    return false;
  }

  /** Saves the currently open grammar definition. */
  public void saveGrammar() {
    saveGrammar(model.getOpenedFile());
  }

  /** Opens a save file dialog to save the grammar definition. */
  public void saveGrammarAs() {
    saveGrammar(null);
  }

  /**
   * Saves the currently opened grammar to the given file or opens a save file
   * dialog if the argument is <code>null</code>.
   * 
   * @param file The file to save the grammar to or <code>null</code> to open a
   *          save file dialog.
   */
  private void saveGrammar(final File file) {
    final File f = (file == null) ? gui.saveGrammarDialog(model.getOpenedFile()) : file;
    if(f == null) return;

    final String content = model.getGrammar();
    try {
      IOUtils.writeString(f, content);
    } catch(final IOException e) {
      gui.showError("Error writing grammar: " + e.getMessage());
      return;
    }

    model.setOpenedFile(f, content);
  }

  /** Opens a new grammar definition. */
  public void openGrammar() {
    if(closeGrammar()) return;
    final File f = gui.chooseGrammarDialog(model.getOpenedFile());
    if(f == null) return;

    try {
      model.setOpenedFile(f);
    } catch(final IOException e) {
      gui.showError("Can't open file: " + e.getMessage());
      return;
    }
  }

  /**
   * Opens the default grammar not associated with a file. The default grammar
   * is a small example grammar.
   */
  public void newGrammar() {
    if(closeGrammar()) return;
    try {
      model.setDefaultGrammar();
    } catch(final IOException e) {
      gui.showError("Can't open default grammar: " + e.getMessage());
    }
  }

  /**
   * Navigates within the parse trees.
   * 
   * @param next direction flag
   */
  public void navigate(final boolean next) {
    final int pos = model.getParseTreePos();
    final ParseTree[] trees = model.getParseTrees();

    if(!next && pos <= 0 || next && pos >= trees.length - 1) return;

    showTree(pos + (next ? 1 : -1));
  }

  /**
   * Ensures that the current syntax tree is redrawn.
   */
  public void refresh() {
    showTree(model.getParseTreePos());
  }

  /**
   * Shows the n'th syntax tree.
   * 
   * @param pos The position of the syntax tree in the list of parse trees.
   */
  public void showTree(final int pos) {
    final ParseTree[] trees = model.getParseTrees();
    if(pos < 0 || pos >= trees.length) return;
    try {
      final Displayer disp = trees[pos].getDisplayer(method);
      model.newParseTreePos(pos, disp);
    } catch(final Exception e) {
      gui.showError("Couldn't open parse tree:\n" + e.getMessage());
      model.newParseTreePos(0, null);
    }
  }

  /**
   * Parses the given phrase.
   * 
   * @param text phrase
   */
  public void parse(final String text) {
    if(text.trim().isEmpty()) {
      gui.showError("Parser error:\nEmpty phrase");
      return;
    }

    // make sure objects don't change during parsing
    final Object pl = parseLock;
    final DataModel m = model;
    final ChartyGUI cg = gui;
    final Thread t = new Thread() {

      @Override
      public void run() {
        synchronized(pl) {
          m.clearInfo();
          final String g = m.getGrammar();
          ParseTree[] trees = new ParseTree[0];
          try {
            trees = ChartParser.parse(new Grammar(new StringReader(g)),
                Tokenizer.tokenize(text), Controller.this);
            INI.set("last", "phrase", text);
          } catch(final ParserException e) {
            cg.showError("Parser error:\n" + e.getMessage());
          } catch(final GrammarSyntaxException e) {
            cg.showError("Grammar error:\n" + e.getMessage());
          }
          m.setParseTrees(trees);
          if(trees.length == 0) {
            m.newParseTreePos(0, null);
            return;
          }

          showTree(0);
        }
      }

    };
    t.start();
  }

  @Override
  public void info(final String category, final String message) {
    model.addInfo(category, message);
  }

  /**
   * Saves the current view on the syntax tree.
   */
  public void saveView() {
    if(!gui.canSaveView()) return;
    final File file = gui.saveViewDialog();
    if(file == null) return;
    gui.saveView(file);
  }

  /**
   * Saves the current syntax tree.
   */
  public void saveTree() {
    if(!gui.canSaveView()) return;
    final File file = gui.saveViewDialog();
    if(file == null) return;
    gui.saveTree(file);
  }

  /** The input phrase component. */
  private PhraseInput phraseInput;

  /**
   * Setter.
   * 
   * @param phraseInput The input phrase component.
   */
  public void setPhraseInput(final PhraseInput phraseInput) {
    this.phraseInput = phraseInput;
  }

  /**
   * Parses the given input phrase.
   */
  public void parse() {
    phraseInput.parse(this);
  }

  /**
   * Exits the program by closing the main window.
   */
  public void exit() {
    gui.dispose();
  }

  /**
   * Refreshes the values in the ini file.
   */
  public void refreshIniValues() {
    INI.setInstance("last", "renderer", renderer);
    INI.setInstance("last", "strategy", strategy);
    INI.setBoolean("last", "latex", latexMethod);
  }

}
