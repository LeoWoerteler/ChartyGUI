package de.woerteler.tree;

import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;
import de.woerteler.charty.DisplayMethod;

public class ImageDisplay implements DisplayMethod {

  @Override
  public BufferedImage getImage(final Edge e) throws Exception {
    // TODO Auto-generated method stub
    return null;
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
