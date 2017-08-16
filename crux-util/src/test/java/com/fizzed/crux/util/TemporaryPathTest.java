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
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import org.junit.Test;

public class TemporaryPathTest {

    @Test
    public void temporaryFile() throws IOException {
        final TemporaryPath tempFile = TemporaryPath.tempFile();
        
        assertThat(Files.exists(tempFile.getPath()), is(true));
        assertThat(Files.isRegularFile(tempFile.getPath()), is(true));
        
        try {
            Set<PosixFilePermission> permissions
                = Files.getPosixFilePermissions(tempFile.getPath());
            
            assertThat(permissions, contains(PosixFilePermission.OWNER_WRITE));
            assertThat(permissions, not(contains(PosixFilePermission.GROUP_READ)));
            assertThat(permissions, not(contains(PosixFilePermission.OTHERS_READ)));
        } catch (Throwable t) {
            // ignore this on non-posix systems
        }
        
        tempFile.close();
        
        assertThat(Files.exists(tempFile.getPath()), is(false));
    }
    
    @Test
    public void temporaryFileWithPrefix() throws IOException {
        try (TemporaryPath tempFile = TemporaryPath.tempFile("test.")) {
            assertThat(tempFile.getPath().getFileName().toString(), startsWith("test."));
        }
    }
    
    @Test
    public void temporaryDirectory() throws IOException {
        TemporaryPath tempDir = TemporaryPath.tempDirectory();
        
        assertThat(Files.exists(tempDir.getPath()), is(true));
        assertThat(Files.isDirectory(tempDir.getPath()), is(true));
        
        try {
            Set<PosixFilePermission> permissions
                = Files.getPosixFilePermissions(tempDir.getPath());
            
            assertThat(permissions, contains(PosixFilePermission.OWNER_WRITE));
            assertThat(permissions, not(contains(PosixFilePermission.GROUP_READ)));
            assertThat(permissions, not(contains(PosixFilePermission.OTHERS_READ)));
        } catch (Throwable t) {
            // ignore this on non-posix systems
        }
        
        tempDir.close();
        
        assertThat(Files.exists(tempDir.getPath()), is(false));
    }
    
    @Test
    public void temporaryDirectoryWithPrefix() throws IOException {
        try (TemporaryPath tempFile = TemporaryPath.tempDirectory("test.")) {
            assertThat(tempFile.getPath().getFileName().toString(), startsWith("test."));
        }
    }
    
    @Test
    public void temporaryDirectoryWithFiles() throws IOException {
        TemporaryPath tempDir = TemporaryPath.tempDirectory();
        
        assertThat(Files.exists(tempDir.getPath()), is(true));
        assertThat(Files.isDirectory(tempDir.getPath()), is(true));
        
        // add some files then close it
        Path testfile1 = tempDir.getPath().resolve("1.txt");
        Files.write(testfile1, Arrays.asList("test"));
        Path testdir1 = tempDir.getPath().resolve("a");
        Files.createDirectories(testdir1);
        
        tempDir.close();
        
        assertThat(Files.exists(tempDir.getPath()), is(false));
    }
    
}