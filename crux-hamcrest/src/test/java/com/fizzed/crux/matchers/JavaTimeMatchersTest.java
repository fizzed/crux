/*
 * Copyright 2021 Fizzed, Inc.
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
package com.fizzed.crux.matchers;

import java.time.Instant;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import static com.fizzed.crux.matchers.JavaTimeMatchers.isSameToMillis;
import static org.hamcrest.CoreMatchers.not;

public class JavaTimeMatchersTest {
    
    @Test
    public void isSameToMillisWithInstant() {
        final Instant i1 = Instant.parse("2021-07-23T01:02:03.000Z");
        final Instant i2 = Instant.parse("2021-07-23T01:02:03.123Z");
        final Instant i3 = Instant.parse("2021-07-23T01:02:03.123456Z");
        final Instant i4 = Instant.parse("2021-07-23T01:02:03.123556Z");
        
//        assertThat(null, isSameToMillis(null));
        assertThat(i1, isSameToMillis(i1));
        assertThat(i1, not(isSameToMillis(i2)));
        assertThat(i2, not(isSameToMillis(i1)));
        
        assertThat(i2, isSameToMillis(i3));
        assertThat(i3, isSameToMillis(i2));
        
        assertThat(i2, isSameToMillis(i4));
        assertThat(i4, isSameToMillis(i2));
    }
    
}
