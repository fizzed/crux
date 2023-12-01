package com.fizzed.crux.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ByteSizeTest {

    @Test
    public void parse() {
        ByteSize size;

        try {
            ByteSize.parse("4s");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        size = ByteSize.parse("100");
        assertThat(size.getValue(), is(100L));
        assertThat(size.toString(), is("100"));

        size = ByteSize.parse("100", ByteSizeUnit.KiB);
        assertThat(size.getValue(), is(102400L));

        size = ByteSize.parse("100kib");
        assertThat(size.getValue(), is(102400L));
        assertThat(size.toString(), is("100K"));

        size = ByteSize.parse("100k");
        assertThat(size.getValue(), is(102400L));

        size = ByteSize.parse("100 K");
        assertThat(size.getValue(), is(102400L));

        size = ByteSize.parse("100 KiB");
        assertThat(size.getValue(), is(102400L));

        size = ByteSize.parse("100 MiB");
        assertThat(size.getValue(), is(104857600L));
        assertThat(size.toString(), is("100M"));

        size = ByteSize.parse("4GiB ");
        assertThat(size.getValue(), is(4294967296L));

        size = ByteSize.parse("4g");
        assertThat(size.getValue(), is(4294967296L));
        assertThat(size.toString(), is("4G"));
        assertThat(size.withDisplayUnit(ByteSizeUnit.MiB).toString(), is("4096M"));

        // as decimal
        size = ByteSize.parse("3.89 GiB");
        assertThat(size.getValue(), is(4176855695L));
        // TODO: is there a fixed width we should use?
        assertThat(size.toString(), is("3.89G"));
        assertThat(size.withDisplayUnit(ByteSizeUnit.MiB).toString(), is("3983.36M"));
    }

}