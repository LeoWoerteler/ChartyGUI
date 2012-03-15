package de.woerteler.gui;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

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
import de.woerteler.tree.DirectDisplay;
import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.util.IOUtils;

/**
 * The controller class.
 * 
 * @author Leo Woerteler
 */
public final class Controller implements ParserInfoListener {

  /** The holder of all actions. */
  private final GUIActions actions;

  /** The data model. */
  final DataModel model;

  /** The GUI. */
  final ChartyGUI gui;

  /** Lock for the parse method. */
  final Object parseLock = new Object();

  /** The method to display the syntax tree. */
  DisplayMethod method = new DirectDisplay(new DefaultRenderer());

  /**
   * Constructor taking the application's {@link DataModel model}.
   * 
   * @param mod data model
   * @param g gui
   */
  Controller(final ChartyGUI g, final DataModel mod) {
    gui = g;
    model = mod;
    actions = new GUIActions(this);
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
   * Setter.
   * 
   * @param method The display method.
   */
  public void setMethod(final DisplayMethod method) {
    this.method = method;
    refresh();
  }

  /**
   * Getter.
   * 
   * @return The current display method.
   */
  public DisplayMethod getMethod() {
    return method;
  }

  /** Saves the currently open grammar definition. */
  void saveFile() {
    final File f = model.getOpenedFile();
    if(f == null) {
      gui.showError("There's no open file to save!");
    }
  }

  /** Opens a new grammar definition. */
  void openFile() {
    File dir = null;
    final File curr = model.getOpenedFile();
    if(curr != null) {
      dir = curr.getParentFile();
    } else {
      final File home = new File(System.getProperty("user.home"));
      if(home.exists()) {
        dir = home;
      }
    }
    final File f = gui.chooseFile(dir);
    if(f == null) return;

    byte[] contents;
    try {
      contents = IOUtils.readFile(f);
    } catch(final IOException e) {
      gui.showError("Can't open file: " + e.getMessage());
      return;
    }

    model.setOpenedFile(f, new String(contents, Charset.forName("UTF-8")));
  }

  /**
   * Navigates within the parse trees.
   * 
   * @param next direction flag
   */
  void navigate(final boolean next) {
    final int pos = model.getParseTreePos();
    final ParseTree[] trees = model.getParseTrees();

    if(!next && pos == 0 || next && pos >= trees.length) return;

    int npos;
    if(next) {
      npos = pos + 1;
    } else {
      npos = pos - 1;
    }

    try {
      final Displayer disp = trees[npos].getDisplayer(method);
      model.newParseTreePos(npos, disp);
    } catch(final Exception e) {
      gui.showError("Couldn't open parse tree:\n" + e.getMessage());
    }
  }

  /**
   * Ensures that the current syntax tree is redrawn.
   */
  public void refresh() {
    final int pos = model.getParseTreePos();
    final ParseTree[] trees = model.getParseTrees();
    if(pos >= trees.length) return;
    try {
      final Displayer disp = trees[pos].getDisplayer(method);
      model.newParseTreePos(pos, disp);
    } catch(final Exception e) {
      gui.showError("Couldn't open parse tree:\n" + e.getMessage());
    }
  }

  /**
   * Parses the given phrase.
   * 
   * @param text phrase
   */
  void parse(final String text) {
    if(text.trim().isEmpty()) {
      gui.showError("Parser error:\nEmpty phrase");
      return;
    }

    final Thread t = new Thread() {
      @Override
      public void run() {
        synchronized(parseLock) {
          model.clearInfo();
          final String g = model.getGrammar();
          ParseTree[] trees = new ParseTree[0];
          try {
            trees = ChartParser.parse(new Grammar(new StringReader(g)),
                Tokenizer.tokenize(text), Controller.this);
          } catch(final ParserException e) {
            gui.showError("Parser error:\n" + e.getMessage());
          } catch(final GrammarSyntaxException e) {
            gui.showError("Grammar error:\n" + e.getMessage());
          }
          model.setParseTrees(trees);
          if(trees.length == 0) {
            model.newParseTreePos(0, null);
            return;
          }

          try {
            final Displayer disp = trees[0].getDisplayer(method);
            model.newParseTreePos(0, disp);
          } catch(final Exception e) {
            model.newParseTreePos(0, null);
            gui.showError("Couldn't open parse tree:\n" + e.getMessage());
          }
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
    final JFileChooser choose = new JFileChooser();
    choose.addChoosableFileFilter(new FileFilter() {

      @Override
      public String getDescription() {
        return "Image (*.png, *.jpg, *.jpeg)";
      }

      @Override
      public boolean accept(final File f) {
        final String name = f.getName();
        return !f.isFile() || name.endsWith(".png") || name.endsWith(".jpg")
            || name.endsWith(".jpeg");
      }
    });
    if(choose.showSaveDialog(gui) != JFileChooser.APPROVE_OPTION) return;
    final File file = choose.getSelectedFile();
    File dest;
    if(!file.getName().contains(".")) {
      dest = new File(file.getParentFile(), file.getName() + ".png");
    } else {
      dest = file;
    }
    gui.saveView(dest);
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

}
