package com.fizzed.crux.util;

import java.util.Objects;

public class MutableDouble implements Comparable<MutableDouble> {

    private double value;

    public MutableDouble() {
        this.value = 0;
    }

    public MutableDouble(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    public void value(double value) {
        this.value = value;
    }

    public double increment() {
        this.value++;
        return this.value;
    }

    public double decrement() {
        this.value--;
        return this.value;
    }

    public double add(double value) {
        this.value += value;
        return this.value;
    }

    public double subtract(double value) {
        this.value -= value;
        return this.value;
    }

    public String toString() {
        return Double.toString(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(MutableDouble o) {
        if (o == null) {
            return 1;
        }
        return Double.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // compare to other mutable integers
        if (o instanceof MutableDouble) {
            MutableDouble that = (MutableDouble) o;
            return value == that.value;
        }
        if (o instanceof Number) {
            Number that = (Number) o;
            return value == that.doubleValue();
        }
        return false;
    }

}