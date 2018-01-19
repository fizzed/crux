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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    
}