package com.fizzed.crux.util;

import java.util.Comparator;
import java.util.Objects;

public class MutableInteger implements Comparable<MutableInteger> {

    private int value;

    public MutableInteger() {
        this.value = 0;
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }

    public int increment() {
        this.value++;
        return this.value;
    }

    public int decrement() {
        this.value--;
        return this.value;
    }

    public int add(int value) {
        this.value += value;
        return this.value;
    }

    public int subtract(int value) {
        this.value -= value;
        return this.value;
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(MutableInteger o) {
        if (o == null) {
            return 1;
        }
        return Integer.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // compare to other mutable integers
        if (o instanceof MutableInteger) {
            MutableInteger that = (MutableInteger) o;
            return value == that.value;
        }
        if (o instanceof Number) {
            Number that = (Number) o;
            return value == that.intValue();
        }
        return false;
    }

}