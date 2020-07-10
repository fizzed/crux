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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
            throw new FileNotFoundException("resource " + resourceName + " not found on classpath");
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
    
}