package de.woerteler.charty;

import java.awt.image.BufferedImage;

import de.woerteler.charty.ChartParser.Edge;

public interface DisplayMethod {

  BufferedImage getImage(Edge e) throws Exception;

}
