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
import java.util.Set;

public interface VagrantClient {
    
    Path workingDirectory();

    boolean areAllMachinesRunning() throws VagrantException;

    boolean areAnyMachinesRunning() throws VagrantException;
    
    Set<String> machinesRunning() throws VagrantException;

    /**
     * Gets a temporary file that is the ssh config file for connecting to
     * the specified machine names.
     * @param machineNames May be null or a list of machine names
     * @return A temporary file that is the ssh config file
     * @throws VagrantException 
     */
    Path sshConfig(String... machineNames) throws VagrantException;

    void sshConfigWrite(Path sshConfigFile, String... machineNames) throws VagrantException;

    Map<String,MachineStatus> status() throws VagrantException;

    static abstract public class Builder<T> {
        
        protected Path workingDirectory;

        public Builder() {
        }

        public T workingDirectory(Path workingDirectory) {
            this.workingDirectory = workingDirectory;
            return (T)this;
        }
        
        abstract public VagrantClient build() throws VagrantException;
    }
    
}
