package de.woerteler.tree;

import java.util.ArrayList;
import java.util.List;

class Node {

  public static final double horizontalSpace = 25.0;

  public static final double verticalSpace = 30.0;

  public static final double width = 15.0;

  public static final double height = 10.0;

  public static Node createNode(final Node parent) {
    if(parent == null) {
      return new Node(width, height, 0);
    }
    return new Node(width, height, parent.y + parent.h + verticalSpace);
  }

  private final double y;

  private final double w;

  private final double h;

  private final List<Node> childs;

  private Node(final double w, final double h, final double y) {
    this.w = w;
    this.h = h;
    this.y = y;
    childs = new ArrayList<Node>();
  }

  public void addChild(final Node n) {
    childs.add(n);
  }

}
