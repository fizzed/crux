package com.fizzed.crux.util;

import java.util.Objects;

public class ByteSize {

    final private long value;
    final private ByteSizeUnit displayUnit;

    public ByteSize(long value) {
        this(value, ByteSizeUnit.B);
    }

    public ByteSize(long value, ByteSizeUnit displayUnit) {
        Objects.requireNonNull(displayUnit, "displayUnit was null");
        this.value = value;
        this.displayUnit = displayUnit;
    }

    public ByteSizeUnit getDisplayUnit() {
        return displayUnit;
    }

    public long getValue() {
        return value;
    }

    public ByteSize withDisplayUnit(ByteSizeUnit displayUnit) {
        return new ByteSize(this.value, displayUnit);
    }

    public double convertValue(ByteSizeUnit unit) {
        return unit.fromByteSize(this.value);
    }

    @Override
    public String toString() {
        if (this.displayUnit == ByteSizeUnit.B) {
            return Long.toString(this.value);
        } else {
            double converted = this.displayUnit.fromByteSize(this.value);
            String s = Double.toString(converted);
            if (s.endsWith(".0")) {
                s = s.substring(0, s.length()-2);
            }
            return s + this.displayUnit.getShortId();
        }
    }

    static public ByteSize parse(String s) {
        return parse(s, ByteSizeUnit.B);
    }

    static public ByteSize parse(String s, ByteSizeUnit defaultUnit) {
        // parse for first digit, then first non-digit
        int digitStartPos = -1;
        int unitStartPos = -1;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                if (digitStartPos < 0) {
                    digitStartPos = i;
                } else {
                    // do nothing
                }
            } else {
                if (unitStartPos < 0) {
                    unitStartPos = i;       // done, we can exit early
                    break;
                } else {
                    // do nothing
                }
            }
        }

        if (digitStartPos < 0) {
            throw new IllegalArgumentException("Value '" + s + "' does not contain any digits");
        }

        String digitString = s.substring(digitStartPos, unitStartPos < 0 ? s.length() : unitStartPos).trim();

        long size = Long.parseLong(digitString);
        ByteSizeUnit displayUnit = defaultUnit;

        if (unitStartPos > 0) {
            String unitString = s.substring(unitStartPos).trim();
            displayUnit = ByteSizeUnit.resolve(unitString);

            if (displayUnit == null) {
                throw new IllegalArgumentException("The part '" + unitString + "' in value '" + s + "' is not a valid byte size unit");
            }
        }

        long value = displayUnit.toByteSize(size);

        return new ByteSize(value, displayUnit);
    }

}