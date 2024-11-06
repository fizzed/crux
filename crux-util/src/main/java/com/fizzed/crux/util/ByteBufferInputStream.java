package com.fizzed.crux.util;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buf;

    public ByteBufferInputStream(ByteBuffer buffer) {
        if (buffer == null) throw new NullPointerException();
        buf = buffer;
    }

    @Override
    public synchronized int read() {
        if (buf.hasRemaining()) return buf.get() & 0xff;
        return -1;
    }

    @Override
    public synchronized int read(byte b[]) {
        return read(0, b.length, b);
    }

    @Override
    public synchronized int read(byte b[], int off, int len) {
        if ((off | len | off + len | b.length - (off + len)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        return read(off, len, b);
    }

    private int read(int off, int len, byte[] b) {
        if (len == 0) return 0;

        int rem = buf.remaining();
        if (rem <= 0) return -1;

        if (rem > len) rem = len;
        buf.get(b, off, rem);
        return rem;
    }

    @Override
    public synchronized long skip(long n) {
        if (n <= 0) return 0;

        int rem = buf.remaining();
        if (n > rem) n = rem;

        buf.position((int)(buf.position() + n));
        return n;
    }

    @Override
    public synchronized int available() { return buf.remaining(); }

    @Override
    public synchronized void mark(int readAheadLimit) { buf.mark(); }

    @Override
    public synchronized void reset() { buf.reset(); }

    @Override
    public boolean markSupported() { return true; }

    @Override
    public void close() {}
}