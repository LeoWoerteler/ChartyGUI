package jkit.io.convert;

import jkit.io.ini.IniReader;

/**
 * Provides a generic enum converter. The enums to create can be set via the
 * constructor.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 * @param <T>
 *            The enum type.
 */
public class EnumConverter<T extends Enum<T>> implements Converter<T> {

  /**
   * The enum type to create.
   */
  private final Class<T> enumType;

  /**
   * Creates an enum converter for the given type.
   * 
   * @param enumType
   *            The enumeration type.
   */
  public EnumConverter(final Class<T> enumType) {
    this.enumType = enumType;
  }

  @Override
  public T convert(final String s) {
    return Enum.valueOf(enumType, s);
  }

  /**
   * Creates an array converter for an enumeration type and a single character
   * delimiter.
   * 
   * @param <T>
   *            The enumeration type.
   * @param enumType
   *            The enumeration type class.
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter character.
   * @return The array enum converter.
   */
  public static <T extends Enum<T>> ArrayConverter<T> createEnumArrayConverter(
      final Class<T> enumType, final T[] defaultValue,
      final char delimiter) {
    return createEnumArrayConverter(enumType, defaultValue, "" + delimiter);
  }

  /**
   * Creates an array converter for an enumeration type and the default
   * delimiter.
   * 
   * @param <T>
   *            The enumeration type.
   * @param enumType
   *            The enumeration type class.
   * @param defaultValue
   *            The default result array.
   * @return The array enum converter.
   * 
   * @see IniReader#DEFAULT_DELIMITER
   */
  public static <T extends Enum<T>> ArrayConverter<T> createEnumArrayConverter(
      final Class<T> enumType, final T[] defaultValue) {
    return createEnumArrayConverter(enumType, defaultValue,
        IniReader.DEFAULT_DELIMITER);
  }

  /**
   * Creates an array converter for an enumeration type.
   * 
   * @param <T>
   *            The enumeration type.
   * @param enumType
   *            The enumeration type class.
   * @param defaultValue
   *            The default result array.
   * @param delimiter
   *            The delimiter.
   * @return The array enum converter.
   */
  public static <T extends Enum<T>> ArrayConverter<T> createEnumArrayConverter(
      final Class<T> enumType, final T[] defaultValue,
      final String delimiter) {
    return new SimpleArrayConverter<T>(new EnumConverter<T>(enumType),
        defaultValue, delimiter);
  }

}
