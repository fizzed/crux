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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Config-friendly immutable way to represent a duration of time as long + unit
 * and easily convert from strings.  Great for json, config files, etc.
 * 
 * Can be represented as a string such as "10ms" or "1s" or "2d".
 * 
 * @author jjlauer
 */
public class TimeDuration implements MoreComparable<TimeDuration> {
 
    private final long duration;
    private final TimeUnit unit;

    public TimeDuration(long duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }
    
    public TimeDuration toNanos() {
        if (unit == TimeUnit.NANOSECONDS) {
            return this;
        } else {
            return new TimeDuration(this.unit.toNanos(this.duration), TimeUnit.NANOSECONDS);
        }
    }
    
    public TimeDuration toMillis() {
        if (unit == TimeUnit.MILLISECONDS) {
            return this;
        } else {
            return new TimeDuration(this.unit.toMillis(this.duration), TimeUnit.MILLISECONDS);
        }
    }
    
    public TimeDuration toSeconds() {
        if (unit == TimeUnit.SECONDS) {
            return this;
        } else {
            return new TimeDuration(this.unit.toSeconds(this.duration), TimeUnit.SECONDS);
        }
    }
    
    public TimeDuration toMinutes() {
        if (unit == TimeUnit.MINUTES) {
            return this;
        } else {
            return new TimeDuration(this.unit.toMinutes(this.duration), TimeUnit.MINUTES);
        }
    }
    
    public TimeDuration toHours() {
        if (unit == TimeUnit.HOURS) {
            return this;
        } else {
            return new TimeDuration(this.unit.toHours(this.duration), TimeUnit.HOURS);
        }
    }
    
    public TimeDuration toDays() {
        if (unit == TimeUnit.DAYS) {
            return this;
        } else {
            return new TimeDuration(this.unit.toDays(this.duration), TimeUnit.DAYS);
        }
    }
    
    public boolean isZero() {
        return this.duration == 0;
    }
    
    /**
     * Calls Thread.sleep() but first converts this duration to milliseconds,
     * then calls Thread.sleep.
     * 
     * @throws InterruptedException 
     */
    public void sleep() throws InterruptedException {
        Thread.sleep(this.asMillis());
    }
    
    public long asNanos() {
        return this.toNanos().getDuration();
    }
    
    public long asMillis() {
        return this.toMillis().getDuration();
    }
    
    public long asSeconds() {
        return this.toSeconds().getDuration();
    }
    
    public long asMinutes() {
        return this.toMinutes().getDuration();
    }
    
    public long asHours() {
        return this.toHours().getDuration();
    }
    
    public long asDays() {
        return this.toDays().getDuration();
    }
    
    @Override
    public String toString() {
        return this.duration + toShort(this.unit);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.duration ^ (this.duration >>> 32));
        hash = 83 * hash + Objects.hashCode(this.unit);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeDuration other = (TimeDuration) obj;
        return this.compareTo(other) == 0;
    }
    
    @Override
    public int compareTo(TimeDuration o) {
        // if units match, compare durations
        if (this.unit == o.unit) {
            // use long.compare to prevent overflow from effecting result
            return Long.compare(this.duration, o.duration);
        } else {
            // convert to nanoseconds, then compare
            return Long.compare(this.asNanos(), o.asNanos());
        }
    }

    static public String toShort(TimeUnit timeUnit) {
        if (timeUnit != null) {
            switch (timeUnit) {
                case DAYS:
                    return "d";
                case HOURS:
                    return "h";
                case MINUTES:
                    return "m";
                case SECONDS:
                    return "s";
                case MILLISECONDS:
                    return "ms";
                case NANOSECONDS:
                    return "ns";
            }
        }
        return null;
    }
    
    static public TimeUnit fromShort(String value) {
        if (value != null) {
            switch (value) {
                case "D":
                case "d":
                    return TimeUnit.DAYS;
                case "H":
                case "h":
                    return TimeUnit.HOURS;
                case "M":
                case "m":
                    return TimeUnit.MINUTES;
                case "S":
                case "s":
                    return TimeUnit.SECONDS;
                case "MS":
                case "ms":
                case "Ms":
                case "mS":
                    return TimeUnit.MILLISECONDS;
                case "NS":
                case "ns":
                case "Ns":
                case "nS":
                    return TimeUnit.NANOSECONDS;
            }
        }
        return null;
    }
    
    static public TimeDuration parse(String value) {
        return parse(value, null);
    }
    
    static public TimeDuration parse(String value, TimeUnit defaultUnit) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // take all non-digits on right
        int durationLength = value.length();
        for ( ; durationLength > 0; durationLength--) {
            if (Character.isDigit(value.charAt(durationLength - 1))) {
                break;
            }
        }
        
        int unitLength = value.length() - durationLength;
        
        if (unitLength <= 0 && defaultUnit == null) {
            throw new IllegalArgumentException("No unit specified on time duration '" + value + "'");
        } else if (unitLength > 2) {
            throw new IllegalArgumentException("Invalid unit specified on time duration '" + value + "'");
        } else if (durationLength <= 0) {
            throw new IllegalArgumentException("No duration specified on time duration '" + value + "'");
        }
        
        TimeUnit unit;
        if (unitLength <= 0) {
            unit = defaultUnit;
        } else {
            String _unit = value.substring(durationLength);
            unit = fromShort(_unit);
            
            if (unit == null) {
                throw new IllegalArgumentException("Unsupported unit '" + _unit + "' specified on time duration '" + value + "'");
            }
        }
        
        String _duration = value.substring(0, durationLength);
        long duration;
        try {
            duration = Long.parseLong(_duration);
        } catch (Exception e) {
            throw new IllegalArgumentException("Duration '" + _duration + "' is not a long value on time duration '" + value + "'");
        }
        
        return new TimeDuration(duration, unit);
    }
    
    static public TimeDuration nanos(long durationNanos) {
        return new TimeDuration(durationNanos, TimeUnit.NANOSECONDS);
    }
    
    static public TimeDuration millis(long durationMillis) {
        return new TimeDuration(durationMillis, TimeUnit.MILLISECONDS);
    }
    
    static public TimeDuration seconds(long durationSeconds) {
        return new TimeDuration(durationSeconds, TimeUnit.SECONDS);
    }
    
    static public TimeDuration minutes(long durationMinutes) {
        return new TimeDuration(durationMinutes, TimeUnit.MINUTES);
    }
    
    static public TimeDuration hours(long durationHours) {
        return new TimeDuration(durationHours, TimeUnit.HOURS);
    }
    
    static public TimeDuration days(long durationDays) {
        return new TimeDuration(durationDays, TimeUnit.DAYS);
    }
    
}