package com.fizzed.crux.util;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.fizzed.crux.util.Maybe.maybe;

public class Interval {
    private final Instant start;
    private final Instant end;
    
    public Interval(Instant start) {
        this(start, null);
    }

    public Interval(Instant start, Instant end) {
        Objects.requireNonNull(start);
        
        this.start = start;
        this.end = maybe(end)
            .orElse(Instant.now());
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
    
    public List<Interval> partition(long temporalDuration, TemporalUnit temporalUnit) {
        List<Interval> intervals = new ArrayList<>();

        Instant from = end;

        Instant to;
        do {
            to = from.minus(temporalDuration, temporalUnit);
            if (to.isBefore(start)) {
                to = start;
            }

            intervals.add(new Interval(to, from));
            from = to;
        } while(to.isAfter(start));

        return intervals;
    }

    @Override
    public String toString() {
        return "Interval{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }
}
