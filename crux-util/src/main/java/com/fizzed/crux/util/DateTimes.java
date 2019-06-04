/*
 * Copyright 2019 Fizzed, Inc.
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimes {
 
    /**
     * Returns now() in UTC.
     * @return Now in UTC
     */
    static public DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }
    
    /**
     * Tests equality (ignoring timezones).
     * @param a
     * @param b
     * @return 
     */
    static public boolean equals(DateTime a, DateTime b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.getMillis() == b.getMillis();
    }
    
    static public DateTime min(DateTime a, DateTime b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isAfter(b) ? b : a;
    }
    
    static public DateTime max(DateTime a, DateTime b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isBefore(b) ? b : a;
    }
    
    static public TimeDuration age(DateTime dt) {
        return age(dt, System.currentTimeMillis());
    }
    
    /**
     * Calculates the age of a datetime against a reference epoch millis. If
     * the datetime is on April 1 and the epochMillis is on April 2, then the
     * age will be a positive 1 day.
     * @param dt
     * @param epochMillis
     * @return 
     */
    static public TimeDuration age(DateTime dt, long epochMillis) {
        if (dt == null) {
            return null;
        }
        long ageMillis = epochMillis - dt.getMillis();
        return TimeDuration.millis(ageMillis);
    }
    
}