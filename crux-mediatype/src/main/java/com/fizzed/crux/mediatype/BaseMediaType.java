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
package com.fizzed.crux.mediatype;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;

public enum BaseMediaType {

    APPLICATION("application"),
    IMAGE("image"),
    FONT("font"),
    MULTIPART("multipart"),
    VIDEO("video"),
    TEXT("text"),
    MESSAGE("message");
    
    static private final Map<String,BaseMediaType> LABELS;
    static {
        LABELS = new HashMap<>();
        for (BaseMediaType mediaType : BaseMediaType.values()) {
            // case insensitive label
            String label = mediaType.getLabel().toLowerCase();
            if (LABELS.containsKey(label)) {
                throw new IllegalArgumentException("Uh oh - duplicate label " + label
                    + " found with " + mediaType + ". You probably want to check your code.");
            }
            LABELS.put(label, mediaType);
        }
    }
    
    private final String label;

    BaseMediaType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
    
    // helpers
    
    static public Optional<BaseMediaType> fromLabel(String label) {
        if (label != null) {
            label = label.trim().toLowerCase();                     // sanitize a bit
            return ofNullable(LABELS.get(label));
        }
        return Optional.empty();
    }
    
}