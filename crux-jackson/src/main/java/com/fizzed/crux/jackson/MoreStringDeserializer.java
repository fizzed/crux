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
package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.io.IOException;

public class MoreStringDeserializer extends StdDeserializer<String> {
    
    private final boolean trim;
    private final boolean emptyToNull;

    public MoreStringDeserializer(
            boolean trim,
            boolean emptyToNull) {
        
        super(String.class);
        this.trim = trim;
        this.emptyToNull = emptyToNull;
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        String v = StringDeserializer.instance.deserialize(parser, context);
        
        if (trim && v != null && v.length() > 0) {
            v = v.trim();
        }
        
        if (emptyToNull && (v == null || v.isEmpty())) {
            return null;
        }

        return v;
    }
    
}