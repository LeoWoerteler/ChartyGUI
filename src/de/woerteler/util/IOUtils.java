package de.woerteler.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Utility methods for input and output.
 * 
 * @author Leo Woerteler
 */
public final class IOUtils {

	/** Buffer size. */
	private static final int BUF_SIZ = 1024;

	/** Hidden default constructor. */
	private IOUtils() {
		// void
	}

	/**
	 * Reads the contents of a file from disk.
	 * 
	 * @param f
	 *            file to read
	 * @return contents
	 * @throws IOException
	 *             I/O exception
	 */
	public static byte[] readFile(final File f) throws IOException {
		final RandomAccessFile raf = new RandomAccessFile(f, "r");
		final byte[] buf = new byte[(int) raf.length()];
		try {
			raf.readFully(buf);
		} finally {
			raf.close();
		}
		return buf;
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
			if (read <= 0) {
				break;
			}
			baos.write(buff, 0, read);
		}
		in.close();
		return baos.toByteArray();
	}

	/**
	 * Creates a temporary file with the given extension.
	 * 
	 * @param tempDir
	 * 
	 * @param dir
	 *            directory
	 * @param ext
	 *            extension (without dot)
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
		if (!f.delete() || !f.mkdirs()) {
			throw new IOException("Can't create directory " + f);
		}
		return f;
	}

	/**
	 * Creates and runs a new {@link Process} with the given arguments, taking
	 * {@code cmd} as a pattern for {@link String#format(String, Object...)}
	 * with arguments {@code args}.
	 * 
	 * @param cmd
	 *            command
	 * @param args
	 *            argument array
	 * @param in
	 *            data for the standard input
	 * @param dir
	 *            working directory
	 * @return data written to STDOUT
	 * @throws IOException
	 *             I/O exception
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

		final Process proc = Runtime.getRuntime()
				.exec(sb.toString(), null, dir);

		final OutputStream stdin = proc.getOutputStream();
		if (in != null) {
			stdin.write(in);
		}
		stdin.close();

		// handle streams
		final byte[] out = readFully(proc.getInputStream());
		final byte[] err = readFully(proc.getErrorStream());

		if (proc.waitFor() != 0) {
			throw new IOException(cmd + " didn't return normally:\n"
					+ new String(out) + "\n\n" + new String(err));
		}

		return out;
	}

	/**
	 * Get the resource specified by the given relative path.
	 * 
	 * @param relPath
	 *            relative path of the resource
	 * @return the resource as a stream
	 * @throws FileNotFoundException
	 *             if the specified resource doesn't exist
	 */
	public static InputStream getResource(final String relPath)
			throws FileNotFoundException {
		final ClassLoader loader = IOUtils.class.getClassLoader();
		for (final String p : new String[] { "", "/" }) {
			final InputStream in = loader.getResourceAsStream(p + relPath);
			if (in != null) {
				return in;
			}
		}
		return new BufferedInputStream(new FileInputStream("resources/"
				+ relPath));
	}

}
