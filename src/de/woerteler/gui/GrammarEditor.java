package de.woerteler.gui;

import static de.woerteler.gui.ChartyGUI.icon;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

  /** Save button. */
  private final JButton save;
  /** open button. */
  private final JButton open;

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
    save = new JButton(icon("save"));
    save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.saveFile();
      }
    });
    toolBar.add(save);

    // open button
    open = new JButton(icon("open"));
    open.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ctrl.openFile();
      }
    });
    toolBar.add(open);

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
