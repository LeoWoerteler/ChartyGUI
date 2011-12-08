package de.woerteler.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Utility class for AWT {@link BufferedImage Images}.
 * 
 * @author Leo Woerteler
 */
public final class ImageUtils {

	/** Hidden default constructor. */
	private ImageUtils() {
		// void
	}

	/**
	 * Trims white margins from a {@link BufferedImage}.
	 * 
	 * @param img
	 *            image to trim
	 * @return trimmed image
	 */
	public static BufferedImage trim(final BufferedImage img) {

		int xMin = -1, xMax = -1, yMin = -1, yMax = -1;

		final int w = img.getWidth(), h = img.getHeight();

		// from top
		x1: for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (!isWhite(img, x, y)) {
					xMin = x;
					break x1;
				}
			}
		}

		// from bottom
		x2: for (int x = w; x-- > 0;) {
			for (int y = 0; y < h; y++) {
				if (!isWhite(img, x, y)) {
					xMax = x;
					break x2;
				}
			}
		}

		// from left
		y1: for (int y = 0; y < h; y++) {
			for (int x = xMin; x <= xMax; x++) {
				if (!isWhite(img, x, y)) {
					yMin = y;
					break y1;
				}
			}
		}

		// from right
		y2: for (int y = h; y-- > 0;) {
			for (int x = xMin; x <= xMax; x++) {
				if (!isWhite(img, x, y)) {
					yMax = y;
					break y2;
				}
			}
		}

		return img.getSubimage(xMin, yMin, xMax - xMin + 1, yMax - yMin + 1);
	}

	/**
	 * Checks whether the pixel on the given position in the image is white.
	 * 
	 * @param img
	 *            image
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @return {@code true}, if the pixel is white, {@code false} otherwise
	 */
	private static boolean isWhite(final BufferedImage img, final int x,
			final int y) {
		final int rgb = img.getRGB(x, y), trans = Color.BLACK.getRGB();
		return (rgb & trans) == 0 || (rgb | trans) == Color.WHITE.getRGB();
	}

	/**
	 * Scale an image proportionally so that it fits into the given lengths.
	 * 
	 * @param img
	 *            image
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @return scaled image
	 */
	public static Image scaleToFit(final BufferedImage img, final int w,
			final int h) {
		final int iw = img.getWidth(), ih = img.getHeight();

		if (iw <= w && ih <= h) {
			return img;
		}

		int nw = h * iw / ih, nh = h;
		if (nw > w) {
			nw = w;
			nh = w * ih / iw;
		}
		return img.getScaledInstance(Math.max(nw, 1), Math.max(nh, 1),
				Image.SCALE_AREA_AVERAGING);
	}
}
