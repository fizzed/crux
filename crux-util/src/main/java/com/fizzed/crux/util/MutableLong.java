package com.fizzed.crux.util;

import java.util.Objects;

public class MutableLong implements Comparable<MutableLong> {

    private long value;

    public MutableLong() {
        this.value = 0;
    }

    public MutableLong(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }

    public long increment() {
        this.value++;
        return this.value;
    }

    public long decrement() {
        this.value--;
        return this.value;
    }

    public long add(long value) {
        this.value += value;
        return this.value;
    }

    public long subtract(long value) {
        this.value -= value;
        return this.value;
    }

    public String toString() {
        return Long.toString(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(MutableLong o) {
        if (o == null) {
            return 1;
        }
        return Long.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // compare to other mutable integers
        if (o instanceof MutableLong) {
            MutableLong that = (MutableLong)o;
            return value == that.value;
        }
        if (o instanceof Number) {
            Number that = (Number) o;
            return value == that.longValue();
        }
        return false;
    }

}