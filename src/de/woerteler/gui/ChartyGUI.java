package de.woerteler.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.woerteler.charty.Displayer;
import de.woerteler.util.IOUtils;

/**
 * The JFrame containing the GUI application.
 * 
 * @author Leo Woerteler
 */
public final class ChartyGUI extends JFrame {

  /** The name of this application. */
  private static final String NAME = "ChartyGUI 0.1 α 1";

  /** Serial version UID. */
  private static final long serialVersionUID = 6440369013993516988L;

  /** Preferred width of the window. */
  private static final int WINDOW_WIDTH = 1024;
  /** Preferred height of the window. */
  private static final int WINDOW_HEIGHT = 768;

  /** Icon sizes provided. */
  private static final int[] ICON_SIZES = { 16, 32, 64, 128, 256};

  /** Cache for icons. */
  private static final HashMap<String, ImageIcon> ICON_MAP;
  static {
    ICON_MAP = new HashMap<String, ImageIcon>();
  }

  /** Parse tree viewer. */
  private final ParseTreeViewer treeViewer;

  /** GRammar editor. */
  private final GrammarEditor editor;

  /**
   * Constructor.
   */
  private ChartyGUI() {
    setTitle(null);
    setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // load logo
    final ArrayList<Image> icons = new ArrayList<Image>();
    try {
      for(final int size : ICON_SIZES) {
        final InputStream in = IOUtils.getResource("logo/" + size + ".png");
        icons.add(ImageIO.read(in));
      }
    } catch(final IOException e) {
      e.printStackTrace();
    }
    setIconImages(icons);

    final DataModel model = new DataModel(this);
    final Controller ctrl = new Controller(this, model);

    // menu bar
    final MenuBar menuBar = new MenuBar();
    setMenuBar(menuBar);
    final Menu fileMenu = new Menu("File");
    menuBar.add(fileMenu);

    // Left side: edit grammar and phrase
    editor = new GrammarEditor(ctrl);
    model.setDocument(editor.getDocument());
    final JPanel edit = new JPanel(new BorderLayout());
    edit.add(editor, BorderLayout.CENTER);
    final PhraseInput input = new PhraseInput(ctrl);
    edit.add(input, BorderLayout.PAGE_END);

    // Right side: view parse tree and parser output
    treeViewer = new ParseTreeViewer(ctrl);
    final JSplitPane view = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        treeViewer, new ParserInfoViewer(model.getInfoTableModel()));
    view.setDividerLocation(WINDOW_HEIGHT / 2);
    view.setOneTouchExpandable(true);
    view.setResizeWeight(1.0 / 2);

    try {
      final InputStream psg = IOUtils.getResource("PSG1.txt");
      final byte[] contents = IOUtils.readFully(psg);
      model.setOpenedFile(null, new String(contents, Charset.forName("UTF-8")));
    } catch(final IOException e) {
      e.printStackTrace();
    }

    // new content pane
    final JPanel content = new JPanel(new BorderLayout());
    setContentPane(content);

    // split the screen in two halfs
    final JSplitPane vertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        edit, view);
    vertical.setDividerLocation(WINDOW_WIDTH / 2);
    vertical.setResizeWeight(1.0 / 2);
    content.add(vertical, BorderLayout.CENTER);

    // display the window
    pack();
    setVisible(true);
    input.focus();
  }

  /**
   * Main method starting the GUI.
   * 
   * @param args ignored
   */
  public static void main(final String[] args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus."
          + "NimbusLookAndFeel");
    } catch(final Exception e) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch(final Exception e2) {
        e2.printStackTrace();
      }
    }
    new ChartyGUI();
  }

  /**
   * Retrieves the icon with the given name.
   * 
   * @param name name of the icon
   * @return icon
   */
  static ImageIcon icon(final String name) {
    if(!ICON_MAP.containsKey(name)) {
      try {
        final InputStream in = IOUtils.getResource("icons/" + name + ".png");
        ICON_MAP.put(name, new ImageIcon(ImageIO.read(in)));
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
    return ICON_MAP.get(name);
  }

  @Override
  public void setTitle(final String title) {
    if(title == null) {
      super.setTitle(NAME);
    } else {
      super.setTitle(NAME + ": " + title);
    }
  }

  /**
   * Shows an error message to the user.
   * 
   * @param msg message
   */
  void showError(final String msg) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JOptionPane.showMessageDialog(ChartyGUI.this, msg, "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  /**
   * Shows a file chooser dialog to the user.
   * 
   * @param dir the starting directory
   * @return the chosen file or {@code null}, if nothing was chosen
   */
  File chooseFile(final File dir) {
    final JFileChooser choose = new JFileChooser(dir);
    choose.setMultiSelectionEnabled(false);
    choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if(choose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      return choose.getSelectedFile();
    }
    return null;
  }

  /**
   * Displays the given parse tree.
   * 
   * @param tree parse tree
   * @param pos current position
   * @param num current number of parse trees
   */
  void showParseTree(final Displayer tree, final int pos, final int num) {
    treeViewer.showParseTree(tree, pos, num);
  }

  /** Rewinds the caret position in the grammar editor. */
  void rewindGrammar() {
    editor.rewind();
  }

}
