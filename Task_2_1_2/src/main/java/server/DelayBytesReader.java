package server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Class for reading from InputStream with limited time.
 */
public class DelayBytesReader {

    private static final int INT_BYTES = 4;
    private static final int LONG_BYTES = 8;

    /**
     * Reads Int from Input stream with limited time.
     *
     * @param in    InputStream
     * @param delay maximum time limit
     * @return read Int value
     * @throws IOException if InputStream don't contain enough bytes after time limit
     * @throws IOException        error with socket connection
     */
    public static int readInt(InputStream in, int delay) throws IOException {
        long cur = System.currentTimeMillis();

        while (in.available() < INT_BYTES) {
            if (System.currentTimeMillis() - cur > delay) {
                throw new IOException("Can't wait for data");
            }
        }

        byte[] bytes = new byte[INT_BYTES];
        in.read(bytes, 0, INT_BYTES);
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * Reads Long from Input stream with limited time.
     *
     * @param in    InputStream
     * @param delay maximum time limit
     * @return read Long value
     * @throws IOException if InputStream don't contain enough bytes after time limit
     * @throws IOException        error with socket connection
     */
    public static long readLong(InputStream in, int delay) throws IOException {
        long cur = System.currentTimeMillis();

        while (in.available() < LONG_BYTES) {
            if (System.currentTimeMillis() - cur > delay) {
                throw new IOException("Can't wait for data");
            }
        }

        byte[] bytes = new byte[LONG_BYTES];
        in.read(bytes, 0, LONG_BYTES);
        return ByteBuffer.wrap(bytes).getLong();
    }
}
