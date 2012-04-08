package de.woerteler.gui;

import static de.woerteler.gui.ChartyGUI.*;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.tree.render.SimpleRenderer;
import de.woerteler.tree.strategy.BottomUpStrategy;

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
    /** Opens a new grammar. */
    GRAMMAR_NEW,

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

    /** Sets to the bottom up syntax tree strategy. */
    DISPLAY_BOTTOM_UP,

    /** Sets to the latex syntax tree display method. */
    DISPLAY_LATEX,

    /** Sets to the custom renderer. */
    CUSTOM_RENDERER,

    /** Sets to the custom strategy. */
    CUSTOM_STRATEGY,

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
        ctrl.setRenderer(new DefaultRenderer());
      }

    });
    actionMap.put(ActionID.DISPLAY_BOX, new AbstractAction("Direct Drawing (Boxes)") {

      private static final long serialVersionUID = -6511102668204333823L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setRenderer(new SimpleRenderer());
      }

    });
    actionMap.put(ActionID.DISPLAY_BOTTOM_UP, new AbstractAction("Bottom-up strategy") {

      private static final long serialVersionUID = -309282807664400632L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setStrategy(new BottomUpStrategy());
      }

    });
    actionMap.put(ActionID.DISPLAY_LATEX, new AbstractAction("LaTeX Drawing") {

      private static final long serialVersionUID = -309282807664400632L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.setLaTeXMethod();
      }

    });
    actionMap.put(ActionID.CUSTOM_RENDERER, new AbstractAction("Custom Renderer") {

      private static final long serialVersionUID = -5448188090530488777L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        // FIXME:
      }

    });
    actionMap.put(ActionID.CUSTOM_STRATEGY, new AbstractAction("Custom Strategy") {

      private static final long serialVersionUID = 2422472290296174048L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        // FIXME:
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
    actionMap.put(ActionID.GRAMMAR_NEW, new AbstractAction("New grammar") {

      private static final long serialVersionUID = -2773817874878903577L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.newGrammar();
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
