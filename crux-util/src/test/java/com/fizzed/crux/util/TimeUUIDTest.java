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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import org.junit.Test;

public class TimeUUIDTest {
 
    // unix: 1516317940686
    //   dt: 2018-01-18 18:25:40.686
    // uuid: e5b75fb3-fca6-11e7-9f59-3138381d5321
    //   ts: 137356107406860211
    // tshx: 1e7fca6e5b75fb3
    
    @Test
    public void toFromStrings() {
        TimeUUID timeuuid = TimeUUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        
        assertThat(timeuuid.toString(), is("e5b75fb3-fca6-11e7-9f59-3138381d5321"));
        
        TimeUUID timeuuid2 = TimeUUID.fromString("e6b75fb3-fca6-11e7-9f59-3138381d5321");
        
        assertThat(timeuuid.compareTo(timeuuid2), is(-1));
    }
    
    @Test
    public void compareTo() {
        TimeUUID timeuuid1 = TimeUUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        
        TimeUUID timeuuid1Same = TimeUUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        assertThat(timeuuid1, equalTo(timeuuid1Same));
        
        TimeUUID timeuuid2 = TimeUUID.fromString("e5b75fb3-fca6-1fe7-9f59-3138381d5321");
        assertThat(timeuuid1, lessThan(timeuuid2));
        
        TimeUUID timeuuid3 = TimeUUID.fromString("e5b75fb3-fca6-1ff7-9f59-3138381d5321");
        assertThat(timeuuid1, lessThan(timeuuid3));
        
        TimeUUID timeuuid4 = TimeUUID.fromString("ffffffff-ffff-1fff-9f59-3138381d5321");
        assertThat(timeuuid1, lessThan(timeuuid4));
        
        TimeUUID timeuuid5 = TimeUUID.fromString("ffffffff-ffff-1fff-ffff-ffffffffffff");
        assertThat(timeuuid1, lessThan(timeuuid5));
        
        TimeUUID timeuuid6 = TimeUUID.fromString("00000000-0000-1000-0000-000000000000");
        assertThat(timeuuid1, greaterThan(timeuuid6));
        
        TimeUUID timeuuid7 = TimeUUID.fromString("d5b75fb3-fca6-11e7-9f59-3138381d5321");
        assertThat(timeuuid1, greaterThan(timeuuid7));
        
        TimeUUID timeuuid8 = TimeUUID.fromString("e5b75fb3-fca6-11d7-9f59-3138381d5321");
        assertThat(timeuuid1, greaterThan(timeuuid8));
    }
    
    @Test
    public void compareToRequiredUnsignedBytes() {
        // these are good examples of sorted timeuuids that will only be valid
        // if their bytes are compared using unsigned ints rather than as byte arrays
        
        // epoch: 1524757638448, utc is Thu Apr 26 2018 15:47:18
        TimeUUID timeuuid1 = TimeUUID.fromString("199663c4-4969-11e8-84c8-af9a6849ad63");
        
        assertThat(timeuuid1.getEpochMillis(), is(1524757638448L));
        
        // epoch: 1524774650702, utc is Thu Apr 26 2018 20:30:50
        TimeUUID timeuuid2 = TimeUUID.fromString("b5ae57a8-4990-11e8-8f6c-d3d609bec473");
        
        assertThat(timeuuid2.getEpochMillis(), is(1524774650702L));
        
        assertThat(timeuuid1.compareTo(timeuuid2), is(-1));
        assertThat(timeuuid1.compareTo(timeuuid1), is(0));
        assertThat(timeuuid2.compareTo(timeuuid1), is(1));
        assertThat(timeuuid2.compareTo(timeuuid2), is(0));
    }
    
}