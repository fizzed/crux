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
    
}