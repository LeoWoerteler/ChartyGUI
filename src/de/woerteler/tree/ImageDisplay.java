package de.woerteler.tree;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;

public class ImageDisplay implements DisplayMethod {

  public static final Color LINES = Color.BLACK;

  public static final Color BORDER = Color.BLACK;

  public static final Color FILL = Color.ORANGE;

  public static final Color TEXT = Color.BLACK;

  @Override
  public BufferedImage getImage(final Edge e) throws Exception {
    final Node n = generateNodeStructure(e);
    final Rectangle2D bbox = n.getBoundingBox();
    final BufferedImage img = new BufferedImage(
        (int) Math.ceil(bbox.getWidth()) + 1,
        (int) Math.ceil(bbox.getHeight()) + 1, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D gfx = (Graphics2D) img.getGraphics();
    gfx.translate(-bbox.getMinX(), -bbox.getMinY());
    System.out.println(bbox.getMinX() + " : " + bbox.getMinY());
    n.draw(gfx, LINES, BORDER, FILL, TEXT);
    gfx.dispose();
    return img;
  }

  private Node generateNodeStructure(final Edge e) {
    final BufferedImage dummy = new BufferedImage(1, 1,
        BufferedImage.TYPE_INT_ARGB);
    final Graphics dummyGfx = dummy.getGraphics();
    final FontMetrics fm = dummyGfx.getFontMetrics();
    final Node n = generateNodeStructure(e, null, fm);
    dummyGfx.dispose();
    dummy.flush();
    return n;
  }

  private Node generateNodeStructure(final Edge e, final Node root,
      final FontMetrics fm) {
    final Node n = Node.createNode(root, e.lhs, fm);
    for(final Edge c : e.children()) {
      final Node nc = generateNodeStructure(c, n, fm);
      n.addChild(nc);
    }
    return n;
  }

}
