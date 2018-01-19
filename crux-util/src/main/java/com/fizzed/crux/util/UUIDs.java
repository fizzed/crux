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

import java.util.Objects;
import java.util.UUID;

/**
 * Utilities for working with UUIDs.
 * 
 * @author jjlauer
 */
public class UUIDs {
    
    static public UUID fromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length != 16) {
            throw new IllegalArgumentException("must be 16 bytes in length");
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
        if (uuid == null) {
            return null;
        }
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] bytes = new byte[16];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte)(msb & 0xFF);
            msb >>= 8;
        }
        for (int i = 15; i >= 8; i--) {
            bytes[i] = (byte)(lsb & 0xFF);
            lsb >>= 8;
        }
        return bytes;
    }
 
    /**
     * Creates a byte array of a time-based UUID that is suitable for sorting
     * and lexigraphical ordering.  Matches the algorithm in MySQL 8.0's 
     * uuid_to_bin() method.  https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-to-bin
     * For example, 6ccd780c-babb-1026-9564-5b8c656024db will return a byte
     * array as 1026babb-6ccd-780c-9564-5b8c656024db.  The embedded timestamp
     * is moved to the front.
     * @param uuid The time-based UUID
     * @return A byte array representation
     */
    static public byte[] toTimeBytes(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (uuid.version() != 1) {
            throw new IllegalArgumentException("not a time-based uuid");
        }
        
        // move the 6 & 7 octet of msb to front
        // move the 4 & 5 octet of msg to next
        // then move 0-3 to the back
        //  in: 6ccd-780c-babb-1026-9564-5b8c-6560-24db
        // out: 1026-babb-6ccd-780c-9564-5b8c-6560-24db
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] bytes = new byte[16];
        bytes[1] = (byte)(msb & 0xFF); msb >>= 8;
        bytes[0] = (byte)(msb & 0xFF); msb >>= 8;
        bytes[3] = (byte)(msb & 0xFF); msb >>= 8;
        bytes[2] = (byte)(msb & 0xFF); msb >>= 8;
        for (int i = 7; i >= 4; i--) {
            bytes[i] = (byte)(msb & 0xFF);
            msb >>= 8;
        }
        for (int i = 15; i >= 8; i--) {
            bytes[i] = (byte)(lsb & 0xFF);
            lsb >>= 8;
        }
        return bytes;
    }
    
    static public UUID fromTimeBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length != 16) {
            throw new IllegalArgumentException("must be 16 bytes in length");
        }

        // most significant nibble MUST be 1
        int version = (bytes[0] >> 4) & 0x0F;
        if (version != 1) {
            throw new IllegalArgumentException("not a time-based byte array");
        }
        
        //  in: 1026-babb-6ccd-780c 9564-5b8c-6560-24db
        // out: 6ccd-780c-babb-1026 9564-5b8c-6560-24db
        long msb = 0;
        long lsb = 0;
        for (int i = 4; i < 8; i++) {
            msb = (msb << 8) | (bytes[i] & 0xff);
        }
        msb = (msb << 8) | (bytes[2] & 0xff);
        msb = (msb << 8) | (bytes[3] & 0xff);
        msb = (msb << 8) | (bytes[0] & 0xff);
        msb = (msb << 8) | (bytes[1] & 0xff);
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }
    
    // millis at 00:00:00.000 15 Oct 1582
    private static final long START_EPOCH = -12219292800000L;
    
    /**
     * Extracts the timestamp embedded within a Type 1 time-based UUID and
     * converts it to "unix time" as the number of milliseconds that have elapsed
     * since 00:00:00 Coordinated Universal Time (UTC), Thursday, 1 January 1970.
     * The same kind of value that System.currentTimeMillis() returns.
     * @param uuid The time-based uuid
     * @return The number of epoch milliseconds
     */
    static public long epochMillis(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid was null");
        if (uuid.version() != 1) {
            throw new IllegalArgumentException("not a time-based uuid");
        }
        return (uuid.timestamp() / 10000L) + START_EPOCH;
    }
    
    static public void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        
        System.out.println("before: " + uuid);
        
        byte[] bytes = toTimeBytes(uuid);
        
        UUID uuid2 = fromBytes(bytes);
        
        System.out.println("after: " + uuid2);
    }
    
}