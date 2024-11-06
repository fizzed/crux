package com.fizzed.crux.util;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ByteBufferInputStreamTest {

    @Test
    public void read() {
        final ByteBuffer buf = ByteBuffer.wrap("abcdef".getBytes());

        final ByteBufferInputStream in = new ByteBufferInputStream(buf);
        int read;

        assertThat(in.available(), is(6));
        assertThat(in.read(), is((int)'a'));

        final byte[] bytes = new byte[2];
        read = in.read(bytes);
        assertThat(read, is(2));
        assertThat(bytes[0], is((byte)'b'));
        assertThat(bytes[1], is((byte)'c'));

        read = in.read(bytes, 1, 1);
        assertThat(read, is(1));
        assertThat(bytes[1], is((byte)'d'));

        assertThat(in.available(), is(2));
        read = in.read(bytes);
        assertThat(read, is(2));
        assertThat(bytes[0], is((byte)'e'));
        assertThat(bytes[1], is((byte)'f'));

        // this should be EOF now
        read = in.read(bytes);
        assertThat(read, is(-1));
    }

}