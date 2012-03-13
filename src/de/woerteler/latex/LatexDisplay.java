package de.woerteler.latex;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;
import de.woerteler.charty.Displayer;

/**
 * Generates a graphical representation of the syntax tree via LaTeX and
 * GhostScript.
 * 
 * @author Joschi <josua.krause@googlemail.com>
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
  public Displayer getDisplayer(final Edge e) throws Exception {
    final BufferedImage img = LaTeX.toImage(toLaTeX(e));
    return new Displayer() {

      @Override
      public void drawTree(final Graphics2D gfx) {
        gfx.drawImage(img, 0, 0, null);
      }

      @Override
      public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(0, 0, img.getWidth(), img.getHeight());
      }

    };
  }

}
