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

import java.time.Instant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import static java.util.Objects.isNull;

public class DateTimes {
 
    static public DateTime dateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        
        return new DateTime(instant.toEpochMilli(), DateTimeZone.UTC);
    }
 
    static public Instant javaInstant(DateTime dt) {
        if (dt == null) {
            return null;
        }
        
        return Instant.ofEpochMilli(dt.getMillis());
    }
    
    static public java.time.Duration javaDuration(Duration duration) {
        if (duration == null) {
            return null;
        }
        return java.time.Duration.ofMillis(duration.getMillis());
    }
    
    /**
     * Returns now() in UTC.
     * @return Now in UTC
     */
    static public DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }
    
    /**
     * Returns a new DateTime in UTC.
     * @param dt
     * @return 
     */
    static public DateTime utc(DateTime dt) {
        if (dt == null || dt.getZone() == DateTimeZone.UTC) {
            return dt;
        }
        return dt.withZone(DateTimeZone.UTC);
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
    
    /**
     * Returns a if less than or equal to b, otherwise b.
     * @param a
     * @param b
     * @return 
     */
    static public DateTime min(DateTime a, DateTime b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isAfter(b) ? b : a;
    }
    
    /**
     * Returns the minimum of all datetimes.
     * 
     * @param dts
     * @return 
     */
    static public DateTime min(DateTime... dts) {
        if (dts == null) {
            return null;
        }
        DateTime v = null;
        for (DateTime dt : dts) {
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
    static public DateTime max(DateTime a, DateTime b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        }
        return a.isBefore(b) ? b : a;
    }
    
    /**
     * Returns the minimum of all datetimes.
     * 
     * @param dts
     * @return 
     */
    static public DateTime max(DateTime... dts) {
        if (dts == null) {
            return null;
        }
        DateTime v = null;
        for (DateTime dt : dts) {
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
    static public boolean gt(DateTime a, DateTime b) {
        if (a == null) {
            // if a is null then even if b is null it can't be greater than
            return false;
        }
        if (b == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.getMillis() > b.getMillis();
    }
    
    /**
     * Is <code>a</code> value greater than or equal to <code>b</code>. Will 
     * still be true if both values are null OR if a is non-null and b is null.
     * @param a
     * @param b
     * @return 
     */
    static public boolean gte(DateTime a, DateTime b) {
        if (a == null) {
            // if a is null and b is null then this is true
            return b == null;
        }
        if (b == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.getMillis() >= b.getMillis();
    }
    
    /**
     * Is <code>a</code> value less than <code>b</code>. If b is not null
     * and a is null then this is true.
     * @param a
     * @param b
     * @return 
     */
    static public boolean lt(DateTime a, DateTime b) {
        if (b == null) {
            // if a is null then even if b is null it can't be greater than
            return false;
        }
        if (a == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.getMillis() < b.getMillis();
    }
    
    /**
     * Is <code>a</code> value less than or equal to <code>b</code>. Will 
     * still be true if both values are null OR if a is non-null and b is null.
     * @param a
     * @param b
     * @return 
     */
    static public boolean lte(DateTime a, DateTime b) {
        if (b == null) {
            // if a is null and b is null then this is true
            return a == null;
        }
        if (a == null) {
            // if a is NOT null and b is then its greater than
            return true;
        }
        return a.getMillis() <= b.getMillis();
    }
    
    /**
     * Calculates the age of a datetime from 'now'. Just like your birthday,
     * if the datetime is in the past then you a positive duration will be returned,
     * if in the future then negative. For example, if the datetime is April 1
     * and 'now' is April 2, then the age will be a positive 1 day.
     * @param dt The datetime to calculate the age of.
     * @return 
     */
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
    
    /**
     * Whether datetime is within start and end datetimes. The min of start and
     * end is detected so the ordering does not matter.  By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
     * @return True if within range, otherwise false.
     */
    static public boolean within(DateTime dt, DateTime start, DateTime end) {
        return within(dt, start, end, true, false);
    }
    
    /**
     * Whether datetime is within start and end datetimes.The min of start and
     * end is detected so the ordering does not matter. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return True if within range, otherwise false.
     */
    static public boolean within(DateTime dt, DateTime start, DateTime end, boolean endInclusive) {
        return within(dt, start, end, true, endInclusive);
    }
    
    /**
     * Whether datetime is within start and end datetimes.The min of start and
     * end is detected so the ordering does not matter.By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
     * @param startInclusive True if the start date is inclusive, otherwise exclusive.
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return True if within range, otherwise false.
     */
    static public boolean within(DateTime dt, DateTime start, DateTime end, boolean startInclusive, boolean endInclusive) {
        if (dt == null) {
            return false;
        }
        
        // intelligentally pick start/end
        final DateTime _start = min(start, end);
        final DateTime _end = max(start, end);
        
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
     * Formats a string of "uptime" relative to NOW such as "6m 1s 467ms"
     * @param dt
     * @return 
     */
    static public String uptime(DateTime dt) {
        if (dt == null) {
            return null;
        }
        return JavaTimes.uptime(dt.getMillis());
    }
    
    static public String uptime(DateTime end, DateTime start) {
        if (end == null || start == null) {
            return null;
        }
        return JavaTimes.uptime(end.getMillis(), start.getMillis());
    }
    
    static public String uptime(Duration duration) {
        if (duration == null) {
            return null;
        }
        return JavaTimes.uptime(javaDuration(duration));
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
     * Returns the minimum of all datetimes.
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
     * Returns the minimum of all datetimes.
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
     * Whether datetime is within start and end datetimes. The min of start and
     * end is detected so the ordering does not matter.  By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
     * @return True if within range, otherwise false.
     */
    static public boolean within(Instant dt, Instant start, Instant end) {
        return within(dt, start, end, true, false);
    }

    /**
     * Whether datetime is within start and end datetimes.The min of start and
     * end is detected so the ordering does not matter. By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
     * @param endInclusive True if the end date is inclusive, otherwise exclusive.
     * @return True if within range, otherwise false.
     */
    static public boolean within(Instant dt, Instant start, Instant end, boolean endInclusive) {
        return within(dt, start, end, true, endInclusive);
    }

    /**
     * Whether datetime is within start and end datetimes.The min of start and
     * end is detected so the ordering does not matter.By default, this method
     * makes the start date INCLUSIVE and the end date EXCLUSIVE/INCLUSIVE depending
     * on what you pass in.
     * @param dt The datetime to check
     * @param start The start (or end) of the datetime range
     * @param end The end (or start) of the datetime range
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