package de.woerteler.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import de.woerteler.charty.ChartParser;
import de.woerteler.charty.Grammar;
import de.woerteler.charty.GrammarSyntaxException;
import de.woerteler.charty.ParseTree;
import de.woerteler.charty.ParserException;
import de.woerteler.charty.ParserInfoListener;
import de.woerteler.charty.Tokenizer;
import de.woerteler.util.IOUtils;

/**
 * The controller class.
 * 
 * @author Leo Woerteler
 */
public final class Controller implements ParserInfoListener {

	/** The data model. */
	private final DataModel model;

	/** The GUI. */
	private final ChartyGUI gui;

	/** Lock for the parse method. */
	private final Object parseLock = new Object();

	/**
	 * Constructor taking the application's {@link DataModel model}.
	 * 
	 * @param mod
	 *            data model
	 * @param g
	 *            gui
	 */
	Controller(final ChartyGUI g, final DataModel mod) {
		gui = g;
		model = mod;
	}

	/** Saves the currently open grammar definition. */
	void saveFile() {
		final File f = model.getOpenedFile();
		if (f == null) {
			gui.showError("There's no open file to save!");
		}
	}

	/** Opens a new grammar definition. */
	void openFile() {
		File dir = null;
		final File curr = model.getOpenedFile();
		if (curr != null) {
			dir = curr.getParentFile();
		} else {
			final File home = new File(System.getProperty("user.home"));
			if (home.exists()) {
				dir = home;
			}
		}
		final File f = gui.chooseFile(dir);
		if (f == null) {
			return;
		}

		byte[] contents;
		try {
			contents = IOUtils.readFile(f);
		} catch (final IOException e) {
			gui.showError("Can't open file: " + e.getMessage());
			return;
		}

		model.setOpenedFile(f, new String(contents, Charset.forName("UTF-8")));
	}

	/**
	 * Navigates within the parse trees.
	 * 
	 * @param next
	 *            direction flag
	 */
	void navigate(final boolean next) {
		final int pos = model.getParseTreePos();
		final ParseTree[] trees = model.getParseTrees();

		if (!next && pos == 0 || next && pos >= trees.length) {
			return;
		}

		int npos;
		if (next) {
			npos = pos + 1;
		} else {
			npos = pos - 1;
		}

		try {
			final BufferedImage img = trees[npos].getImage();
			model.newParseTreePos(npos, img);
		} catch (final IOException e) {
			gui.showError("Couldn't open parse tree:\n" + e.getMessage());
		}
	}

	/**
	 * Parses the given phrase.
	 * 
	 * @param text
	 *            phrase
	 */
	void parse(final String text) {
		if (text.trim().isEmpty()) {
			gui.showError("Parser error:\nEmpty phrase");
			return;
		}

		final Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (parseLock) {
					model.clearInfo();
					final String g = model.getGrammar();
					ParseTree[] trees = new ParseTree[0];
					try {
						trees = ChartParser.parse(new Grammar(new StringReader(
								g)), Tokenizer.tokenize(text), Controller.this);
					} catch (final ParserException e) {
						gui.showError("Parser error:\n" + e.getMessage());
					} catch (final GrammarSyntaxException e) {
						gui.showError("Grammar error:\n" + e.getMessage());
					}
					model.setParseTrees(trees);
					if (trees.length == 0) {
						model.newParseTreePos(0, null);
						return;
					}

					try {
						final BufferedImage img = trees[0].getImage();
						model.newParseTreePos(0, img);
					} catch (final IOException e) {
						model.newParseTreePos(0, null);
						gui.showError("Couldn't open parse tree:\n"
								+ e.getMessage());
					}
				}
			}
		};
		t.start();
	}

	@Override
	public void info(final String category, final String message) {
		model.addInfo(category, message);
	}
}
