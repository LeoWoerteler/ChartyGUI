package jkit.io.convert;

import java.io.File;

/**
 * Converts a String into an arbitrary object.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 * @param <T>
 *            The type of the object.
 */
public interface Converter<T> {

  /**
   * Converts the String into the object. Note that the objects
   * {@link Object#toString() toString()} method must return the exact same
   * String.
   * 
   * @param s The String.
   * @return The converted object or <code>null</code> if the String could not
   *         be converted.
   */
  T convert(String s);

  /**
   * A converter for filenames.
   */
  Converter<File> FILE_CONVERTER = new Converter<File>() {

    @Override
    public File convert(final String s) {
      return new File(s);
    }

  };

}
