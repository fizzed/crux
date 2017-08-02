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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryBuilder {
    
    private String name;
    private Boolean daemon;

    public ThreadFactoryBuilder(String name) {
        this(name, null);
    }

    public ThreadFactoryBuilder(String name, Boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }
    
    public ThreadFactoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(Boolean daemon) {
        this.daemon = daemon;
        return this;
    }
    
    public ThreadFactory build() {
        return new ThreadFactory() {
            private final AtomicInteger sequence = new AtomicInteger();
            
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName(name + "-" + sequence.getAndIncrement());
                if (daemon != null) {
                    thread.setDaemon(daemon);
                }
                return thread;
            }
        };
    }
    
}
