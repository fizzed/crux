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

    /**
     * Returns a if less than or equal to b, otherwise b.
     * @param a
     * @param b
     * @return
     */
    static public Instant min(Instant a, Instant b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isAfter(b) ? b : a;
    }

    /**
     * Returns the minimum of all instants.
     *
     * @param dts
     * @return
     */
    static public Instant min(Instant... dts) {
        if (dts == null) {
            return null;
        }
        Instant v = null;
        for (Instant dt : dts) {
            v = min(v, dt);
        }
        return v;
    }

    /**
     * Returns a if greater than or equal to b, otherwise b.
     * @param a
     * @param b
     * @return
     */
    static public Instant max(Instant a, Instant b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isBefore(b) ? b : a;
    }

    /**
     * Returns the minimum of all instants.
     *
     * @param dts
     * @return
     */
    static public Instant max(Instant... dts) {
        if (dts == null) {
            return null;
        }
        Instant v = null;
        for (Instant dt : dts) {
            v = max(v, dt);
        }
        return v;
    }

    /**
     * Is <code>a</code> value greater than <code>b</code>. If a is not null
     * and b is null then this is true.
     * @param a
     * @param b
     * @return
     */
    static public boolean gt(Instant a, Instant b) {
        if (a == null) {
            // if a is null then even if b is null it can't be greater than
            return false;
        }
        if (b == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.isAfter(b);
    }

    /**
     * Is <code>a</code> value greater than or equal to <code>b</code>. Will 
     * still be true if both values are null OR if a is non-null and b is null.
     * @param a
     * @param b
     * @return
     */
    static public boolean gte(Instant a, Instant b) {
        if (a == null) {
            // if a is null and b is null then this is true
            return b == null;
        }
        if (b == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.compareTo(b) >= 0;
    }

    /**
     * Is <code>a</code> value less than <code>b</code>. If b is not null
     * and a is null then this is true.
     * @param a
     * @param b
     * @return
     */
    static public boolean lt(Instant a, Instant b) {
        if (b == null) {
            // if a is null then even if b is null it can't be greater than
            return false;
        }
        if (a == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.isBefore(b);
    }

    /**
     * Is <code>a</code> value less than or equal to <code>b</code>. Will 
     * still be true if both values are null OR if a is non-null and b is null.
     * @param a
     * @param b
     * @return
     */
    static public boolean lte(Instant a, Instant b) {
        if (b == null) {
            // if a is null and b is null then this is true
            return a == null;
        }
        if (a == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.compareTo(b) <= 0;
    }

    /**
     * Whether instant is within start and end instants. The min of start and
     * end is detected so the ordering does not matter.  By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param dt The instant to check
     * @param start The start (or end) of the instant range
     * @param end The end (or start) of the instant range
     * @return True if within range, otherwise false.
     */
    static public boolean within(Instant dt, Instant start, Instant end) {
        return within(dt, start, end, true, false);
    }

    /**
     * Whether instant is within start and end instants.The min of start and
     * end is detected so the ordering does not matter. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The instant to check
     * @param start The start (or end) of the instant range
     * @param end The end (or start) of the instant range
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return True if within range, otherwise false.
     */
    static public boolean within(Instant dt, Instant start, Instant end, boolean endInclusive) {
        return within(dt, start, end, true, endInclusive);
    }

    /**
     * Whether instant is within start and end instants.The min of start and
     * end is detected so the ordering does not matter.By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The instant to check
     * @param start The start (or end) of the instant range
     * @param end The end (or start) of the instant range
     * @param startInclusive True if the start date is inclusive, otherwise exclusive.
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return True if within range, otherwise false.
     */
    static public boolean within(Instant dt, Instant start, Instant end, boolean startInclusive, boolean endInclusive) {
        if (dt == null) {
            return false;
        }

        // intelligentally pick start/end
        final Instant _start = min(start, end);
        final Instant _end = max(start, end);

        if (startInclusive && lt(dt, _start)) {
            return false;
        }

        if (!startInclusive && lte(dt, _start)) {
            return false;
        }

        if (endInclusive && gt(dt, _end)) {
            return false;
        }

        if (!endInclusive && gte(dt, _end)) {
            return false;
        }

        return true;
    }

    /**
     * Is there an overlap in range1 (<code>start1</code> to <code>end1</code>)  and range2 (<code>start2</code> to <code>end2</code>) 
     * null values are treated as <code>Instant.MIN</code> for start and <code>Instant.MAX</code> for end. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param start1 - start of range1
     * @param end1 - end of range1
     * @param start2 - start of range2
     * @param end2 - end of range2
     * @return boolean
     */
    static public boolean overlap(Instant start1, Instant end1, Instant start2, Instant end2) {
        return overlap(start1, end1, start2, end2, true, false);
    }

    /**
     * Is there an overlap in range1 (<code>start1</code> to <code>end1</code>)  and range2 (<code>start2</code> to <code>end2</code>) 
     * null values are treated as <code>Instant.MIN</code> for start and <code>Instant.MAX</code> for end. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param start1 - start of range1
     * @param end1 - end of range1
     * @param start2 - start of range2
     * @param end2 - end of range2
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return boolean
     */
    static public boolean overlap(Instant start1, Instant end1, Instant start2, Instant end2, boolean endInclusive) {
        return overlap(start1, end1, start2, end2, true, endInclusive);
    }

    /**
     * Is there an overlap in range1 (<code>start1</code> to <code>end1</code>)  and range2 (<code>start2</code> to <code>end2</code>) 
     * null values are treated as <code>Instant.MIN</code> for start and <code>Instant.MAX</code> for end. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param start1 - start of range1
     * @param end1 - end of range1
     * @param start2 - start of range2
     * @param end2 - end of range2
     * @param startInclusive True if the start date is inclusive, otherwise exclusive.
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return boolean
     */
    static public boolean overlap(Instant start1, Instant end1, Instant start2, Instant end2, boolean startInclusive, boolean endInclusive) {
        // if both never end, there is an overlap
        if (end1 == null && end2 == null) {
            return true;
        }

        // if both have no provided start, there is an overlap
        if (start1 == null && start2 == null) {
            return true;
        }

        Instant range1Start = start1 == null ? Instant.MIN : start1;
        Instant range1End = end1 == null ? Instant.MAX : end1;
        Instant range2Start = start2 == null ? Instant.MIN : start2;
        Instant range2End = end2 == null ? Instant.MAX : end2;

        if (lt(range2Start, range1Start)) {
            // swap ranges
            Instant temp1 = range1Start;
            Instant temp2 = range1End;
            range1Start = range2Start;
            range1End = range2End;
            range2Start = temp1;
            range2End = temp2;
        }

        boolean range1EndsBeforeRange2Starts = endInclusive ? lt(range1End, range2Start) : lte(range1End, range2Start);
        boolean range1StartsAfterRange2Ends = startInclusive ? gt(range1Start, range2End) : gte(range1Start, range2End);

        return !(range1EndsBeforeRange2Starts || range1StartsAfterRange2Ends);
    }
    
}