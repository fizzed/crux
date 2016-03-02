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
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OkEdgeBody extends OkEdgeCommit {
    
    private final String contentType;
    private RequestBody body;
    
    OkEdgeBody(OkEdge edge, String contentType) {
        super(edge);
        this.contentType = contentType;
    }
    
    RequestBody getRequestBody() {
        return this.body;
    }
    
    public OkEdge body(File file) {
        this.body = RequestBody.create(MediaType.parse(contentType), file);
        return this.commit();
    }
    
    public OkEdge body(byte[] bytes) {
        this.body = RequestBody.create(MediaType.parse(contentType), bytes);
        return this.commit();
    }
    
    public OkEdge body(String string) {
        this.body = RequestBody.create(MediaType.parse(contentType), string);
        return this.commit();
    }
    
}
