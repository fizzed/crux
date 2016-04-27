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
 * Utility class for generating and handling SecureCodes.
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
    
    public SecureCode code(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new SecureCode(bytes);
    }
    
    public SecureCode code24() {
        return code(24);
    }
    
    public SecureCode code36() {
        return code(36);
    }
    
    public SecureCode code48() {
        return code(48);
    }
    
    public SecureCode code60() {
        return code(60);
    }
    
    static public String encode(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }
    
    static public byte[] decode(String string) {
        return decoder.decode(string);
    }
    
}
