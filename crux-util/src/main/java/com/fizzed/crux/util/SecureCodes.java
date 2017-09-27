/*
 * Copyright 2016 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating and handling SecureCodes.  A SecureCode is
 * efficiently stored as bytes with an external base64, URL-safe string
 * representation.  The base64 padding chars will be removed too.
 */
public class SecureCodes {
    
    static private SecureCodes INSTANCE;

    static private final Base64.Decoder decoder = Base64.getUrlDecoder();
    static private final Base64.Encoder encoder = Base64.getUrlEncoder();
    private final SecureRandom random;
    
    public SecureCodes() {
        this(new SecureRandom());
    }
    
    public SecureCodes(SecureRandom random) {
        this.random = new SecureRandom();
    }
    
    static public SecureCodes getInstance() {
        // lazy instantiation
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SecureCodes.class) {
            INSTANCE = new SecureCodes();
        }
        return INSTANCE;
    }
    
    /**
     * Generates a new, random secure code with the specified number of bytes.
     * @param byteLength The number of bytes to randomly generate
     * @return A new, randomly generated secure code
     */
    public SecureCode code(int byteLength) {
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return new SecureCode(bytes);
    }
    
    /**
     * Generates a new, random secure code with a length of 16 bytes (128 bit)
     * @return A new, randomly generated 16 byte (128 bit) secure code
     */
    public SecureCode code16() {
        return code(16);
    }
    
    /**
     * Generates a new, random secure code with a length of 24 bytes (192 bit)
     * @return A new, randomly generated 24 byte (192 bit) secure code
     */
    public SecureCode code24() {
        return code(24);
    }
    
    /**
     * Generates a new, random secure code with a length of 32 bytes (256 bit)
     * @return A new, randomly generated 32 byte (256 bit) secure code
     */
    public SecureCode code32() {
        return code(32);
    }
    
    /**
     * Generates a new, random secure code with a length of 36 bytes (288 bit)
     * @return A new, randomly generated 36 byte (288 bit) secure code
     */
    public SecureCode code36() {
        return code(36);
    }
    
    /**
     * Generates a new, random secure code with a length of 48 bytes (384 bit)
     * @return A new, randomly generated 48 byte (384 bit) secure code
     */
    public SecureCode code48() {
        return code(48);
    }
    
    /**
     * Generates a new, random secure code with a length of 60 bytes (480 bit)
     * @return A new, randomly generated 60 byte (480 bit) secure code
     */
    public SecureCode code60() {
        return code(60);
    }
    
    /**
     * Generates a new, random secure code with a length of 64 bytes (512 bit)
     * @return A new, randomly generated 64 byte (512 bit) secure code
     */
    public SecureCode code64() {
        return code(64);
    }
    
    /**
     * Generates a new, random secure code with a length of 128 bytes (1024 bit)
     * @return A new, randomly generated 128 byte (1024 bit) secure code
     */
    public SecureCode code128() {
        return code(128);
    }
    
    /**
     * Generates a new, random secure code with a length of 256 bytes (2048 bit)
     * @return A new, randomly generated 256 byte (2048 bit) secure code
     */
    public SecureCode code256() {
        return code(256);
    }
    
    /**
     * Generates a new, random secure code with a length of 512 bytes (4096 bit)
     * @return A new, randomly generated 512 byte (4096 bit) secure code
     */
    public SecureCode code512() {
        return code(512);
    }
    
    static public String encode(byte[] bytes) {
        String encoded = encoder.encodeToString(bytes);
        
        // remove any trailing padding (up to 2 equals chars)
        if (encoded.charAt(encoded.length()-2) == '=') {
            return encoded.substring(0, encoded.length()-2);
        } else if (encoded.charAt(encoded.length()-1) == '=') {
            return encoded.substring(0, encoded.length()-1);
        } else {
            return encoded;
        }
    }
    
    /**
     * Decodes a secure code external representation.
     * @param string The external string
     * @return The byte array
     * @throws IllegalArgumentException If the value is detected to be invalid
     */
    static public byte[] decode(String string) throws IllegalArgumentException {
        return decoder.decode(string);
    }
    
}
