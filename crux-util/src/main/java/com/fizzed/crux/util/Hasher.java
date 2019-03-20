package com.fizzed.crux.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Hasher {
 
    Hasher update(ByteBuffer buffer);
    
    Hasher update(byte[] buffer);
    
    Hasher update(byte[] buffer, int offset, int length);
    
    byte[] asBytes();
    
    String asHex();
    
    static public Hasher md5() {
        return new Md5Hasher();
    }
    
    static abstract class DigestHasher implements Hasher {

        protected final MessageDigest digest;
        protected byte[] hash;

        protected DigestHasher(String algorithm) {
            try {
                this.digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        protected void verifyNotCompleted() {
            if (this.hash != null) {
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