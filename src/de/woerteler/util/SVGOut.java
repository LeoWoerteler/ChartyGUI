package de.woerteler.util;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Writer;

/**
 * Provides a method to access an SVG writer.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public interface SVGOut {

  /**
   * Creates a graphics object that writes the graphics operations as SVG.
   * 
   * @param name The name of the program that created the SVG document.
   * @return The graphics object.
   */
  Graphics2D getGraphics(String name);

  /**
   * Writes the content of the previously created graphics object to the writer.
   * 
   * @param out The writer.
   * @param g The graphics object generated with {@link #getGraphics(String)}.
   * @throws IOException I/O Exception.
   */
  void write(Writer out, Graphics2D g) throws IOException;

}
