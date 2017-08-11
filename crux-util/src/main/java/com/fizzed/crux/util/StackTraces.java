/*
 * Copyright 2017 Fizzed, Inc.
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utilities for working with stacktraces.
 * 
 * @author joelauer
 */
public class StackTraces {
   
    static public List<StackTraceElement> current() {
        return of(Thread.currentThread());
    }
    
    static public List<StackTraceElement> of(Supplier<Thread> supplier) {
        return of(supplier.get());
    }
    
    static public List<StackTraceElement> of(Thread thread) {
        return Arrays.asList(thread.getStackTrace());
    }
    
    static public boolean anyMatch(Thread thread, Predicate<StackTraceElement> predicate) {
        return of(thread).stream().anyMatch(predicate);
    }
    
}
