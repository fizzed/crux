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
package com.fizzed.crux.vagrant;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class VagrantStatus {
    
    private final String name;
    private final Map<String,String> values;

    public VagrantStatus(String name) {
        this.name = name;
        this.values = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getValues() {
        return values;
    }
    
    public boolean isRunning() {
        String state = values.get("state");
        return Objects.equals(state, "running");
    }

    @Override
    public String toString() {
        return "Status{name=" + name + ", values=" + values + '}';
    }
    
}
