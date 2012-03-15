package de.woerteler.gui;

import static de.woerteler.gui.GUIActions.ActionID.*;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * This {@link JPanel} contains the parsed phrase.
 * 
 * @author Leo Woerteler
 */
public final class PhraseInput extends JPanel {

  /** Serial Version UID. */
  private static final long serialVersionUID = 6734749240715465060L;

  /**
   * The example text that is loaded on start-up. Should be parse-able with the
   * example grammar.
   */
  public static final String EXAMPLE_TEXT = "Mary killed the man with the tie";

  /** The input text field. */
  private final JTextField input;

  /**
   * Constructor.
   * 
   * @param ctrl controller
   */
  public PhraseInput(final Controller ctrl) {
    setLayout(new BorderLayout());

    input = new JTextField();
    input.getInputMap().
    put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), PARSE);
    input.getActionMap().put(PARSE, ctrl.getActionFor(PARSE));

    input.setText(EXAMPLE_TEXT);
    add(input, BorderLayout.CENTER);

    add(new JButton(ctrl.getActionFor(PARSE)), BorderLayout.LINE_END);
    ctrl.setPhraseInput(this);
  }

  /** Requests the focus for the input text field. */
  public void focus() {
    input.requestFocusInWindow();
  }

  /**
   * Invokes the parsing.
   * 
   * @param ctrl Controller
   */
  public void parse(final Controller ctrl) {
    final Document doc = input.getDocument();
    try {
      ctrl.parse(doc.getText(0, doc.getLength()));
    } catch(final BadLocationException e1) {
      e1.printStackTrace();
    }
  }

}
