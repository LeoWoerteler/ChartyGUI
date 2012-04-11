package de.woerteler.latex;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import de.woerteler.util.IOUtils;
import de.woerteler.util.ImageUtils;

/**
 * Class for interaction with LaTeX and GhostScript.
 *
 * @author Leo Woerteler
 */
public final class LaTeX {

  /** PDFLaTeX program. */
  private static String pdfLaTeX;

  /** PDFLaTeX command line. */
  private static final String[] PDF_LATEX_ARGS = { "\"%s\"" };

  /** GhostScript program. */
  private static String ghostScript;

  /** GhostScript command line. */
  private static final String[] GHOST_SCRIPT_ARGS = { "-dNOPAUSE", "-dBATCH",
    "-q", "-sDEVICE=pngalpha", "-r200", "-sOutputFile=-", "-" };

  /** Temporary directory. */
  private static File tempDir;

  /** Hidden default constructor. */
  private LaTeX() {
    // void
  }

  /**
   * Typesets a LaTeX document and converts the resulting PDF to a
   * {@link BufferedImage}.
   *
   * @param latex the LaTeX document
   * @return corresponding image
   * @throws IOException I/O exception
   */
  public static BufferedImage toImage(final String latex) throws IOException {
    initTempDir();
    final byte[] tex = latex.getBytes(Charset.forName("UTF-8"));
    final byte[] png = pdfToPng(toPDF(tex));
    final BufferedImage img = ImageIO.read(new ByteArrayInputStream(png));
    return ImageUtils.trim(img);
  }

  /**
   * Typesets a LaTeX document with PDFLaTeX.
   *
   * @param tex LaTeX document
   * @return resulting PDF
   * @throws IOException I/O exception
   */
  private static byte[] toPDF(final byte[] tex) throws IOException {

    // find command
    synchronized (PDF_LATEX_ARGS) {
      if (pdfLaTeX == null) {
        final File mac = new File("/usr/texbin/pdflatex");
        if (mac.canExecute()) {
          pdfLaTeX = mac.getPath();
        } else {
          final String other = Programs.which("pdflatex");
          if (other == null) throw new IOException(
              "Can't find 'pdflatex', please check your "
                  + "PATH environment variable.");
          pdfLaTeX = other;
        }
      }
    }

    final File temp = IOUtils.tempFile(tempDir, "tex");
    final FileOutputStream fos = new FileOutputStream(temp);
    try {
      fos.write(tex);
    } finally {
      fos.close();
    }

    // create a subprocess for pdflatex and send it the TEX file
    final String[] args = PDF_LATEX_ARGS.clone();
    args[0] = String.format(args[0], temp.getName());

    try {
      IOUtils.exec(pdfLaTeX, args, null, tempDir);
    } catch (final InterruptedException e) {
      throw new IOException(e);
    }

    final File pdf = new File(temp.getPath().replaceFirst("\\.tex$", ".pdf"));

    return IOUtils.readFully(pdf);
  }

  /**
   * Converts the given PDF file to a PNG image with GhostScript.
   *
   * @param pdf
   *            PDF file
   * @return resulting PNG image
   * @throws IOException
   *             I/O exception
   */
  private static byte[] pdfToPng(final byte[] pdf) throws IOException {
    synchronized (GHOST_SCRIPT_ARGS) {
      if (ghostScript == null) {
        for (final String p : new String[] { "gs", "gswin32c" }) {
          final String prog = Programs.which(p);
          if (prog != null) {
            ghostScript = prog;
            break;
          }
        }
        if (ghostScript == null) throw new IOException(
            "Can't find GhostScript (gs/gswin32c), please check"
                + " your PATH environment variable.");
      }
    }

    byte[] png;
    try {
      png = IOUtils.exec(ghostScript, GHOST_SCRIPT_ARGS, pdf, tempDir);
    } catch (final InterruptedException e) {
      throw new IOException(e);
    }

    return png;
  }

  /**
   * Initializes the temporary directory.
   *
   * @throws IOException exception
   */
  private static void initTempDir() throws IOException {
    if (tempDir == null) {
      tempDir = IOUtils.tempDir();
      tempDir.deleteOnExit();
    }
  }

}
