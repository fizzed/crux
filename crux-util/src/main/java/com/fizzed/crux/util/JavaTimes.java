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
package com.fizzed.crux.util;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class JavaTimes {
 
    static public ZonedDateTime zonedDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
 
    /**
     * Returns now() in UTC.
     * @return Now in UTC
     */
    static public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }
    
    /**
     * Tests equality (ignoring timezones).
     * @param a
     * @param b
     * @return 
     */
    static public boolean equals(ZonedDateTime a, ZonedDateTime b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.toInstant().equals(b.toInstant());
    }

    /**
     * Tests for similarity down to millisecond precision. Uses a truncating
     * strategy for the "nanos" part of the value by default, unless rounding is
     * true in which case the nanos may cause the millis to be also checked if
     * they would round up.
     * @param a 
     * @param b
     * @param rounding If the nanos should be cause the milliseconds to be rounded up
     * @return True if same moment in time otherwise false
     */
    static public boolean sameMillisPrecision(Instant a, Instant b, boolean rounding) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        
        final Instant expectedTruncated = b.truncatedTo(ChronoUnit.MILLIS);
        final Instant actualTruncated = a.truncatedTo(ChronoUnit.MILLIS);

        if (expectedTruncated.equals(actualTruncated)) {
            return true;
        }

        if (rounding) {
            int expectedNanoOfSecond = b.get(ChronoField.NANO_OF_SECOND);
            int actualNanoOfSecond = a.get(ChronoField.NANO_OF_SECOND);

            int expectedNanoRemains = expectedNanoOfSecond - ((expectedNanoOfSecond/1000000)*1000000);
            int actualNanoRemains = actualNanoOfSecond - ((actualNanoOfSecond/1000000)*1000000);

            final Instant expectedPlus1 = expectedTruncated.plusMillis(1);
            final Instant actualPlus1 = actualTruncated.plusMillis(1);

            // can expected be rounded?
            if (expectedNanoRemains >= 500000) {
                if (expectedPlus1.equals(actualTruncated)) {
                    return true;
                }
                if (actualNanoRemains >= 500000 && expectedPlus1.equals(actualPlus1)) {
                    return true;
                }
            }

            if (actualNanoRemains >= 500000) {
                if (actualPlus1.equals(expectedTruncated)) {
                    return true;
                }
                if (expectedNanoRemains >= 500000 && actualPlus1.equals(expectedPlus1)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Calculates the age of a datetime from 'now'. Just like your birthday,
     * if the datetime is in the past then you a positive duration will be returned,
     * if in the future then negative. For example, if the datetime is April 1
     * and 'now' is April 2, then the age will be a positive 1 day.
     * @param instant The datetime to calculate the age of.
     * @return 
     */
    static public TimeDuration age(Instant instant) {
        return age(instant, System.currentTimeMillis());
    }
    
    /**
     * Calculates the age of a datetime from 'now'. Just like your birthday,
     * if the datetime is in the past then you a positive duration will be returned,
     * if in the future then negative. For example, if the datetime is April 1
     * and 'now' is April 2, then the age will be a positive 1 day.
     * @param dt The datetime to calculate the age of.
     * @return 
     */
    static public TimeDuration age(ZonedDateTime dt) {
        return age(dt, System.currentTimeMillis());
    }
    
    /**
     * Calculates the age of a datetime against a reference epoch millis. If
     * the datetime is on April 1 and the epochMillis is on April 2, then the
     * age will be a positive 1 day.
     * @param instant
     * @param epochMillis
     * @return 
     */
    static public TimeDuration age(Instant instant, long epochMillis) {
        if (instant == null) {
            return null;
        }
        long ageMillis = epochMillis - instant.toEpochMilli();
        return TimeDuration.millis(ageMillis);
    }
    
    /**
     * Calculates the age of a datetime against a reference epoch millis. If
     * the datetime is on April 1 and the epochMillis is on April 2, then the
     * age will be a positive 1 day.
     * @param dt
     * @param epochMillis
     * @return 
     */
    static public TimeDuration age(ZonedDateTime dt, long epochMillis) {
        if (dt == null) {
            return null;
        }
        return age(dt.toInstant(), epochMillis);
    }
    
    /**
     * Formats a string of "uptime" relative to NOW such as "6m 1s 467ms"
     * @param start
     * @return 
     */
    static public String uptime(Instant start) {
        if (start == null) {
            return null;
        }
        return uptime(System.currentTimeMillis(), start.toEpochMilli());
    }
    
    static public String uptime(Long startEpochMillis) {
        if (startEpochMillis == null) {
            return null;
        }
        return uptime(System.currentTimeMillis(), startEpochMillis);
    }
    
    static public String uptime(ZonedDateTime start) {
        if (start == null) {
            return null;
        }
        return uptime(start.toInstant());
    }
    
    static public String uptime(ZonedDateTime end, ZonedDateTime start) {
        if (end == null || start == null) {
            return null;
        }
        return uptime(end.toInstant(), start.toInstant());
    }
    
    static public String uptime(Instant end, Instant start) {
        if (end == null || start == null) {
            return null;
        }
        return uptime(end.toEpochMilli(), start.toEpochMilli());
    }
    
    static public String uptime(Long endEpochMillis, Long startEpochMillis) {
        if (endEpochMillis == null || startEpochMillis == null) {
            return null;
        }
        return uptime(Duration.ofMillis(endEpochMillis - startEpochMillis));
    }
    
    static public String uptime(Duration duration) {
        if (duration == null) {
            return null;
        }
        
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long mins = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;
        long ms = TimeUnit.MILLISECONDS.convert(duration.getNano(), TimeUnit.NANOSECONDS);
        
        final StringBuilder sb = new StringBuilder();
        
        if (days != 0) {
            sb.append(days).append("d");
        }
        
        if (hours != 0) {
            if (sb.length() > 0) { sb.append(" "); }
            sb.append(hours).append("h");
        }
        
        if (mins != 0) {
            if (sb.length() > 0) { sb.append(" "); }
            sb.append(mins).append("m");
        }
        
        if (secs != 0) {
            if (sb.length() > 0) { sb.append(" "); }
            sb.append(secs).append("s");
        }
        
        if (ms != 0 || sb.length() == 0) {
            if (sb.length() > 0) { sb.append(" "); }
            sb.append(ms).append("ms");
        }
        
        return sb.toString();
    }
    
}