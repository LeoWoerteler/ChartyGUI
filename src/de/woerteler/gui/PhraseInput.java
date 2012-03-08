package de.woerteler.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

  /** The input text field. */
  private final JTextField input;

  /**
   * Constructor.
   *
   * @param ctrl controller
   */
  PhraseInput(final Controller ctrl) {
    setLayout(new BorderLayout());

    input = new JTextField();
    input.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          parse(ctrl);
        }
      }
    });
    input.setText("Mary killed the man with the tie");
    add(input, BorderLayout.CENTER);

    final JButton parse = new JButton("Parse");
    parse.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        parse(ctrl);
      }
    });
    add(parse, BorderLayout.LINE_END);
  }

  /** Requests the focus for the input text field. */
  void focus() {
    input.requestFocusInWindow();
  }

  /**
   * Invopes the parsing.
   *
   * @param ctrl Controller
   */
  void parse(final Controller ctrl) {
    final Document doc = input.getDocument();
    try {
      ctrl.parse(doc.getText(0, doc.getLength()));
    } catch (final BadLocationException e1) {
      e1.printStackTrace();
    }
  }
}
