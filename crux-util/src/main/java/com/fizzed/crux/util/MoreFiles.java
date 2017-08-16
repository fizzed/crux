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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Methods that java.nio.Files should include.
 * 
 * @author jjlauer
 */
public class MoreFiles {
    
    /**
     * Deletes a directory (and all the files in it).
     * 
     * @param dir the directory to delete
     * @throws java.io.IOException
     */
    static public void deleteDirectory(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /**
     * Deletes a directory if it exists (and all the files in it).
     * 
     * @param dir the directory to delete
     * @return True if deleted or false if it the dir did not exist
     * @throws java.io.IOException
     */
    static public boolean deleteDirectoryIfExists(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return false;
        }
        deleteDirectory(dir);
        return true;
    }
    
}
