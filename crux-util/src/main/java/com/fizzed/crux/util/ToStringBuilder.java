/*
 * Copyright 2020 Fizzed, Inc.
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

import java.util.Objects;

public class ToStringBuilder {
 
    private final String fieldDelimiter;
    private final String valueDelimiter;
    private StringBuilder sb;

    public ToStringBuilder() {
        this(", ", "=");
    }

    public ToStringBuilder(
            String fieldDelimiter) {
        
        this(fieldDelimiter, "=");
    }

    public ToStringBuilder(
            String fieldDelimiter,
            String valueDelimiter) {
        
        this.fieldDelimiter = fieldDelimiter;
        this.valueDelimiter = valueDelimiter;
    }
    
    @Override
    public String toString() {
        if (this.sb != null) {
            return this.sb.toString();
        }
        return null;
    } 

    public ToStringBuilder nb(
            Object value) {
        
        String s = Objects.toString(value, null);
        
        if (s != null && !s.isEmpty() && !s.trim().isEmpty()) {
            this.a(s);
        }
        
        return this;
    }

    public ToStringBuilder nn(
            Object value) {
        
        if (value != null) {
            this.a(value);
        }
        
        return this;
    }

    public ToStringBuilder a(
            Object value) {
        
        if (this.sb == null) {
            this.sb = new StringBuilder();
        }
        
        this.sb.append(value);
        
        return this;
    }

    public ToStringBuilder a(
            ToStringBuilder other) {
        
        if (other != null && other.sb != null) {
            if (this.sb == null) {
                this.sb = new StringBuilder();
            }

            this.sb.append(other.sb);
        }
        
        return this;
    }
    
    /**
     * Appends key and value if value is NOT blank (empty).
     * 
     * @param fieldName
     * @param fieldValue
     * @return 
     */
    public ToStringBuilder nb(
            String fieldName, Object fieldValue) {
        
        String s = Objects.toString(fieldValue, null);
        
        if (s != null && !s.isEmpty() && !s.trim().isEmpty()) {
            this.a(fieldName, s);
        }
        
        return this;
    }

    /**
     * Appends key and value if value is NOT null.
     * 
     * @param fieldName
     * @param fieldValue
     * @return 
     */
    public ToStringBuilder nn(
            String fieldName, Object fieldValue) {
        
        if (fieldValue != null) {
            this.a(fieldName, fieldValue);
        }
        
        return this;
    }

    /**
     * Appends key and value regardless of whether value is null.
     * 
     * @param fieldName
     * @param fieldValue
     * @return 
     */
    public ToStringBuilder a(
            String fieldName, Object fieldValue) {
        
        if (sb == null) {
            sb = new StringBuilder();
        }
        else if (sb.length() > 0) {
            sb.append(this.fieldDelimiter);
        }
        
        sb.append(fieldName);
        
        if (this.valueDelimiter != null) {
            sb.append(this.valueDelimiter);
        }
        
        sb.append(Objects.toString(fieldValue, null));
        
        return this;
    }
    
}