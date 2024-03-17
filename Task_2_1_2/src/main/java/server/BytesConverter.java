package server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BytesConverter {

    protected static final int LONG_BYTES = 8;
    protected static final int INT_BYTES = 4;

    protected static long[] toLongArray(byte[] bytes, int count) {
        var bb = ByteBuffer.wrap(bytes);
        long[] res = new long[count];
        for (int i = 0; i < count; i++) {
            res[i] = bb.getLong();
        }
        return res;
    }

    protected static int readInt(InputStream in) throws IOException {
        long cur = System.currentTimeMillis();

        while (in.available() < 4) {
            if (System.currentTimeMillis() - cur > 300) {
                throw new IOException("Can't wait data");
            }
        }

        byte[] bytes = new byte[4];
        in.read(bytes, 0, 4);
        return ByteBuffer.wrap(bytes).getInt();
    }

    protected static long readLong(InputStream in) throws IOException {
        long cur = System.currentTimeMillis();

        while (in.available() < 8) {
            if (System.currentTimeMillis() - cur > 300) {
                throw new IOException("Can't wait data");
            }
        }

        byte[] bytes = new byte[8];
        in.read(bytes, 0, 8);
        return ByteBuffer.wrap(bytes).getLong();

    }
}
