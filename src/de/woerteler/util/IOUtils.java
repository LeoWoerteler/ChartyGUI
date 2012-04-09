package de.woerteler.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Utility methods for input and output.
 *
 * @author Leo Woerteler
 */
public final class IOUtils {

  /** The UTF-8 charset. */
  public static final Charset UTF8 = Charset.forName("UTF-8");

  /** Buffer size. */
  private static final int BUF_SIZ = 1024;

  /** Hidden default constructor. */
  private IOUtils() {
    // void
  }

  /**
   * Reads a file until EOF is reached. the stream is closed afterwards.
   * 
   * @param in the file
   * @return read bytes
   * @throws IOException I/O exception
   */
  public static byte[] readFully(final File in) throws IOException {
    return readFully(new FileInputStream(in));
  }

  /**
   * Reads an {@link InputStream} until EOF is reached. the stream is closed
   * afterwards.
   *
   * @param in
   *            input stream
   * @return read bytes
   * @throws IOException
   *             I/O exception
   */
  public static byte[] readFully(final InputStream in) throws IOException {
    final byte[] buff = new byte[BUF_SIZ];
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (true) {
      final int read = in.read(buff);
      // does not stop when bytes not yet available
      if(read < 0) {
        break;
      }
      baos.write(buff, 0, read);
    }
    in.close();
    return baos.toByteArray();
  }

  /**
   * Reads an input stream as UTF-8 String.
   * 
   * @param in The input stream.
   * @return The input stream interpreted as UTF-8 String.
   * @throws IOException If an exception occurs during reading.
   */
  public static String readString(final InputStream in) throws IOException {
    return new String(readFully(in), UTF8);
  }

  /**
   * Writes a string to the output stream. The output stream is flushed and
   * closed afterwards.
   * 
   * @param out The output stream.
   * @param str The string to write.
   * @throws IOException I/O Exception
   */
  public static void writeString(final OutputStream out, final String str)
      throws IOException {
    final Writer writer = new OutputStreamWriter(out, UTF8);
    writer.append(str);
    writer.flush();
    writer.close();
  }

  /**
   * Writes a string to a file.
   * 
   * @param file The file.
   * @param str The string to write.
   * @throws IOException I/O Exception
   */
  public static void writeString(final File file, final String str) throws IOException {
    writeString(new FileOutputStream(file), str);
  }

  /**
   * Creates a temporary file with the given extension.
   *
   * @param dir directory
   * @param ext extension (without dot)
   * @return temp file
   * @throws IOException
   *             I/O exception
   */
  public static File tempFile(final File dir, final String ext)
      throws IOException {
    return File.createTempFile("charty_", "." + ext, dir);
  }

  /**
   * Creates a temporary directory.
   *
   * @return temp dir
   * @throws IOException
   *             I/O exception
   */
  public static File tempDir() throws IOException {
    final File f = File.createTempFile("charty_", null);
    if (!f.delete() || !f.mkdirs())
      throw new IOException("Can't create directory " + f);
    return f;
  }

  /**
   * Creates and runs a new {@link Process} with the given arguments, taking
   * {@code cmd} as a pattern for {@link String#format(String, Object...)}
   * with arguments {@code args}.
   *
   * @param cmd command
   * @param args argument array
   * @param in data for the standard input
   * @param dir working directory
   * @return data written to STDOUT
   * @throws IOException I/O exception
   * @throws InterruptedException
   *             if the thread is interrupted while waiting for the process to
   *             terminate
   */
  public static byte[] exec(final String cmd, final String[] args,
      final byte[] in, final File dir) throws IOException,
      InterruptedException {

    final StringBuilder sb = new StringBuilder();
    if (cmd.contains(" ")) {
      sb.append('"' + cmd + '"');
    } else {
      sb.append(cmd);
    }
    for (final String arg : args) {
      sb.append(' ' + arg);
    }

    final Process proc = Runtime.getRuntime().exec(sb.toString(), null, dir);

    final OutputStream stdin = proc.getOutputStream();
    if (in != null) {
      stdin.write(in);
    }
    stdin.close();

    // handle streams
    final byte[] out = readFully(proc.getInputStream());
    final byte[] err = readFully(proc.getErrorStream());

    if (proc.waitFor() != 0) throw new IOException(cmd + " didn't return normally:\n"
        + new String(out) + "\n\n" + new String(err));

    return out;
  }

  /**
   * Get the resource specified by the given relative path.
   *
   * @param relPath relative path of the resource
   * @return the resource as a stream
   * @throws FileNotFoundException
   *             if the specified resource doesn't exist
   */
  public static InputStream getResource(final String relPath)
      throws FileNotFoundException {
    final ClassLoader loader = IOUtils.class.getClassLoader();
    for (final String p : new String[] { "", "/" }) {
      final InputStream in = loader.getResourceAsStream(p + relPath);
      if (in != null) return in;
    }
    return new BufferedInputStream(new FileInputStream("resources/"
        + relPath));
  }

}
