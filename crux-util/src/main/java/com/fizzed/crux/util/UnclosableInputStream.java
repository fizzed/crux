package com.fizzed.crux.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnclosableInputStream extends FilterInputStream {

    protected boolean closed;

    public UnclosableInputStream(InputStream in) {
        super(in);
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public int read() throws IOException {
        if (this.closed) return -1;
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (this.closed) return -1;
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) return -1;
        return super.read(b, off, len);
    }

    @Override
    public int available() throws IOException {
        if (this.closed) throw new IOException("Stream closed");
        return super.available();
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

}