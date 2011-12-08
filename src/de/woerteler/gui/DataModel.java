package de.woerteler.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.woerteler.charty.ParseTree;

/**
 * The GUI application's data model.
 * 
 * @author Leo Woerteler
 * 
 */
public final class DataModel {

	/** Table model of the info table. */
	private final AbstractTableModel infoTableModel;
	/** Data of the info table. */
	private final ArrayList<String[]> tableData = new ArrayList<String[]>();
	/** Column names. */
	private final String[] columns = { "Rule", "Action" };

	/** The Document for the grammar editor. */
	private Document grammar;

	/** The currently opened file. */
	private File opened;

	/** GUI. */
	private final ChartyGUI gui;

	/** Current parse trees. */
	private ParseTree[] trees = new ParseTree[0];

	/** Current position inside the parse trees. */
	private int parseTreePos = 0;

	/**
	 * Constructor.
	 * 
	 * @param g
	 *            GUI object, used for updating the title
	 */
	public DataModel(final ChartyGUI g) {
		gui = g;
		infoTableModel = new AbstractTableModel() {

			/** Serial version UID. */
			private static final long serialVersionUID = -3188010969325542260L;

			@Override
			public String getValueAt(final int row, final int column) {
				synchronized (this) {
					return tableData.get(row)[column];
				}
			}

			@Override
			public int getRowCount() {
				synchronized (this) {
					return tableData.size();
				}
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public String getColumnName(final int column) {
				return columns[column];
			}
		};
	}

	/**
	 * Sets the grammar viewer's document.
	 * 
	 * @param doc
	 *            document
	 */
	void setDocument(final Document doc) {
		grammar = doc;
	}

	/**
	 * Getter for the info table model.
	 * 
	 * @return table model
	 */
	TableModel getInfoTableModel() {
		return infoTableModel;
	}

	/**
	 * Adds one line to the info table.
	 * 
	 * @param rule
	 *            rule that produced the output
	 * @param desc
	 *            description
	 */
	void addInfo(final String rule, final String desc) {
		synchronized (infoTableModel) {
			final int len = tableData.size();
			tableData.add(new String[] { rule, desc });
			infoTableModel.fireTableRowsInserted(len, len);
		}
	}

	/**
	 * Sets the currently opened file.
	 * 
	 * @param f
	 *            file
	 * @param contents
	 *            contents of the file
	 */
	synchronized void setOpenedFile(final File f, final String contents) {
		opened = f;
		if (f != null) {
			gui.setTitle(f.getPath());
		}
		try {
			grammar.remove(0, grammar.getLength());
			grammar.insertString(0, contents, null);
			gui.rewindGrammar();
		} catch (final BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the currently opened file.
	 * 
	 * @return opened file
	 */
	synchronized File getOpenedFile() {
		return opened;
	}

	/**
	 * Sets the current parse trees.
	 * 
	 * @param ts
	 *            trees
	 */
	synchronized void setParseTrees(final ParseTree[] ts) {
		trees = ts;
	}

	/**
	 * Getter for the parse trees.
	 * 
	 * @return parse trees
	 */
	synchronized ParseTree[] getParseTrees() {
		return trees;
	}

	/**
	 * Sets a new position in the parse trees.
	 * 
	 * @param pos
	 *            new position
	 * @param img
	 *            image to show
	 */
	synchronized void newParseTreePos(final int pos, final BufferedImage img) {
		parseTreePos = pos;
		gui.showParseTree(img, pos + 1, trees.length);
	}

	/**
	 * Getter for the current position.
	 * 
	 * @return position
	 */
	int getParseTreePos() {
		return parseTreePos;
	}

	/**
	 * Returns the grammar definitions.
	 * 
	 * @return the grammar definitions
	 */
	String getGrammar() {
		try {
			return grammar.getText(0, grammar.getLength());
		} catch (final BadLocationException e) {
			return "";
		}
	}

	/** Clears the info panel. */
	void clearInfo() {
		synchronized (infoTableModel) {
			final int size = tableData.size();
			tableData.clear();
			if (size > 0) {
				infoTableModel.fireTableRowsDeleted(0, size - 1);
			}
		}
	}

}
