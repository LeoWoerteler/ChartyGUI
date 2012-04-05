package de.woerteler.gui;

import static de.woerteler.gui.ChartyGUI.*;

import java.io.File;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.woerteler.charty.Displayer;
import de.woerteler.charty.ParseTree;

/**
 * The GUI application's data model.
 * 
 * @author Leo Woerteler
 */
public final class DataModel {

  /** Column names. */
  public static final String[] COLUMNS = { "Rule", "Action"};

  /** Table model of the info table. */
  private final AbstractTableModel infoTableModel;
  /** Data of the info table. */
  private final ArrayList<String[]> tableData = new ArrayList<String[]>();

  /** The Document for the grammar editor. */
  private Document grammar;

  /** The currently opened file. */
  private File opened;

  /** GUI. */
  private final ChartyGUI gui;

  /** Current parse trees. */
  private ParseTree[] trees = new ParseTree[0];

  /** Current position inside the parse trees. */
  private int parseTreePos;

  /**
   * Constructor.
   * 
   * @param g GUI object, used for updating the title
   */
  public DataModel(final ChartyGUI g) {
    gui = g;
    infoTableModel = new AbstractTableModel() {

      /** Serial version UID. */
      private static final long serialVersionUID = -3188010969325542260L;

      @Override
      public String getValueAt(final int row, final int column) {
        return getTableDataAt(row, column);
      }

      @Override
      public int getRowCount() {
        return getTableDataSize();
      }

      @Override
      public int getColumnCount() {
        return 2;
      }

      @Override
      public String getColumnName(final int column) {
        return COLUMNS[column];
      }
    };
  }

  /**
   * Getter.
   * 
   * @param row The row.
   * @param col The column.
   * @return The content.
   */
  public String getTableDataAt(final int row, final int col) {
    synchronized(infoTableModel) {
      return tableData.get(row)[col];
    }
  }

  /**
   * Getter.
   * 
   * @return The table size.
   */
  public int getTableDataSize() {
    synchronized(infoTableModel) {
      return tableData.size();
    }
  }

  /**
   * Sets the grammar viewer's document.
   * 
   * @param doc document
   */
  public void setDocument(final Document doc) {
    grammar = doc;
  }

  /**
   * Getter for the info table model.
   * 
   * @return table model
   */
  public TableModel getInfoTableModel() {
    return infoTableModel;
  }

  /**
   * Adds one line to the info table.
   * 
   * @param rule rule that produced the output
   * @param desc description
   */
  public void addInfo(final String rule, final String desc) {
    synchronized(infoTableModel) {
      final int len = tableData.size();
      tableData.add(new String[] { rule, desc});
      infoTableModel.fireTableRowsInserted(len, len);
    }
  }

  /**
   * Sets the currently opened file.
   * 
   * @param f file
   * @param contents contents of the file
   */
  public synchronized void setOpenedFile(final File f, final String contents) {
    opened = f;
    if(f != null) {
      gui.setTitle(f.getPath());
      INI.setObject("last", "grammar", f);
    } else {
      gui.setTitle(null);
      INI.set("last", "grammar", "");
    }
    try {
      grammar.remove(0, grammar.getLength());
      grammar.insertString(0, contents, null);
      gui.rewindGrammar();
    } catch(final BadLocationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter for the currently opened file.
   * 
   * @return opened file
   */
  public synchronized File getOpenedFile() {
    return opened;
  }

  /**
   * Sets the current parse trees.
   * 
   * @param ts trees
   */
  public synchronized void setParseTrees(final ParseTree[] ts) {
    trees = ts;
  }

  /**
   * Getter for the parse trees.
   * 
   * @return parse trees
   */
  public synchronized ParseTree[] getParseTrees() {
    return trees;
  }

  /**
   * Sets a new position in the parse trees.
   * 
   * @param pos new position
   * @param disp the displayer
   */
  public synchronized void newParseTreePos(final int pos, final Displayer disp) {
    parseTreePos = pos;
    gui.showParseTree(disp, pos + 1, trees.length);
  }

  /**
   * Getter for the current position.
   * 
   * @return position
   */
  public int getParseTreePos() {
    return parseTreePos;
  }

  /**
   * Returns the grammar definitions.
   * 
   * @return the grammar definitions
   */
  public String getGrammar() {
    try {
      return grammar.getText(0, grammar.getLength());
    } catch(final BadLocationException e) {
      return "";
    }
  }

  /** Clears the info panel. */
  public void clearInfo() {
    synchronized(infoTableModel) {
      final int size = tableData.size();
      tableData.clear();
      if(size > 0) {
        infoTableModel.fireTableRowsDeleted(0, size - 1);
      }
    }
  }

}
