package com.fizzed.crux.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

abstract public class Hasher {
 
    /**
     * Updates the hash/digest with the supplied byte buffer. The hasher must
     * still be OPEN otherwise this will throw an exception.
     * @param buffer
     * @return 
     */
    abstract public Hasher update(ByteBuffer buffer);
    
    /**
     * Updates the hash/digest with the supplied byte buffer. The hasher must
     * still be OPEN otherwise this will throw an exception.
     * @param buffer
     * @return 
     */
    abstract public Hasher update(byte[] buffer);
    
    /**
     * Updates the hash/digest with the supplied byte buffer. The hasher must
     * still be OPEN otherwise this will throw an exception.
     * @param buffer
     * @param offset
     * @param length
     * @return 
     */
    abstract public Hasher update(byte[] buffer, int offset, int length);
    
    abstract public boolean isCompleted();
    
    /**
     * Closes the hasher (if not yet closed) and returns the result as bytes.
     * @return 
     */
    abstract public byte[] asBytes();
    
    /**
     * Closes the hasher (if not yet closed) and returns the result as a hex-encoded
     * value
     * @return 
     */
    abstract public String asHex();
    
    /**
     * Creates a new (open) MD5 hasher that is ready to accept a stream of bytes.
     * @return 
     */
    static public Hasher md5() {
        return new Md5Hasher();
    }

    /**
     * Creates a new (closed) MD5 hasher that has computed the digest for the file.
     * @param file The file to compute MD5 digest
     * @return
     * @throws IOException 
     */
    static public Hasher md5(File file) throws IOException {
        return md5(file != null ? file.toPath() : null);
    }
    
    /**
     * Creates a new (closed) MD5 hasher that has computed the digest for the file.
     * @param file The file to compute MD5 digest
     * @return
     * @throws IOException 
     */
    static public Hasher md5(Path file) throws IOException {
        try (InputStream input = Files.newInputStream(file)) {
            return md5(input);
        }
    }
    
    /**
     * Creates a new (closed) MD5 hasher that has computed the digest for the input.
     * @param input The input to compute MD5 digest. The input will be read until
     *      its exhausted. Caller is responsible for closing the input!
     * @return
     * @throws IOException 
     */
    static public Hasher md5(InputStream input) throws IOException {
        final Hasher hasher = md5();
        consume(hasher, input);
        return hasher;
    }
    
    static private void consume(Hasher hasher, InputStream input) throws IOException {
        InOuts.consume(input, (data, read) -> {
            hasher.update(data, 0, read);
        });
    }
    
    static abstract class DigestHasher extends Hasher {

        protected final MessageDigest digest;
        protected byte[] hash;

        protected DigestHasher(String algorithm) {
            try {
                this.digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        @Override
        public boolean isCompleted() {
            return this.hash != null;
        }
        
        protected void verifyNotCompleted() {
            if (this.isCompleted()) {
                throw new IllegalStateException("Hasher is completed and cannot be updated");
            }
        }
        
        @Override
        public Hasher update(ByteBuffer buffer) {
            this.verifyNotCompleted();
            this.digest.update(buffer);
            buffer.flip();
            return this;
        }

        @Override
        public Hasher update(byte[] buffer) {
            this.verifyNotCompleted();
            this.digest.update(buffer);
            return this;
        }

        @Override
        public Hasher update(byte[] buffer, int offset, int length) {
            this.verifyNotCompleted();
            this.digest.update(buffer, offset, length);
            return this;
        }

        @Override
        public byte[] asBytes() {
            // only enter synchronized block if still null
            if (this.hash == null) {
                synchronized(this) {
                    // verify hash is till null
                    if (this.hash == null) {
                        this.hash = this.digest.digest();
                    }
                }
            }
            return this.hash;
        }

        @Override
        public String asHex() {
            return Base16.encode(this.asBytes());
            //return new BigInteger(1, this.asBytes()).toString(16);
        }

    }
    
    static class Md5Hasher extends DigestHasher {
        
        public Md5Hasher() {
            super("MD5");
        }
        
    }

}