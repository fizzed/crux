/*
 * Copyright 2017 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class MoreFilesTest {

    public Path buildTestDirectory() throws IOException {
        Path dir = Paths.get("target/test-data/", UUID.randomUUID().toString());
        Files.createDirectories(dir);
        Path subdir = dir.resolve("a");
        Files.createDirectories(subdir);
        Path testfile1 = dir.resolve("1.txt");
        Files.write(testfile1, Arrays.asList("test"));
        Path testfile2 = subdir.resolve("2.txt");
        Files.write(testfile2, Arrays.asList("test"));
        return dir;
    }
    
    @Test
    public void deleteDirectory() throws IOException {
        Path dir = this.buildTestDirectory();
        
        assertThat(Files.exists(dir), is(true));
        
        MoreFiles.deleteDirectory(dir);
        
        assertThat(Files.exists(dir), is(false));
    }
    
    @Test
    public void deleteDirectoryIfExists() throws IOException {
        Path dir = this.buildTestDirectory();
        
        assertThat(Files.exists(dir), is(true));
        
        boolean deleted = MoreFiles.deleteDirectoryIfExists(dir);
        
        assertThat(Files.exists(dir), is(false));
        assertThat(deleted, is(true));
    }
    
    @Test
    public void deleteDirectoryOnAFile() throws IOException {
        Path file = Paths.get("target/test-data/test.txt");
        Files.write(file, Arrays.asList("test"));
        
        MoreFiles.deleteDirectory(file);
        
        assertThat(Files.exists(file), is(false));
    }
    
    @Test(expected=NoSuchFileException.class)
    public void deleteDirectoryThatDoesNotExist() throws IOException {
        MoreFiles.deleteDirectory(Paths.get("target/does-not-exist"));
    }
    
    @Test
    public void deleteDirectoryIfExistsThatDoesNotExist() throws IOException {
        boolean deleted = MoreFiles.deleteDirectoryIfExists(Paths.get("target/does-not-exist"));
        
        assertThat(deleted, is(false));
    }
    
}