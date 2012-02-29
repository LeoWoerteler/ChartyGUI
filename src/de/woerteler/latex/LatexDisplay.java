package de.woerteler.latex;

import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;

/**
 * Generates a graphical representation of the syntax tree via LaTeX and
 * GhostScript.
 * 
 * @author Joschi
 * 
 */
public class LatexDisplay implements DisplayMethod {

  /**
   * Creates a LaTeX document for this parse tree, with help from the Qtree
   * package.
   * 
   * @param edge The root edge.
   * @return LaTeX document
   */
  private String toLaTeX(final Edge edge) {
    return "\\documentclass{article}\n\\usepackage{qtree}\n"
        + "\\usepackage[utf8]{inputenc}\n\n"
        + "\\usepackage[landscape]{geometry}\n" + "\\usepackage{fullpage}\n"
        + "\\pagestyle{empty}\n\n\\begin{document}\n\t\\Tree " + edge.toLaTeX()
        + "\n\\end{document}\n";
  }

  @Override
  public BufferedImage getImage(final Edge e) throws Exception {
    return LaTeX.toImage(toLaTeX(e));
  }

}
