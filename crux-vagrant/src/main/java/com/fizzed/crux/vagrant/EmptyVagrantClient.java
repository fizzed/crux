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
import java.util.Collections;
import java.util.Map;

public class EmptyVagrantClient extends BaseVagrantClient {

    static public class Builder extends VagrantClient.Builder<Builder> {
        
        @Override
        public VagrantClient build() throws VagrantException {
            return new EmptyVagrantClient(this.workingDirectory);
        }
        
    }

    protected EmptyVagrantClient(Path workingDirectory) {
        super(workingDirectory);
    }
    
    // override methods that actually make calls out to vagrant
    
    @Override
    public Map<String,MachineStatus> status() throws VagrantException {
        return Collections.emptyMap();
    }

    @Override
    public Path sshConfig(String... machineNames) throws VagrantException {
        return null;
    }

    @Override
    public void sshConfigWrite(Path sshConfigFile, String... machineNames) throws VagrantException {
        // do nothing
    }
    
}
