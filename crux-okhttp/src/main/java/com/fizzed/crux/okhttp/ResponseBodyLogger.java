/*
 * Copyright 2016 Fizzed, Inc.
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
package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class ResponseBodyLogger {
    
    static public final long MAX_BODY_SIZE_TO_LOG = 30000L;
    
    static public void log(final Response response, final ResponseBody responseBody) throws IOException {
        // much safer body logger in case where the body shouldn't be logged
        // smart detection of file downloads, etc.
        long contentLength = responseBody.contentLength();
        
        if (contentLength >= MAX_BODY_SIZE_TO_LOG) {
            System.out.println("");
            System.out.println("Body content length exceeds max body size you'd want to log!");
            System.out.println("<-- END HTTP (" +contentLength + "-byte body)");
            return;
        }
        
        BufferedSource source = responseBody.source();
        source.request(MAX_BODY_SIZE_TO_LOG);         // Buffer up to max size we'd log
        Buffer buffer = source.buffer();

        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(charset);
            } catch (UnsupportedCharsetException e) {
                System.out.println("");
                System.out.println("Couldn't decode the response body; charset is malformed.");
                System.out.println("<-- END HTTP");
                return;
            }
        }

        if (contentLength != 0) {
            System.out.println("");
            System.out.println(buffer.clone().readString(charset));
        }

        System.out.println("<-- END HTTP (" + buffer.size() + "-byte body)");
    }
    
}
