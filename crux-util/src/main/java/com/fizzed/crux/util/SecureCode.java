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

import java.util.Arrays;

/**
 * Immutable, secure, random code internally stored as a byte array but externally
 * safe to use in a URI.
 */
public class SecureCode {
    
    private final byte[] bytes;

    public SecureCode(byte[] bytes) {
        this.bytes = bytes;
    }
    
    public SecureCode(String value) {
        this.bytes = SecureCodes.decode(value);
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    @Override
    public String toString() {
        return SecureCodes.encode(bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SecureCode other = (SecureCode) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }
}