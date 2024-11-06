package com.fizzed.crux.util;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fizzed.crux.util.ByteInput.byteInput;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ByteInputTest {

    @Test
    public void file() throws Exception {
        Path file = Resources.file("/fixtures/hello.txt");

        ByteInput bi = byteInput(file);

        assertThat(bi.size(), is(11L));
        assertThat(bi.available(), is(11L));

        try (InputStream in = bi.open()) {
            String s = InOuts.stringUTF8(in);
            assertThat(s, is("I am a test"));
        }
    }

    @Test
    public void byteArray() throws Exception {
        byte[] bytes = "I am a test".getBytes();

        ByteInput bi = byteInput(bytes);

        assertThat(bi.size(), is(11L));
        assertThat(bi.available(), is(11L));

        try (InputStream in = bi.open()) {
            String s = InOuts.stringUTF8(in);
            assertThat(s, is("I am a test"));
        }
    }

    @Test
    public void byteBuffer() throws Exception {
        byte[] bytes = "I am a test".getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        ByteInput bi = byteInput(buffer);

        assertThat(bi.size(), is(11L));
        assertThat(bi.available(), is(11L));

        try (InputStream in = bi.open()) {
            String s = InOuts.stringUTF8(in);
            assertThat(s, is("I am a test"));
        }
    }

    @Test
    public void inputStream() throws Exception {
        byte[] bytes = "I am a test".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        ByteInput bi = byteInput(bais);

        assertThat(bi.size(), is(-1L));
        assertThat(bi.available(), is(11L));

        try (InputStream in = bi.open()) {
            String s = InOuts.stringUTF8(in);
            assertThat(s, is("I am a test"));
        }
    }

    @Test
    public void inputStreamUncloseable() throws Exception {
        byte[] bytes = "I am a test".getBytes();
        final AtomicBoolean closed = new AtomicBoolean(false);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes) {
            @Override
            public void close() throws IOException {
                closed.set(true);
            }
        };

        ByteInput bi1 = byteInput(bais);

        try (InputStream in = bi1.open()) { }
        assertThat(closed.get(), is(true));


        closed.set(false);
        ByteInput bi2 = byteInput(bais, false);

        try (InputStream in = bi2.open()) { }
        assertThat(closed.get(), is(false));
    }

}