package com.fizzed.crux.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UnclosableOutputStream extends FilterOutputStream {

    private boolean closed;

    public UnclosableOutputStream(OutputStream output) {
        super(output);
        this.closed = false;
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void write(int b) throws IOException {
        if (closed) throw new IOException("Stream closed");
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (closed) throw new IOException("Stream closed");
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (closed) throw new IOException("Stream closed");
        super.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (closed) throw new IOException("Stream closed");
        super.flush();
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

}