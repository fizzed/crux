package com.fizzed.crux.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * When you are opening a path with try-with-resources that you want to be
 * deleted on close.
 */
public class TemporaryPath implements Closeable {

    private final Path path;

    public TemporaryPath(Path file) {
        Objects.requireNonNull(file, "path was null");
        this.path = file;
    }

    public Path getPath() {
        return this.path;
    }
    
    @Override
    public void close() throws IOException {
        Files.deleteIfExists(this.path);
    }
    
    /**
     * Closes (deletes) the temporary file and suppresses any IO exception that
     * may or may not occur.
     */
    public void safeClose() {
        try {
            this.close();
        } catch (IOException e) {
            // ignore it
        }
    }
    
    @Override
    public String toString() {
        return this.path.toString();
    }
    
    static public TemporaryPath tempFile() {
        return tempFile("", "");
    }
    
    static public TemporaryPath tempFile(Path baseDir) {
        return tempFile(baseDir, "", "");
    }
    
    static public TemporaryPath tempFile(String prefix) {
        return tempFile(prefix, "");
    }
    
    static public TemporaryPath tempFile(Path baseDir, String prefix) {
        return tempFile(baseDir, prefix, "");
    }
    
    static public TemporaryPath tempFile(String prefix, String suffix) {
        try {
            Path path = Files.createTempFile(prefix, suffix);
            return new TemporaryPath(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    static public TemporaryPath tempFile(Path baseDir, String prefix, String suffix) {
        try {
            // baseDir must exist for this to work
            Files.createDirectories(baseDir);
            Path path = Files.createTempFile(baseDir, prefix, suffix);
            return new TemporaryPath(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    static public TemporaryPath tempDirectory() {
        return tempDirectory("");
    }
    
    static public TemporaryPath tempDirectory(Path baseDir) {
        return tempDirectory(baseDir, "");
    }
    
    static public TemporaryPath tempDirectory(String prefix) {
        try {
            Path path = Files.createTempDirectory(prefix);
            return new TemporaryPath(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    static public TemporaryPath tempDirectory(Path baseDir, String prefix) {
        try {
            // baseDir must exist for this to work
            Files.createDirectories(baseDir);
            Path path = Files.createTempDirectory(baseDir, prefix);
            return new TemporaryPath(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
}