package de.woerteler.gui;

import static de.woerteler.gui.GUIActions.ActionID.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jkit.io.convert.Converter;
import jkit.io.ini.IniReader;
import de.woerteler.charty.Displayer;
import de.woerteler.util.IOUtils;

/**
 * The JFrame containing the GUI application.
 * 
 * @author Leo Woerteler
 * @author Joschi <josua.krause@googlemail.com>
 */
public final class ChartyGUI extends JFrame {

  /**
   * The ini file that holds all settings for the charty application.
   */
  public static final IniReader INI = IniReader.createFailProofIniReader(
      new File("./charty.ini"), true);

  /** The name of this application. */
  private static final String NAME = "ChartyGUI 0.1 α 1";

  /** The info text for saving editor changes when closing the window. */
  private static final String SAVE_INFO = "<html>The grammar has unsaved changes." +
      "<br>Would you like to save it now?";

  /** Serial version UID. */
  private static final long serialVersionUID = 6440369013993516988L;

  /** Preferred width of the window. */
  private static final int WINDOW_WIDTH = INI.getInteger("window", "width", 1024);
  /** Preferred height of the window. */
  private static final int WINDOW_HEIGHT = INI.getInteger("window", "height", 768);

  /** Icon sizes provided. */
  private static final int[] ICON_SIZES = { 16, 32, 64, 128, 256};

  /** Cache for icons. */
  private static final HashMap<String, ImageIcon> ICON_MAP;

  static {
    ICON_MAP = new HashMap<String, ImageIcon>();
  }

  /** Parse tree viewer. */
  private final ParseTreeViewer treeViewer;

  /** Grammar editor. */
  private final GrammarEditor editor;

  /** The save action. It is kept in the gui for saving when closing the window. */
  private final Action saveAction;

  /**
   * Constructor.
   */
  private ChartyGUI() {
    setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

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
    final JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    final JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    fileMenu.add(ctrl.getActionFor(GRAMMAR_OPEN));
    saveAction = ctrl.getActionFor(GRAMMAR_SAVE);
    fileMenu.add(saveAction);
    fileMenu.addSeparator();
    fileMenu.add(ctrl.getActionFor(EXIT));
    final JMenu displayMenu = new JMenu("Display");
    menuBar.add(displayMenu);
    final ButtonGroup displayGroup = new ButtonGroup();
    final JRadioButtonMenuItem dd = new JRadioButtonMenuItem(
        ctrl.getActionFor(DISPLAY_DEFAULT));
    dd.setSelected(true);
    displayGroup.add(dd);
    displayMenu.add(dd);
    final JRadioButtonMenuItem db = new JRadioButtonMenuItem(
        ctrl.getActionFor(DISPLAY_BOX));
    displayGroup.add(db);
    displayMenu.add(db);
    final JRadioButtonMenuItem dl = new JRadioButtonMenuItem(
        ctrl.getActionFor(DISPLAY_LATEX));
    displayGroup.add(dl);
    displayMenu.add(dl);
    displayMenu.addSeparator();
    displayMenu.add(ctrl.getActionFor(VIEW_SAVE));
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
      final File grammar = INI.getObject("last", "grammar", Converter.FILE_CONVERTER, "");
      final InputStream psg = (grammar == null || !grammar.exists())
          ? IOUtils.getResource("PSG1.txt")
              : new BufferedInputStream(new FileInputStream(grammar));
          final byte[] contents = IOUtils.readFully(psg);
          model.setOpenedFile(grammar.exists() ? grammar : null,
              new String(contents, Charset.forName("UTF-8")));
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
    input.focus();

    // to the center of the screen
    setLocationRelativeTo(null);
    // shows unresolved / cycling threads -- safer close
    // ensures call of dispose
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  @Override
  public void dispose() {
    if(grammarHasChanged) {
      final int result = JOptionPane.showConfirmDialog(this, SAVE_INFO,
          "Unsaved changes...", JOptionPane.YES_NO_CANCEL_OPTION);
      if(result == JOptionPane.CANCEL_OPTION) {
        // abort dispose
        setVisible(true);
        return;
      }
      if(result == JOptionPane.YES_OPTION) {
        // since it is our own action we are safe to pass no action event
        saveAction.actionPerformed(null);
      }
    }
    writeIniOnChange();
    super.dispose();
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
    new ChartyGUI().setVisible(true);
  }

  /**
   * Retrieves the icon with the given name.
   * 
   * @param name name of the icon
   * @return icon
   */
  public static ImageIcon icon(final String name) {
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

  /**
   * Whether the grammar has been changed in the editor since the last save.
   */
  private boolean grammarHasChanged;

  /**
   * Sets the title of the window.
   * 
   * @param title The title after the program name.
   * @param changed Whether the editor has changes.
   */
  public void setTitle(final String title, final boolean changed) {
    grammarHasChanged = changed;
    super.setTitle(NAME + (title == null ? "" : ": " + title) + (changed ? "*" : ""));
  }

  @Override
  public void setTitle(final String title) {
    setTitle(title, false);
  }

  /**
   * Shows an error message to the user.
   * 
   * @param msg message
   */
  public void showError(final String msg) {
    final Component cgui = this;
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JOptionPane.showMessageDialog(cgui, msg, "Error",
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
  public File chooseFile(final File dir) {
    final JFileChooser choose = new JFileChooser(dir);
    choose.setMultiSelectionEnabled(false);
    choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
    return choose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION
        ? choose.getSelectedFile() : null;
  }

  /**
   * Displays the given parse tree.
   * 
   * @param tree parse tree
   * @param pos current position
   * @param num current number of parse trees
   */
  public void showParseTree(final Displayer tree, final int pos, final int num) {
    treeViewer.showParseTree(tree, pos, num);
  }

  /** Rewinds the caret position in the grammar editor. */
  public void rewindGrammar() {
    editor.rewind();
  }

  /**
   * Saves the current view of the syntax tree.
   * 
   * @param file The destination.
   */
  public void saveView(final File file) {
    final Dimension dim = treeViewer.getTreeViewSize();
    final BufferedImage img = new BufferedImage(dim.width, dim.height,
        BufferedImage.TYPE_INT_ARGB);
    final Graphics2D gfx = (Graphics2D) img.getGraphics();
    treeViewer.paintSyntaxTree(gfx);
    gfx.dispose();
    final boolean isPng = file.getName().endsWith(".png");
    try {
      ImageIO.write(img, isPng ? "PNG" : "JPG", file);
    } catch(final IOException e) {
      showError(e.getMessage());
    }
  }

  /**
   * Saves the ini file if the content has been changed since loading the file.
   */
  public void writeIniOnChange() {
    if(INI.hasChanged()) {
      try {
        INI.writeIni();
      } catch(final Exception e) {
        showError("Error writing the ini file!");
      }
    }
  }

}
