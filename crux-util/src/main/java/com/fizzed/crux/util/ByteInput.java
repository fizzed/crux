package com.fizzed.crux.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Generic way of providing "byte inputs" as files, byte arrays, etc. to methods.
 */
public class ByteInput {

    protected final Path file;
    protected final byte[] bytes;
    protected final ByteBuffer buffer;
    protected final InputStream in;
    protected final boolean closeable;

    static public ByteInput byteInput(Path file) {
        Objects.requireNonNull(file, "file cannot be null");
        return new ByteInput(file, null, null, null, true);
    }

    static public ByteInput byteInput(File file) {
        Objects.requireNonNull(file, "file cannot be null");
        return new ByteInput(file.toPath(), null, null, null, true);
    }

    static public ByteInput byteInput(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        return new ByteInput(null, bytes, null, null, true);
    }

    static public ByteInput byteInput(ByteBuffer buffer) {
        Objects.requireNonNull(buffer, "buffer cannot be null");
        return new ByteInput(null, null, buffer, null, true);
    }

    static public ByteInput byteInput(InputStream in) {
        Objects.requireNonNull(in, "input cannot be null");
        return new ByteInput(null, null, null, in, true);
    }

    static public ByteInput byteInput(InputStream in, boolean closeable) {
        Objects.requireNonNull(in, "input cannot be null");
        return new ByteInput(null, null, null, in, closeable);
    }

    protected Long size;

    protected ByteInput(Path file, byte[] bytes, ByteBuffer buffer, InputStream in, boolean closeable) {
        this.file = file;
        this.bytes = bytes;
        this.buffer = buffer;
        this.in = in;
        this.closeable = closeable;
    }

    /**
     * If the size is definitively known ahead of time, this is the number of bytes. Will work on files, byte arrays,
     * and byte buffers, but others will return -1 since the size is not known definitively.
     * @return The number of bytes this input represents or -1 if that size is not definitively known
     * @throws IOException
     */
    public long available() throws IOException {
        if (this.file != null || this.bytes != null || this.buffer != null) {
            return this.size();
        } else if (this.in != null) {
            return this.in.available();
        }
        return -1L;
    }

    /**
     * If the size is definitively known ahead of time, this is the number of bytes. Will work on files, byte arrays,
     * and byte buffers, but others will return -1 since the size is not known definitively.
     * @return The number of bytes this input represents or -1 if that size is not definitively known
     * @throws IOException
     */
    public long size() throws IOException {
        if (size == null) {
            if (this.file != null) {
                this.size = Files.size(this.file);
            } else if (this.bytes != null) {
                this.size = (long)this.bytes.length;
            } else if (this.buffer != null) {
                this.size = (long)this.buffer.remaining();
            } else {
                // otherwise we just don't know what size this is
                this.size = -1l;
            }
        }
        return this.size;
    }

    public InputStream open() throws IOException {
        if (this.file != null) {
            return Files.newInputStream(this.file);
        } else if (this.bytes != null) {
            return new ByteArrayInputStream(this.bytes);
        } else if (this.buffer != null) {
            return new ByteBufferInputStream(this.buffer);
        } else if (this.in != null) {
            if (this.closeable) {
                return this.in;
            } else {
                // wrap input stream in an un-closeable version
                return new UnclosableInputStream(this.in);
            }
        } else {
            throw new IllegalStateException("All available inputs were null (file, bytes, input)");
        }
    }

}