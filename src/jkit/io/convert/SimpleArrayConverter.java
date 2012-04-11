package jkit.io.convert;

import jkit.io.ini.IniReader;

/**
 * A simple array converter.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 * @param <T>
 *            The component type of the arrays.
 */
public class SimpleArrayConverter<T> extends ArrayConverterAdapter<T> {

  /** The component converter. */
  private final Converter<T> converter;

  /**
   * Creates a simple array converter with a single character as delimiter.
   * 
   * @param converter
   *            The converter for the array elements.
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter character.
   */
  public SimpleArrayConverter(final Converter<T> converter,
      final T[] defaultValue, final char delimiter) {
    this(converter, defaultValue, "" + delimiter);
  }

  /**
   * Creates a simple array converter with the default delimiter.
   * 
   * @param converter
   *            The converter for the array elements.
   * @param defaultValue
   *            The default result array.
   * 
   * @see IniReader#DEFAULT_DELIMITER
   */
  public SimpleArrayConverter(final Converter<T> converter,
      final T[] defaultValue) {
    this(converter, defaultValue, IniReader.DEFAULT_DELIMITER);
  }

  /**
   * Creates a simple array converter.
   * 
   * @param converter
   *            The converter for the array elements.
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter.
   */
  public SimpleArrayConverter(final Converter<T> converter,
      final T[] defaultValue, final String delimiter) {
    super(defaultValue, delimiter);
    this.converter = converter;
  }

  @Override
  public T convert(final String s) {
    return converter.convert(s);
  }

}
