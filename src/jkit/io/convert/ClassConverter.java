package jkit.io.convert;


/**
 * A converter that converts a String into a class and loads it.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 * 
 * @param <T>
 *            The type of the objects of the class.
 */
public class ClassConverter<T> implements Converter<Class<T>> {

  /** The class which subclass is loaded. */
  private final Class<T> superClass;

  /**
   * Creates a class converter for a given type. Such that all loaded classes
   * are assignable to this type.
   * 
   * @param superClass
   *            The class representing the type.
   */
  public ClassConverter(final Class<T> superClass) {
    this.superClass = superClass;
  }

  /**
   * Loading helper.
   * 
   * @param name The name of the class.
   * @return The loaded class.
   */
  @SuppressWarnings("unchecked")
  private Class<T> readClass(final String name) {
    try {
      final Class<?> h = Class.forName(name);
      return superClass.isAssignableFrom(h) ? (Class<T>) h : null;
    } catch (final ClassNotFoundException e) {
      return null;
    }
  }

  @Override
  public Class<T> convert(final String s) {
    return readClass(s);
  }

}
