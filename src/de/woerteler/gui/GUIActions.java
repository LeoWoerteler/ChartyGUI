package de.woerteler.gui;

import static de.woerteler.gui.ChartyGUI.*;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.woerteler.latex.LatexDisplay;
import de.woerteler.tree.DirectDisplay;
import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.tree.render.SimpleRenderer;

/**
 * A class containing all GUI interactions.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class GUIActions {

  /**
   * The ids for the actions.
   * 
   * @author Joschi <josua.krause@googlemail.com>
   */
  public static enum ActionID {
    /** Opens a grammar file. */
    GRAMMAR_OPEN,

    /** Saves the current grammar. */
    GRAMMAR_SAVE,

    /** Saves the current grammar with a save file dialog. */
    GRAMMAR_SAVE_AS,

    /** Sets to the default syntax tree display method. */
    DISPLAY_DEFAULT,

    /** Sets to a boxed syntax tree display method. */
    DISPLAY_BOX,

    /** Sets to the latex syntax tree display method. */
    DISPLAY_LATEX,

    /** Saves the current view. */
    VIEW_SAVE,

    /** Shows the next view. */
    VIEW_NEXT,

    /** Shows the previous view. */
    VIEW_PREV,

    /** Parses the input phrase. */
    PARSE,

    /** Exits the program. */
    EXIT,
  }

  /** The map holding all actions. */
  private final Map<ActionID, Action> actionMap = new HashMap<ActionID, Action>();

  /**
   * Generates actions for the given controller.
   * 
   * @param ctrl The controller.
   */
  public GUIActions(final Controller ctrl) {
    fillActionMap(ctrl);
  }

  /**
   * Fills the action map.
   * 
   * @param ctrl The controller.
   */
  private void fillActionMap(final Controller ctrl) {
    actionMap.put(ActionID.DISPLAY_DEFAULT, new AbstractAction("Direct Drawing") {

      private static final long serialVersionUID = -7100863959215774798L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setMethod(new DirectDisplay(new DefaultRenderer()));
      }

    });
    actionMap.put(ActionID.DISPLAY_BOX, new AbstractAction("Direct Drawing (Boxes)") {

      private static final long serialVersionUID = -6511102668204333823L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setMethod(new DirectDisplay(new SimpleRenderer()));
      }

    });
    actionMap.put(ActionID.DISPLAY_LATEX, new AbstractAction("LaTeX Drawing") {

      private static final long serialVersionUID = -309282807664400632L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setMethod(new LatexDisplay());
      }

    });
    actionMap.put(ActionID.VIEW_SAVE, new AbstractAction("Save current view...") {

      private static final long serialVersionUID = -1487104580494952919L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.saveView();
      }

    });
    actionMap.put(ActionID.GRAMMAR_SAVE, new AbstractAction("Save grammar",
        icon("save")) {

      private static final long serialVersionUID = -8903820399182467464L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.saveGrammar();
      }

    });
    actionMap.put(ActionID.GRAMMAR_SAVE_AS, new AbstractAction("Save grammar as...") {

      private static final long serialVersionUID = -4823837786555270274L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.saveGrammarAs();
      }

    });
    actionMap.put(ActionID.GRAMMAR_OPEN,
        new AbstractAction("Open grammar", icon("open")) {

      private static final long serialVersionUID = -5029782910302902656L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.openGrammar();
      }

    });
    actionMap.put(ActionID.VIEW_NEXT,
        new AbstractAction("Next", icon("arrow_right")) {

      private static final long serialVersionUID = -9214877448093102266L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.navigate(true);
      }

    });
    actionMap.put(ActionID.VIEW_PREV, new AbstractAction("Previous",
        icon("arrow_left")) {

      private static final long serialVersionUID = -1884743080405185742L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.navigate(false);
      }

    });
    actionMap.put(ActionID.PARSE, new AbstractAction("Parse") {

      private static final long serialVersionUID = 1033487881819066274L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.parse();
      }

    });
    actionMap.put(ActionID.EXIT, new AbstractAction("Close") {

      private static final long serialVersionUID = 534484243507794332L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.exit();
      }

    });
  }

  /**
   * Getter.
   * 
   * @param id The action id.
   * @return The corresponding action.
   */
  public Action getAction(final ActionID id) {
    return actionMap.get(id);
  }

}
