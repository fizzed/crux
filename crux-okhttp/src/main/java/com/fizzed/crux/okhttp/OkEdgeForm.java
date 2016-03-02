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

import java.util.Objects;
import okhttp3.FormBody;

public class OkEdgeForm extends OkEdgeDone {
    
    private final FormBody.Builder formBodyBuilder;
    
    OkEdgeForm(OkEdge edge) {
        super(edge);
        this.formBodyBuilder = new FormBody.Builder();
    }
    
    FormBody.Builder getFormBodyBuilder() {
        return this.formBodyBuilder;
    }
    
    public OkEdgeForm add(String name, Object value) {
        this.formBodyBuilder.add(name, Objects.toString(value, ""));
        return this;
    }
    
}
