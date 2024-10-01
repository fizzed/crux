/*
 * Copyright 2018 Fizzed, Inc.
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Utilities for working with resources on classpath.
 * 
 * @author jjlauer
 */
public class Resources {
    
    @Deprecated
    static public InputStream newInputStream(String resourceName) throws IOException {
        return input(resourceName);
    }
    
    static public InputStream input(String resourceName) throws IOException {
        final InputStream input = Resources.class.getResourceAsStream(resourceName);
        
        if (input == null) {
            throw new FileNotFoundException("Resource " + resourceName + " not found on classpath");
        }
        
        return input;
    }
    
    static public byte[] bytes(String resourceName) throws IOException {
        try (InputStream input = newInputStream(resourceName)) {
            return InOuts.bytes(input);
        }
    }
    
    @Deprecated
    static public byte[] readAllBytes(String resourceName) throws IOException {
        return bytes(resourceName);
    }
 
    static public String string(String resourceName, Charset charset) throws IOException {
        final InputStream input = newInputStream(resourceName);
        return InOuts.string(input, charset);
    }
 
    static public String stringUTF8(String resourceName) throws IOException {
        return string(resourceName, StandardCharsets.UTF_8);
    }
 
    @Deprecated
    static public String readToString(String resourceName, Charset charset) throws IOException {
        final byte[] bytes = readAllBytes(resourceName);
        
        return new String(bytes, charset);
    }
 
    @Deprecated
    static public String readToStringUTF8(String resourceName) throws IOException {
        return readToString(resourceName, StandardCharsets.UTF_8);
    }

    static public Path file(String resourceName) throws IOException {
        final Maybe<Path> maybePath = Maybe.empty();

        file(resourceName, (isLongLived, path) -> {
            if (isLongLived) {
                maybePath.set(path);
            }
        });

        if (!maybePath.isPresent()) {
            throw new IOException("Resource " + resourceName + " was found on classpath but its not available as a Path (e.g. its inside a jar)");
        }

        return maybePath.get();
    }

    /**
     * Locates a file as a Path that's a resource on the classpath. Can either be on the local filesystem (e.g. if
     * running local unit tests) OR packaged inside a .jar. This method will create a FileSystem to read that jar
     * and the Path it will return is only valid for the life of the provided Consumer, then that backing FileSystem
     * will be closed.
     * @param resourceName The resource to locate
     * @param consumer The consumer of the Path. Once the consumer is finished, the Path will no longer be guaranteed
     *                 to be valid.
     * @throws IOException
     */
    static public void file(String resourceName, BiConsumer<Boolean,Path> consumer) throws IOException {
        final URL url = Resources.class.getResource(resourceName);

        if (url == null) {
            throw new FileNotFoundException("Resource " + resourceName + " not found on classpath");
        }

        final URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        // If we try to access a file that is inside a JAR,
        // It throws NoSuchFileException (linux), InvalidPathException (Windows)
        // jar:file:/home/jjlauer/.m2/repository/joda-time/joda-time/2.10/joda-time-2.10.jar!/org/joda/time/tz/data/ZoneInfoMap
        if (uri.getScheme().equals("jar")) {
            String jarPathAndInnerFile = uri.getSchemeSpecificPart();
            int sepPos = jarPathAndInnerFile.indexOf('!');
            // we'll skip the file: part too
            String jarPath = jarPathAndInnerFile.substring(5, sepPos);
            String filePath = jarPathAndInnerFile.substring(sepPos + 1);
            try (FileSystem fs = FileSystems.newFileSystem(Paths.get(jarPath), Resources.class.getClassLoader())) {
                consumer.accept(false, fs.getPath(filePath));
                return;
            }
        }

        consumer.accept(true, new File(uri).toPath());
    }
    
}