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
import static org.hamcrest.CoreMatchers.not;

public class JavaTimeMatchersTest {
    
    @Test
    public void sameToMillis() {
        final Instant i1 = Instant.parse("2021-07-23T01:02:03.000Z");
        final Instant i2 = Instant.parse("2021-07-23T01:02:03.123Z");
        final Instant i3 = Instant.parse("2021-07-23T01:02:03.123456Z");
        final Instant i4 = Instant.parse("2021-07-23T01:02:03.123556Z");
        final Instant i5 = Instant.parse("2021-07-23T01:02:03.123556999Z");
        final Instant i6 = Instant.parse("2021-07-23T01:02:03.124Z");
        
        assertThat(null, JavaTimeMatchers.sameToMillis(null));
        assertThat(i1, JavaTimeMatchers.sameToMillis(i1));
        assertThat(i1, not(JavaTimeMatchers.sameToMillis(i2)));
        assertThat(i2, not(JavaTimeMatchers.sameToMillis(i1)));
        
        assertThat(i2, JavaTimeMatchers.sameToMillis(i3));
        assertThat(i3, JavaTimeMatchers.sameToMillis(i2));
        
        assertThat(i2, JavaTimeMatchers.sameToMillis(i4));
        assertThat(i4, JavaTimeMatchers.sameToMillis(i2));
        
        assertThat(i5, JavaTimeMatchers.sameToMillis(i4));
        assertThat(i4, JavaTimeMatchers.sameToMillis(i5));
        
        assertThat(i6, JavaTimeMatchers.sameToMillis(i4));
        assertThat(i4, JavaTimeMatchers.sameToMillis(i6));
        
        assertThat(i6, JavaTimeMatchers.sameToMillis(i5));
        assertThat(i5, JavaTimeMatchers.sameToMillis(i6));
        
        assertThat(i6, not(JavaTimeMatchers.sameToMillis(i3)));
        assertThat(i3, not(JavaTimeMatchers.sameToMillis(i6)));
    }
    
}
