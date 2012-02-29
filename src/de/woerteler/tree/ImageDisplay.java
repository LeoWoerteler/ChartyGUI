package de.woerteler.tree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;

public class ImageDisplay implements DisplayMethod {

  public static final Color LINES = Color.BLACK;

  public static final Color BORDER = Color.BLACK;

  public static final Color FILL = Color.ORANGE;

  @Override
  public BufferedImage getImage(final Edge e) throws Exception {
    final Node n = generateNodeStructure(e, null);
    final Rectangle2D bbox = n.getBoundingBox();
    final BufferedImage img = new BufferedImage(
        (int) Math.ceil(bbox.getWidth()) + 1,
        (int) Math.ceil(bbox.getHeight()) + 1, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D gfx = (Graphics2D) img.getGraphics();
    gfx.translate(-bbox.getMinX(), -bbox.getMinY());
    System.out.println(bbox.getMinX() + " : " + bbox.getMinY());
    n.draw(gfx, LINES, BORDER, FILL);
    gfx.dispose();
    return img;
  }

  private Node generateNodeStructure(final Edge e, final Node root) {
    final Node n = Node.createNode(root);
    for(final Edge c : e.children()) {
      final Node nc = generateNodeStructure(c, n);
      n.addChild(nc);
    }
    return n;
  }

}
