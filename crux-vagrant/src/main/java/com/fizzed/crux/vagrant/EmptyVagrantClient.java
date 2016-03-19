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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An empty vagrant client to simplify use.
 */
public class EmptyVagrantClient implements VagrantClient {

    private final Path workingDirectory;

    public EmptyVagrantClient(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
    
    @Override
    public Path workingDirectory() {
        return this.workingDirectory;
    }
    
    @Override
    public boolean areAllMachinesRunning() throws VagrantException {
        return false;
    }

    @Override
    public boolean areAllMachinesRunning(boolean refresh) throws VagrantException {
        return false;
    }

    @Override
    public boolean areAnyMachinesRunning() throws VagrantException {
        return false;
    }

    @Override
    public boolean areAnyMachinesRunning(boolean refresh) throws VagrantException {
        return false;
    }

    @Override
    public Set<String> fetchMachinesRunning() throws VagrantException {
        return Collections.emptySet();
    }

    @Override
    public Set<String> fetchMachinesRunning(boolean refresh) throws VagrantException {
        return Collections.emptySet();
    }
    
    @Override
    public Path fetchSshConfig() throws VagrantException {
        return null;
    }

    @Override
    public Path fetchSshConfig(boolean refresh) throws VagrantException {
        return null;
    }

    @Override
    public void fetchSshConfig(java.nio.file.Path sshConfigFile) throws VagrantException {
        // do nothing
    }

    @Override
    public Map<String, VagrantStatus> fetchStatus() throws VagrantException {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, VagrantStatus> fetchStatus(boolean refresh) throws VagrantException {
        return Collections.emptyMap();
    }

}
