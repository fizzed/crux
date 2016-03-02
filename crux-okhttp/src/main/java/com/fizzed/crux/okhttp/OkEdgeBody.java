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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class OkEdgeBody extends OkEdgeDone {
    
    static private final byte[] EMPTY_BYTES = new byte[0];
    
    private final String contentType;
    private RequestBody body;
    
    OkEdgeBody(OkEdge edge, String contentType) {
        super(edge);
        this.contentType = contentType;
    }
    
    RequestBody getRequestBody() {
        return this.body;
    }
    
    public OkEdge emptyBody() {
        this.body = RequestBody.create(MediaType.parse(contentType), EMPTY_BYTES);
        return this.done();
    }
    
    /**
    public OkEdge body(InputStream input) {
        this.body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse(contentType);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                OutputStream output = sink.outputStream();
                Files.
            }
        }
        return this.commit();
    }
    */
    
    public OkEdge body(File file) {
        this.body = RequestBody.create(MediaType.parse(contentType), file);
        return this.done();
    }
    
    public OkEdge body(byte[] bytes) {
        this.body = RequestBody.create(MediaType.parse(contentType), bytes);
        return this.done();
    }
    
    public OkEdge body(String string) {
        this.body = RequestBody.create(MediaType.parse(contentType), string);
        return this.done();
    }
    
}
