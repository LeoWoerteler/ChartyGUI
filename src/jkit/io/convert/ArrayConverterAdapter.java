package jkit.io.convert;

import java.lang.reflect.Array;

import jkit.io.ini.IniReader;

/**
 * An adapter for the array converter. A subclass only has to implement the
 * {@link #convert(String)} method.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 * @param <T>
 *            The component type of the arrays.
 * 
 * @see SimpleArrayConverter
 */
public abstract class ArrayConverterAdapter<T> implements ArrayConverter<T> {

  /** The used delimiter. */
  private final String delimiter;

  /** The default value. */
  private final T[] defaultValue;

  /** The arrays component type. */
  private final Class<?> componentType;

  /**
   * Creates an array converter adapter with a single character as delimiter.
   * 
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter character.
   */
  public ArrayConverterAdapter(final T[] defaultValue, final char delimiter) {
    this(defaultValue, "" + delimiter);
  }

  /**
   * Creates an array converter adapter with the default delimiter.
   * 
   * @param defaultValue
   *            The default result array.
   * 
   * @see IniReader#DEFAULT_DELIMITER
   */
  public ArrayConverterAdapter(final T[] defaultValue) {
    this(defaultValue, IniReader.DEFAULT_DELIMITER);
  }

  /**
   * Creates an array converter adapter.
   * 
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter String.
   */
  public ArrayConverterAdapter(final T[] defaultValue, final String delimiter) {
    this.defaultValue = defaultValue;
    this.delimiter = delimiter;
    componentType = defaultValue.getClass().getComponentType();
  }

  @Override
  public T[] defaultValue() {
    return defaultValue;
  }

  @Override
  public T[] array(final int length) {
    return array0(length);
  }

  /**
   * Warning suppressing worm hole.
   * 
   * @param length
   *            The length of the array.
   * @return The created array.
   */
  // safe warning remover due to different java compiler settings
  // where @SuppressWarnings("unchecked") creates a warning
  @SuppressWarnings("all")
  private T[] array0(final int length) {
    return (T[]) Array.newInstance(componentType, length);
  }

  @Override
  public String delimiter() {
    return delimiter;
  }

}
