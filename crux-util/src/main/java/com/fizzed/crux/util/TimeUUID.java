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

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * A time-based UUID that is internally stored in a sortable/lexigraphical
 * byte ordering.  It's outside appearance (e.g. toString()) is identical as
 * java.util.UUID, but you can access its underlying byte array, and its 
 * natural ordering will be time ascending.
 * 
 * @author jjlauer
 */
public class TimeUUID implements Serializable, Comparable<TimeUUID> {
 
    private final byte[] timeBytes;

    public TimeUUID(byte[] timeBytes) {
        this.timeBytes = timeBytes;
    }
    
    public byte[] getBytes() {
        return this.timeBytes;
    }
    
    public long getEpochMillis() {
        return UUIDs.epochMillis(this.toUUID());
    }
    
    public UUID toUUID() {
        return UUIDs.fromTimeBytes(this.timeBytes);
    }
    
    static public TimeUUID fromUUID(UUID uuid) {
        return new TimeUUID(UUIDs.toTimeBytes(uuid));
    }
    
    static public TimeUUID fromString(String uuid) {
        return fromUUID(UUID.fromString(uuid));
    }
    
    @Override
    public String toString() {
        return this.toUUID().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Arrays.hashCode(this.timeBytes);
        return hash;
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
        final TimeUUID other = (TimeUUID) obj;
        return Arrays.equals(this.timeBytes, other.timeBytes);
    }
    
    /**
     * Compares this TimeUUID with the specified TimeUUID.  Since a TimeUUID
     * is already arranged in a sortable byte-order, this method simply compares
     * timeBytes left to right.
     * @param  val {@code TimeUUID} to which this {@code TimeUUID} is to be compared
     * @return  -1, 0 or 1 as this {@code TimeUUID} is less than, equal to, or
     *          greater than {@code val}
     */
    @Override
    public int compareTo(TimeUUID val) {
        int c;
        for (int i = 0; i < timeBytes.length; i++) {
            c = timeBytes[i] - val.timeBytes[i];
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }
    
}