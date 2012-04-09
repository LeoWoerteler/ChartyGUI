package de.woerteler.tree;

import java.awt.FontMetrics;

/**
 * Defines measurements for the layout of the syntax tree.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class Measures {

  /**
   * The horizontal space between two nodes.
   */
  public static final double HORIZONTAL_SPACE = 25.0;

  /**
   * The vertical space between two nodes.
   */
  public static final double VERTICAL_SPACE = 30.0;

  /**
   * The horizontal space between rectangle border and text.
   */
  public static final double TEXT_H_SPACE = 2;

  /**
   * The vertical space between rectangle border and text.
   */
  public static final double TEXT_V_SPACE = 1;

  /**
   * The font metrics of the used font.
   */
  private final FontMetrics fm;

  /**
   * Creates a font measure.
   * 
   * @param fm The underlying font metrics.
   */
  public Measures(final FontMetrics fm) {
    this.fm = fm;
  }

  /**
   * Getter.
   * 
   * @return The left position of the label text.
   */
  public double stringLeft() {
    return TEXT_H_SPACE;
  }

  /**
   * The width of the box.
   * 
   * @param str The string that is displayed within the box.
   * @return The width of the box.
   */
  public double boxWidth(final String str) {
    return fm.stringWidth(str) + TEXT_H_SPACE * 2;
  }

  /**
   * Getter.
   * 
   * @return The height of the box.
   */
  public double boxHeight() {
    return fm.getHeight() + TEXT_V_SPACE * 2;
  }

  /**
   * Getter.
   * 
   * @return The lower position of the text.
   */
  public double stringLower() {
    return fm.getHeight() - fm.getAscent();
  }

  /**
   * Calculates the lower position of the text as seen from below. Note that
   * this value must be <em>added</em> to the lower coordinate of the box.
   * 
   * @return The negative distance from the lower coordinate of the box to the
   *         lower coordinate of the text.
   */
  public double stringFromBelow() {
    return -TEXT_V_SPACE - stringLower();
  }

  /**
   * Getter.
   * 
   * @return The horizontal space between two boxes.
   */
  public double horizontalSpace() {
    return HORIZONTAL_SPACE;
  }

  /**
   * Getter.
   * 
   * @return The vertical space between two rows.
   */
  public double verticalSpace() {
    return VERTICAL_SPACE;
  }

}
