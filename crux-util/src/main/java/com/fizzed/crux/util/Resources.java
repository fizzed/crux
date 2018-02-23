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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Utilities for working with resources on classpath.
 * 
 * @author jjlauer
 */
public class Resources {
    
    static public InputStream newInputStream(String resourceName) throws IOException {
        final InputStream input = Resources.class.getResourceAsStream(resourceName);
        
        if (input == null) {
            throw new FileNotFoundException("resource " + resourceName + " not found on classpath");
        }
        
        return input;
    }
    
    static public byte[] readAllBytes(String resourceName) throws IOException {
        final InputStream input = newInputStream(resourceName);
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int read;
            byte[] data = new byte[2048];
            while ((read = input.read(data, 0, data.length)) != -1) {
                output.write(data, 0, read);
            }
            output.flush();
            return output.toByteArray();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
 
    static public String readToString(String resourceName, Charset charset) throws IOException {
        final byte[] bytes = readAllBytes(resourceName);
        
        return new String(bytes, charset);
    }
    
}