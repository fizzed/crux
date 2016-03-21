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

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CachingVagrantClient extends DefaultVagrantClient {
    
    static public class Builder extends VagrantClient.Builder<Builder> {
        
        @Override
        public VagrantClient build() throws VagrantException {
            return new CachingVagrantClient(this.workingDirectory);
        }
        
    }
    
    private final AtomicReference<Map<String,MachineStatus>> statusRef;

    protected CachingVagrantClient(Path workingDirectory) {
        super(workingDirectory);
        this.statusRef = new AtomicReference<>();
    }
    
    @Override
    public Map<String,MachineStatus> status() throws VagrantException {
        Map<String,MachineStatus> status = this.statusRef.get();
        
        if (status != null) {
            return status;
        }
        
        status = super.status();
        this.statusRef.set(status);
        
        return status;
    }
    
}
