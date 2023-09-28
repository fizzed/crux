package com.fizzed.crux.util;

import org.junit.Test;

import java.util.TreeSet;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class MutableLongTest {

    @Test
    public void ops() {
        MutableLong mi = new MutableLong();

        assertThat(mi.value(), is(0L));

        mi.increment();

        assertThat(mi.value(), is(1L));

        mi.decrement();

        assertThat(mi.value(), is(0L));
    }

    @Test
    public void testEquals() {
        MutableLong mi = new MutableLong(15);

        assertThat(mi, is(15));
        assertThat(mi, is(15L));
        assertThat(mi, is((byte)15));
        assertThat(mi, is((short)15));
        assertThat(mi, is((double)15));
        assertThat(mi, is(not(16L)));
        assertThat(mi, is(new MutableLong(15L)));
        assertThat(mi, is(not(new MutableLong(16L))));
    }

    @Test
    public void compareTo() {
        MutableLong m1 = new MutableLong(15);
        MutableLong m2 = new MutableLong(16);

        assertThat(m1.compareTo(m2), is(-1));
        assertThat(m1.compareTo(null), is(1));
        assertThat(m2.compareTo(m1), is(1));
        assertThat(m2.compareTo(m2), is(0));
    }

    @Test
    public void treeSet() {
        TreeSet<MutableLong> mints = new TreeSet<>();

        mints.add(new MutableLong(11));
        mints.add(new MutableLong(9));
        mints.add(new MutableLong(9));       // should be de-duped
        mints.add(new MutableLong(10));

        assertThat(mints.stream().collect(toList()), is(asList(new MutableLong(9), new MutableLong(10), new MutableLong(11))));
    }

}