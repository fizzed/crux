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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InOuts {
    
    static public final int DEFAULT_BUFFER_SIZE = 2048;
    
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