package com.fizzed.crux.util;

import org.junit.Test;

import java.util.TreeSet;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class MutableDoubleTest {

    @Test
    public void ops() {
        MutableDouble mi = new MutableDouble();

        assertThat(mi.value(), is(0d));

        mi.increment();

        assertThat(mi.value(), is(1d));

        mi.decrement();

        assertThat(mi.value(), is(0d));
    }

    @Test
    public void testEquals() {
        MutableDouble mi = new MutableDouble(15d);

        assertThat(mi, is(15d));
        assertThat(mi, is(15));
        assertThat(mi, is(15L));
        assertThat(mi, is((byte)15));
        assertThat(mi, is(not(16d)));
        assertThat(mi, is(new MutableDouble(15)));
        assertThat(mi, is(not(new MutableDouble(16))));
    }

    @Test
    public void compareTo() {
        MutableDouble m1 = new MutableDouble(15);
        MutableDouble m2 = new MutableDouble(16);

        assertThat(m1.compareTo(m2), is(-1));
        assertThat(m1.compareTo(null), is(1));
        assertThat(m2.compareTo(m1), is(1));
        assertThat(m2.compareTo(m2), is(0));
    }

    @Test
    public void treeSet() {
        TreeSet<MutableDouble> mints = new TreeSet<>();

        mints.add(new MutableDouble(11));
        mints.add(new MutableDouble(9));
        mints.add(new MutableDouble(9));       // should be de-duped
        mints.add(new MutableDouble(10));

        assertThat(mints.stream().collect(toList()), is(asList(new MutableDouble(9), new MutableDouble(10), new MutableDouble(11))));
    }

}