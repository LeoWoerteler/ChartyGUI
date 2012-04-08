package de.woerteler.gui;

import static de.woerteler.gui.GUIActions.ActionID.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import jkit.io.convert.ArrayConverterAdapter;
import jkit.io.convert.Converter;
import jkit.io.ini.IniReader;
import de.woerteler.charty.Displayer;
import de.woerteler.gui.GUIActions.ActionID;
import de.woerteler.latex.LaTeXDisplay;
import de.woerteler.tree.render.DefaultRenderer;
import de.woerteler.tree.render.NodeRenderer;
import de.woerteler.tree.render.SimpleRenderer;
import de.woerteler.tree.strategy.BottomUpStrategy;
import de.woerteler.tree.strategy.TreeStrategy;
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

  /** Serial version UID. */
  private static final long serialVersionUID = 6440369013993516988L;

  /** Preferred width of the window. */
  private static final int WINDOW_WIDTH = INI.getInteger("window", "width", 1024);
  /** Preferred height of the window. */
  private static final int WINDOW_HEIGHT = INI.getInteger("window", "height", 768);

  /**
   * The initial window position or an empty array/<code>null</code> if it is in
   * the center of the screen.
   */
  private static Integer[] initialPosition = INI.getArray("window", "pos",
      new ArrayConverterAdapter<Integer>(new Integer[0]) {
    @Override
    public Integer convert(final String s) {
      try {
        return Integer.parseInt(s);
      } catch(final NumberFormatException e) {
        return null;
      }
    }
  });

  static {
    if(initialPosition != null && initialPosition.length != 2) {
      initialPosition = null;
    }
  }

  /**
   * The initial window extended state.
   */
  private static final int WINDOW_EXTEND_STATE =
      INI.getInteger("window", "state", NORMAL);

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

  /** The associated controller. */
  private final Controller ctrl;

  /** Whether the window was in the center of the screen at start up. */
  private final boolean wasInCenter;

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
    ctrl = new Controller(this, model);

    // menu bar
    final JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    final JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    fileMenu.add(ctrl.getActionFor(GRAMMAR_NEW));
    fileMenu.add(ctrl.getActionFor(GRAMMAR_OPEN));
    fileMenu.add(ctrl.getActionFor(GRAMMAR_SAVE));
    fileMenu.add(ctrl.getActionFor(GRAMMAR_SAVE_AS));
    fileMenu.addSeparator();
    fileMenu.add(ctrl.getActionFor(EXIT));
    final JMenu displayMenu = new JMenu("Display");
    menuBar.add(displayMenu);
    final ButtonGroup rendererGroup = new ButtonGroup();
    final ButtonGroup strategyGroup = new ButtonGroup();
    // no selection items
    addMenuItem(null, NodeRenderer.class, null, rendererGroup);
    addMenuItem(null, TreeStrategy.class, null, strategyGroup);
    // renderer items
    addMenuItem(DISPLAY_DEFAULT, DefaultRenderer.class, displayMenu, rendererGroup);
    addMenuItem(DISPLAY_BOX, SimpleRenderer.class, displayMenu, rendererGroup);
    // custom item as last
    if(Controller.CUSTOM_RENDERER != null) {
      addMenuItem(CUSTOM_RENDERER, Controller.CUSTOM_RENDERER.getClass(),
          displayMenu, rendererGroup);
    }
    displayMenu.addSeparator();
    // strategy items
    addMenuItem(DISPLAY_BOTTOM_UP, BottomUpStrategy.class, displayMenu, strategyGroup);
    // custom item as last
    if(Controller.CUSTOM_STRATEGY != null) {
      addMenuItem(CUSTOM_STRATEGY, Controller.CUSTOM_STRATEGY.getClass(),
          displayMenu, strategyGroup);
    }
    displayMenu.addSeparator();
    // the latex item and rest
    addMenuItem(DISPLAY_LATEX, LaTeXDisplay.class, displayMenu, null);
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
      if(grammar == null || !grammar.exists()) {
        model.setDefaultGrammar();
      } else {
        try {
          model.setOpenedFile(grammar);
        } catch(final IOException e) {
          // fall-back for unreadable files
          model.setDefaultGrammar();
        }
      }
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

    // make the window ready for display
    pack();
    input.focus();

    wasInCenter = initialPosition == null;
    if(wasInCenter) {
      // to the center of the screen
      setLocationRelativeTo(null);
      initialPosition = new Integer[] { getX(), getY()};
    } else {
      setLocation(initialPosition[0], initialPosition[1]);
    }

    // set extended state
    setExtendedState(WINDOW_EXTEND_STATE);

    // shows unresolved / cycling threads -- safer close
    // ensures call of dispose
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  /**
   * Adds a maybe grouped radio button menu item and registers it in the
   * controller.
   * 
   * @param id The action id or <code>null</code> if no action is needed.
   * @param lookup The lookup class associated with this menu item.
   * @param menu The menu to add the button to or <code>null</code> if the
   *          button should remain hidden.
   * @param group The group to add the button to or <code>null</code> if no
   *          group is associated with the button.
   */
  private void addMenuItem(final ActionID id, final Class<?> lookup, final JMenu menu,
      final ButtonGroup group) {
    final JRadioButtonMenuItem item = id == null ? new JRadioButtonMenuItem()
    : new JRadioButtonMenuItem(ctrl.getActionFor(id));
    if(group != null) {
      group.add(item);
    }
    if(menu != null) {
      menu.add(item);
    } else {
      item.setVisible(false);
    }
    ctrl.registerDisplayMenuItem(lookup, item);
  }

  @Override
  public void dispose() {
    if(ctrl.closeGrammar()) {
      // abort dispose
      setVisible(true);
      return;
    }
    ctrl.refreshIniValues();
    refreshIniValues();
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
      // system look and feel already used -- proceed
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
   * Sets the title of the window.
   * 
   * @param title The title after the program name.
   * @param changed Whether the editor has changes.
   */
  public void setTitle(final String title, final boolean changed) {
    setTitle(NAME + (title == null ? "" : ": " + title) + (changed ? "*" : ""));
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
   * Whether there is a view that can be saved.
   * 
   * @return Whether a view can be saved.
   */
  public boolean canSaveView() {
    if(!treeViewer.hasTree()) {
      showError("There is no view to save!");
      return false;
    }
    return true;
  }

  /**
   * Saves the current view of the syntax tree.
   * 
   * @param file The destination.
   */
  public void saveView(final File file) {
    final Rectangle box = treeViewer.getTreeViewSize();
    final BufferedImage img = new BufferedImage(box.width, box.height,
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
   * Refreshes the ini values responsible for the window.
   */
  private void refreshIniValues() {
    // window extended state
    final int state = getExtendedState();
    if(state != WINDOW_EXTEND_STATE) {
      INI.setInteger("window", "state", state);
    }
    setExtendedState(NORMAL); // get the real window bounds
    final Rectangle box = getBounds();
    setExtendedState(state); // restore the extended state
    // window dimension
    if(box.width != WINDOW_WIDTH) {
      INI.setInteger("window", "width", box.width);
    }
    if(box.height != WINDOW_HEIGHT) {
      INI.setInteger("window", "height", box.height);
    }
    // window position
    final Integer[] curPos = new Integer[] { box.x, box.y};
    boolean needPosUpdate;
    if(initialPosition == null || initialPosition.length != 2) {
      needPosUpdate = true;
    } else {
      final boolean samePosition = box.x == initialPosition[0]
          && box.y == initialPosition[1];
      if(wasInCenter && samePosition) {
        INI.set("window", "pos", "");
        initialPosition = null;
      }
      needPosUpdate = !samePosition;
    }
    if(needPosUpdate) {
      INI.setArray("window", "pos", curPos);
      initialPosition = curPos;
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

  /**
   * The String of the home directory.
   */
  public static final String HOME_STR = System.getProperty("user.home");

  /**
   * Shows a file chooser dialog to the user.
   * 
   * @param cur the current opened grammar or <code>null</code>
   * @return the chosen file or {@code null}, if nothing was chosen
   */
  public File chooseGrammarDialog(final File cur) {
    File dir = cur;
    if(dir == null || !dir.exists()) {
      dir = INI.getObject("last", "grammarDir", Converter.FILE_CONVERTER, HOME_STR);
    }
    final JFileChooser choose = new JFileChooser(dir);
    choose.setMultiSelectionEnabled(false);
    choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
    final File res = choose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION
        ? choose.getSelectedFile() : null;
        if(res != null) {
          INI.setObject("last", "grammarDir", res.getParentFile());
        }
        return res;
  }

  /**
   * Shows a grammar save dialog to the user.
   * 
   * @param cur the currently opened grammar or <code>null</code>
   * @return the chosen file or {@code null}, if nothing was chosen
   */
  public File saveGrammarDialog(final File cur) {
    File dir = cur;
    if(dir == null || !dir.exists()) {
      dir = INI.getObject("last", "grammarDir", Converter.FILE_CONVERTER, HOME_STR);
    }
    final JFileChooser saveDialog = new JFileChooser(dir);
    saveDialog.setMultiSelectionEnabled(false);
    saveDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
    final File res = (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        ? saveDialog.getSelectedFile() : null;
        if(res != null) {
          INI.setObject("last", "grammarDir", res.getParentFile());
        }
        return res;
  }

  /**
   * Shows a view save dialog to the user.
   * 
   * @return the chosen file or {@code null}, if nothing was chosen
   */
  public File saveViewDialog() {
    final JFileChooser choose = new JFileChooser(INI.getObject("last", "viewDir",
        Converter.FILE_CONVERTER, HOME_STR));
    choose.setFileFilter(new FileNameExtensionFilter("Image (*.png, *.jpg, *.jpeg)",
        "jpg", "jpeg", "png"));
    final File res = (choose.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        ? choose.getSelectedFile() : null;
        if(res != null) {
          INI.setObject("last", "viewDir", res.getParentFile());
        }
        return res;
  }

}
