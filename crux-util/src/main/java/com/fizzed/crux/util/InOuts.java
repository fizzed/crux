/*
 * Copyright 2019 Fizzed, Inc.
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InOuts {
    
    static public final int DEFAULT_BUFFER_SIZE = 2048;
    
    /**
     * Null-safe closing of an inputstream (but will still throw an IOException)
     * @param input
     * @throws IOException 
     */
    static public void close(InputStream input) throws IOException {
        if (input != null) {
            input.close();
        }
    }
    
    /**
     * Null-safe closing of an outputstream (but will still throw an IOException)
     * @param output
     * @throws IOException 
     */
    static public void close(OutputStream output) throws IOException {
        if (output != null) {
            output.close();
        }
    }
    
    /**
     * Null-safe closing of an inputstream AND any exception during the close
     * will be discarded.
     * 
     * @param input 
     */
    static public void closeQuietly(InputStream input) {
        try {
            close(input);
        } catch (IOException e) {
            // ignore exception
        }
    }
    
    /**
     * Null-safe closing of an ouputstream AND any exception during the close
     * will be discarded.
     * 
     * @param output 
     */
    static public void closeQuietly(OutputStream output) {
        try {
            close(output);
        } catch (IOException e) {
            // ignore exception
        }
    }
    
    /**
     * Reads all bytes from input to a byte array AND will close input at EOF.
     * 
     * @param input
     * @return
     * @throws IOException 
     */
    static public byte[] bytes(InputStream input) throws IOException {
        return bytes(input, true);
    }
    
    /**
     * Reads all bytes from input to a byte array AND will optionally close the
     * input at EOF.
     * 
     * @param input
     * @return
     * @throws IOException 
     */
    static public byte[] bytes(InputStream input, boolean close) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            InOuts.copy(input, output);
            return output.toByteArray();
        } finally {
            if (close) {
                close(input);
            }
        }
    }
 
    static public String string(InputStream input, Charset charset) throws IOException {
        return string(input, charset, true);
    }
 
    static public String string(InputStream input, Charset charset, boolean close) throws IOException {
        final byte[] bytes = bytes(input, close);
        return new String(bytes, charset);
    }
    
    static public String stringUTF8(InputStream input) throws IOException {
        return string(input, StandardCharsets.UTF_8, true);
    }
    
    static public String stringUTF8(InputStream input, boolean close) throws IOException {
        return string(input, StandardCharsets.UTF_8, close);
    }
    
    /**
     * Copies an input stream to an output stream in chunks (the buffer size).
     * Does NOT close the input or output streams.
     * @param input
     * @param output
     * @return
     * @throws IOException 
     */
    static public long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }
    
    /**
     * Copies an input stream to an output stream in chunks (the buffer size).
     * Does NOT close the input or output streams.
     * @param input
     * @param output
     * @param bufferSize
     * @return
     * @throws IOException 
     */
    static public long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        long copied = InOuts.consume(input, bufferSize, (data, read) -> {
            output.write(data, 0, read);
        });
        output.flush();
        return copied;
    }
 
    static public interface ByteReadConsumer {
        void apply(byte[] data, int read) throws IOException;
    }
    
    /**
     * Consumes an input stream in chunks (the buffer size) and allows you to 
     * simply write the consuming code. Does NOT close the inputstream.
     * @param input
     * @param consumer
     * @return
     * @throws IOException 
     */
    static public long consume(InputStream input, ByteReadConsumer consumer) throws IOException {
        return consume(input, DEFAULT_BUFFER_SIZE, consumer);
    }
    
    /**
     * Consumes an input stream in chunks (the buffer size) and allows you to 
     * simply write the consuming code. Does NOT close the inputstream.
     * @param input
     * @param bufferSize
     * @param consumer
     * @return
     * @throws IOException 
     */
    static public long consume(InputStream input, int bufferSize, ByteReadConsumer consumer) throws IOException {
        long totalRead = 0;
        int read;
        byte[] buffer = new byte[bufferSize];
        while ((read = input.read(buffer, 0, buffer.length)) != -1) {
            totalRead += read;
            consumer.apply(buffer, read);
        }
        return totalRead;
    }
    
}