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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.not;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class JodaTimeMatchersTest {
    
    @Test
    public void jodaSameToMillis() {
        final DateTimeZone usEasternTZ = DateTimeZone.forID("America/New_York");
        
        
        final DateTime i1 = DateTime.parse("2021-07-23T01:02:03.000Z");
        final DateTime i2 = DateTime.parse("2021-07-23T01:02:03.123Z");
        final DateTime i3 = DateTime.parse("2021-07-23T01:02:03.123456Z");
        final DateTime i6 = DateTime.parse("2021-07-23T01:02:03.124Z").withZone(DateTimeZone.UTC);
        final DateTime i7 = DateTime.parse("2021-07-23T01:02:03.124Z").withZone(usEasternTZ);
        
        assertThat(null, JodaTimeMatchers.jodaSameToMillis(null));
        assertThat(i1, JodaTimeMatchers.jodaSameToMillis(i1));
        assertThat(i1, not(JodaTimeMatchers.jodaSameToMillis(i2)));
        assertThat(i2, not(JodaTimeMatchers.jodaSameToMillis(i1)));
        
        assertThat(i2, JodaTimeMatchers.jodaSameToMillis(i3));
        assertThat(i3, JodaTimeMatchers.jodaSameToMillis(i2));

        assertThat(i6, not(JodaTimeMatchers.jodaSameToMillis(i3)));
        assertThat(i3, not(JodaTimeMatchers.jodaSameToMillis(i6)));
        
        // millis actually match, but timezone throw off the equality
        assertThat(i6.getMillis(), is(i7.getMillis()));
        assertThat(i6, is(not(i7)));
        assertThat(i7, is(not(i6)));
        
        assertThat(i6, JodaTimeMatchers.jodaSameToMillis(i7));
        assertThat(i7, JodaTimeMatchers.jodaSameToMillis(i6));
    }
    
}
