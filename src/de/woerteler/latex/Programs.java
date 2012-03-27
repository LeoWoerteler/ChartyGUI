package de.woerteler.latex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to find the PDFLaTeX and GhostScript programs.
 *
 * @author Leo Woerteler
 */
public final class Programs {

  /** Hidden default constructor. */
  private Programs() {
    // never used
  }

  /** The path variable's content. */
  private static final List<File> PATH = new ArrayList<File>();
  /** Extension of executable file types on the path. */
  private static final List<String> PATH_EXT = new ArrayList<String>();

  static {
    String path = System.getProperty("java.library.path", "");
    final String path2 = System.getenv("PATH");
    if (path2 != null) {
      path += File.pathSeparator + path2;
    }
    for (final String p : path.split(File.pathSeparator)) {
      final File f = new File(p);
      if (f.isDirectory()) {
        try {
          final File cf = f.getCanonicalFile();
          if (!PATH.contains(cf)) {
            PATH.add(cf);
          }
        } catch (final IOException e) {
          // should not happen
          throw new Error(e);
        }
      }
    }

    PATH_EXT.add("");
    final String os = System.getProperty("os.name", "").toLowerCase();
    if (os.indexOf("windows") > -1) {
      final String pathExt = System.getenv("PATHEXT");
      if (pathExt != null) {
        for (final String ext : pathExt.split(File.pathSeparator)) {
          PATH_EXT.add(ext);
        }
      }
    } else {
      PATH_EXT.add(".sh");
    }
  }

  /**
   * Finds an executable file on the path.
   *
   * @param prog
   *            program's name
   * @return the file's path, or {@code null}, if not found
   */
  public static String which(final String prog) {
    for (final File f : PATH) {
      for (final String ext : PATH_EXT) {
        final File p = new File(f, prog + ext);
        if (p.isFile()) return p.getAbsolutePath();
      }
    }
    return null;
  }

}
