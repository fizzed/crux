/*
 * Copyright 2018 Fizzed, Inc.
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

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Utilities for working with UUIDs.
 * 
 * @author jjlauer
 */
public class UUIDs {
    
    static public UUID fromBytes(byte[] bytes) {
        // copied directly from private constructor in JDK
        if (bytes.length != 16) {
            throw new IllegalArgumentException("data must be 16 bytes in length");
        }
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }
    
    static public byte[] toBytes(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] result = new byte[16];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(msb & 0xFF);
            msb >>= 8;
            result[i+8] = (byte)(lsb & 0xFF);
            lsb >>= 8;
        }
        return result;
    }
 
    static public byte[] toLexographicBytes(UUID uuid) {
        // https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-to-bin
        // for example: 6ccd780c-baba-1026-9564-5b8c656024db
        // in mysql swap returns: 1026-BABA-6CCD780C-9564-5B8C656024DB
        // If swap_flag is 1, the format of the return value differs: The time-low and time-high parts
        // (the first and third groups of hexadecimal digits, respectively) are swapped. This moves the
        // more rapidly varying part to the right and can improve indexing efficiency if the result is
        // stored in an indexed column.
        
        // unix: 1516317940686
        //   dt: 2018-01-18 18:25:40.686
        // uuid: e5b75fb3-fca6-11e7-9f59-3138381d5321
        //   ts: 137356107406860211
        // tshx: 1e7fca6e5b75fb3
        
        byte[] bytes = toBytes(uuid);
        
        
        
        return null;
    }
    
    static public void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        
        System.out.println("before: " + uuid);
        
        byte[] bytes = toBytes(uuid);
        
        UUID uuid2 = fromBytes(bytes);
        
        System.out.println("after: " + uuid2);
    }
    
}