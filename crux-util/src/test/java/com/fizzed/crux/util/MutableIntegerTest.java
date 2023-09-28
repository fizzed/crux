package com.fizzed.crux.util;

import org.junit.Test;

import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class MutableIntegerTest {

    @Test
    public void ops() {
        MutableInteger mi = new MutableInteger();

        assertThat(mi.value(), is(0));

        mi.increment();

        assertThat(mi.value(), is(1));

        mi.decrement();

        assertThat(mi.value(), is(0));
    }

    @Test
    public void testEquals() {
        MutableInteger mi = new MutableInteger(15);

        assertThat(mi, is(15));
        assertThat(mi, is(15L));
        assertThat(mi, is((byte)15));
        assertThat(mi, is(not(16)));
        assertThat(mi, is(new MutableInteger(15)));
        assertThat(mi, is(not(new MutableInteger(16))));
    }

    @Test
    public void compareTo() {
        MutableInteger m1 = new MutableInteger(15);
        MutableInteger m2 = new MutableInteger(16);

        assertThat(m1.compareTo(m2), is(-1));
        assertThat(m1.compareTo(null), is(1));
        assertThat(m2.compareTo(m1), is(1));
        assertThat(m2.compareTo(m2), is(0));
    }

    @Test
    public void treeSet() {
        TreeSet<MutableInteger> mints = new TreeSet<>();

        mints.add(new MutableInteger(11));
        mints.add(new MutableInteger(9));
        mints.add(new MutableInteger(9));       // should be de-duped
        mints.add(new MutableInteger(10));

        assertThat(mints.stream().collect(toList()), is(asList(new MutableInteger(9), new MutableInteger(10), new MutableInteger(11))));
    }

}