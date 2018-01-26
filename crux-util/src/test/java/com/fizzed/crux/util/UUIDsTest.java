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

import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class UUIDsTest {
    
    // unix: 1516317940686
    //   dt: 2018-01-18 18:25:40.686
    // uuid: e5b75fb3-fca6-11e7-9f59-3138381d5321
    //   ts: 137356107406860211
    // tshx: 1e7fca6e5b75fb3
    
    @Test
    public void getEpochMillis() {
        UUID uuid = UUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        
        assertThat(UUIDs.getEpochMillis(uuid), is(1516317940686L));
    }
    
    @Test
    public void setEpochMillis() {
        UUID uuid = UUID.fromString("ffffffff-fca6-11e7-9f59-3138381d5321");
        
        UUID uuid2 = UUIDs.setEpochMillis(uuid, 1516317940686L);
        
        // slightly less precise than original due to conversions to/from unix time
        assertThat(uuid2.toString(), is("e5b75ee0-fca6-11e7-9f59-3138381d5321"));
    }
    
    @Test
    public void setTimestamp() {
        UUID uuid = UUID.fromString("ffffffff-fca6-11e7-9f59-3138381d5321");
        
        UUID uuid2 = UUIDs.setTimestamp(uuid, 137356107406860211L);

        assertThat(uuid2.toString(), is("e5b75fb3-fca6-11e7-9f59-3138381d5321"));
    }
    
    @Test
    public void toAndFromBytes() {
        UUID expected = UUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        byte[] bytes = UUIDs.toBytes(expected);
        UUID actual = UUIDs.fromBytes(bytes);
        assertThat(actual, is(expected));
        
        expected = UUID.randomUUID();
        bytes = UUIDs.toBytes(expected);
        actual = UUIDs.fromBytes(bytes);
        assertThat(actual, is(expected));
        
        // try a bunch of randoms to verify things work
        for (int i = 0; i < 1000; i++) {
            expected = UUID.randomUUID();
            bytes = UUIDs.toBytes(expected);
            actual = UUIDs.fromBytes(bytes);
            assertThat(actual, is(expected));
        }
    }
    
    @Test
    public void toAndFromTimeBytes() {
        UUID expected = UUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        byte[] bytes = UUIDs.toTimeBytes(expected);
        UUID actual = UUIDs.fromTimeBytes(bytes);
        assertThat(actual, is(expected));
        
        expected = UUID.fromString("6ccd780c-baba-1026-9564-5b8c656024db");
        bytes = UUIDs.toTimeBytes(expected);
        actual = UUIDs.fromTimeBytes(bytes);
        assertThat(actual, is(expected));
        
        try {
            UUIDs.toTimeBytes(UUID.randomUUID());
            fail("only time-based uuids should have worked");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            bytes = UUIDs.toTimeBytes(expected);
            bytes[0] = 0;   // set to invalid version
            UUIDs.fromTimeBytes(bytes);
            fail("only byte arrays with version 1 in first nibble should have worked");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
}
