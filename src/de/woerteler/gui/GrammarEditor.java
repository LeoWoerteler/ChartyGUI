package de.woerteler.gui;

import static de.woerteler.gui.GUIActions.ActionID.*;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.Document;

/**
 * This {@link JPanel} displays the grammar that's currently used.
 *
 * @author Leo Woerteler
 */
public final class GrammarEditor extends JPanel {

  /** Text area. */
  private final JTextArea area;

  /** Serial version UID. */
  private static final long serialVersionUID = 7554060907371987000L;

  /**
   * Default constructor.
   *
   * @param ctrl Controller
   */
  public GrammarEditor(final Controller ctrl) {
    super(new BorderLayout());
    final JToolBar toolBar = new JToolBar();
    toolBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    toolBar.setFloatable(false);

    // save button
    toolBar.add(new JButton(ctrl.getActionFor(GRAMMAR_SAVE)));

    // open button
    toolBar.add(new JButton(ctrl.getActionFor(GRAMMAR_OPEN)));

    add(toolBar, BorderLayout.PAGE_START);

    area = new JTextArea();
    add(new JScrollPane(area), BorderLayout.CENTER);
  }

  /**
   * Getter for the text area's document.
   *
   * @return the document
   */
  Document getDocument() {
    return area.getDocument();
  }

  /** Rewinds the caret position to the start of the document. */
  void rewind() {
    area.setCaretPosition(0);
  }

}
