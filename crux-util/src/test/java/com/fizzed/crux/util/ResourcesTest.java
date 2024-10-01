package com.fizzed.crux.util;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

public class ResourcesTest {

    @Test
    public void fileNotInJar() throws Exception {
        Path f1 = Resources.file("/fixtures/hello.txt");

        assertThat(f1, is(not(nullValue())));
        assertThat(Files.exists(f1), is(true));
        assertThat(f1.getFileName().toString(), is("hello.txt"));
    }

    @Test
    public void fileInJar() throws Exception {
        // this is a file IN the joda-time.jar
        Resources.file("/org/joda/time/tz/data/ZoneInfoMap", (isLongLived, f1) -> {
            assertThat(f1, is(not(nullValue())));
            assertThat(Files.exists(f1), is(true));
            assertThat(f1.getFileName().toString(), is("ZoneInfoMap"));
        });
    }

    @Test
    public void fileInJarButNotAllowedAsLongLived() throws Exception {
        try {
            Resources.file("/org/joda/time/tz/data/ZoneInfoMap");
            fail();
        } catch (IOException e) {
            assertThat(e.getMessage(), containsString("Resource /org/joda/time/tz/data/ZoneInfoMap was found on classpath but its not available as a Path"));
        }
    }

    @Test
    public void fileNotFound() throws Exception {
        try {
            Resources.file("/fixtures/notfound.txt");
            fail();
        } catch (FileNotFoundException e) {
            // expected
        }
    }

}