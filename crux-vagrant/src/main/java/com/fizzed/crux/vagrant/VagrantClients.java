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

public class VagrantClients {

    static public VagrantClient defaultClient() throws VagrantException {
        return new CachingVagrantClient.Builder()
            .build();
    }
    
    static public VagrantClient cachingClient() throws VagrantException {
        return new CachingVagrantClient.Builder()
            .build();
    }
    
    static public VagrantClient emptyClient() throws VagrantException {
        return new EmptyVagrantClient.Builder()
            .build();
    }
    
    static public VagrantClient cachingOrEmptyClient() throws VagrantException {
        try {
            VagrantClient cachingClient = cachingClient();
            cachingClient.status();     // may throw exception
            return cachingClient;
        } catch (Exception e) {
            return emptyClient();
        }
    }
    
}
