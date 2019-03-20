/*
 * Copyright 2019 Fizzed, Inc.
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

public class Base16 {
 
    static private final char[] CHARS = "0123456789abcdef".toCharArray();
    
    static public String encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return encode(bytes, bytes.length);
    }
    
    static public String encode(byte[] bytes, int maxLength) {
        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength was < 0");
        }
        
        if (bytes == null) {
            return null;
        }

        final int length = maxLength < bytes.length ? maxLength : bytes.length;
        
        if (length <= 0) {
            return "";
        }
        
        final StringBuilder buf = new StringBuilder(2 * length);

        for (int i = 0 ; i < length ; i++) {
            buf.append(CHARS[(0x0f & (bytes[i] >> 4))]);
            buf.append(CHARS[(0x0f & bytes[i])]);
        }

        return buf.toString();
    }
    
}