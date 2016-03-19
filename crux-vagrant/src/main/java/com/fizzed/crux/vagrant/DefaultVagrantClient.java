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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class DefaultVagrantClient implements VagrantClient {
    
    private final Path workingDirectory;
    private final AtomicReference<Map<String,VagrantStatus>> statusRef;
    private final AtomicBoolean areAllMachinesRunning;
    private final AtomicBoolean areAnyMachinesRunning;
    private final AtomicReference<Path> sshConfigFileRef;

    DefaultVagrantClient(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.statusRef = new AtomicReference<>();
        this.areAllMachinesRunning = new AtomicBoolean();
        this.areAnyMachinesRunning = new AtomicBoolean();
        this.sshConfigFileRef = new AtomicReference<>();
    }

    @Override
    public Path workingDirectory() {
        return this.workingDirectory;
    }
    
    @Override
    public Map<String,VagrantStatus> fetchStatus() throws VagrantException {
        return fetchStatus(false);
    }
    
    @Override
    public Map<String,VagrantStatus> fetchStatus(boolean refresh) throws VagrantException {
        Map<String,VagrantStatus> status = this.statusRef.get();
        
        if (!refresh && status != null) {
            return status;
        }
        
        try {
            ProcessResult result
                = new ProcessExecutor()
                    .command("vagrant", "status", "--machine-readable")
                    .readOutput(true)
                    .exitValueNormal()
                    .execute();

            List<String> lines = VagrantUtil.parseLines(result);

            status = VagrantUtil.parseStatus(lines);
            
            // all/any machines running?
            boolean allMachinesRunning = (0 != status.size());      // true if some exist or false if no machines
            boolean anyMachinesRunning = false;
            for (VagrantStatus vs : status.values()) {
                if (!vs.isRunning()) {
                    allMachinesRunning = false;
                } else {
                    anyMachinesRunning = true;
                }
            }
            
            this.areAllMachinesRunning.set(allMachinesRunning);
            this.areAnyMachinesRunning.set(anyMachinesRunning);
            this.statusRef.set(status);
            
            return status;
        } catch (InvalidExitValueException e) {
            throw new VagrantException(e.getMessage());
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new VagrantException(e.getMessage(), e);
        }
    }
    
    @Override
    public boolean areAllMachinesRunning() throws VagrantException {
        return this.areAllMachinesRunning(false);
    }
    
    @Override
    public boolean areAllMachinesRunning(boolean refresh) throws VagrantException {
        if (!refresh && this.statusRef.get() != null) {
            return areAllMachinesRunning.get();
        }

        this.fetchStatus(refresh);
        return areAllMachinesRunning.get();
    }
    
    @Override
    public boolean areAnyMachinesRunning() throws VagrantException {
        return this.areAnyMachinesRunning(false);
    }
    
    @Override
    public boolean areAnyMachinesRunning(boolean refresh) throws VagrantException {
        if (!refresh && this.statusRef.get() != null) {
            return areAnyMachinesRunning.get();
        }
        
        this.fetchStatus(refresh);
        return areAnyMachinesRunning.get();
    }
    
    @Override
    public Set<String> fetchMachinesRunning() throws VagrantException {
        return fetchMachinesRunning(false);
    }
    
    @Override
    public Set<String> fetchMachinesRunning(boolean refresh) throws VagrantException {
        return this.fetchStatus(refresh).values().stream()
            .filter((status) -> status.isRunning())
            .map((status) -> status.getName())
            .collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
    }
    
    @Override
    public Path fetchSshConfig() throws VagrantException {
        return fetchSshConfig(false);
    }
    
    @Override
    public Path fetchSshConfig(boolean refresh) throws VagrantException {
        Path sshConfigFile = this.sshConfigFileRef.get();
        
        if (!refresh && sshConfigFile != null) {
            return sshConfigFile;
        }
        
        try {
            File tempFile = File.createTempFile("vagrant.", ".ssh-config");
            tempFile.deleteOnExit();
            sshConfigFile = tempFile.toPath();
        } catch (IOException e) {
            throw new VagrantException(e.getMessage(), e);
        }
        
        fetchSshConfig(sshConfigFile);
        this.sshConfigFileRef.set(sshConfigFile);
        return sshConfigFile;
    }
    
    @Override
    public void fetchSshConfig(Path sshConfigFile) throws VagrantException {
        try {
            ProcessResult result
                = new ProcessExecutor()
                    .command("vagrant", "ssh-config")
                    .readOutput(true)
                    .exitValueNormal()
                    .execute();
            
            // save .ssh-config
            Files.write(sshConfigFile,
                        result.output(),
                        StandardOpenOption.TRUNCATE_EXISTING);
            
            // fix ssh-config for windows
            // remove UserKnownHostsFile line
            // identity file probably wrong too
            List<String> filteredConfig
                = Files.lines(sshConfigFile)
                    .filter((line) -> !line.contains("UserKnownHostsFile"))
                    .map((line) -> (!line.contains("IdentityFile") ? line : line.replace("\"", "")))
                    .collect(Collectors.toList());
        
            Files.write(sshConfigFile,
                        filteredConfig,
                        StandardOpenOption.TRUNCATE_EXISTING);
        } catch (InvalidExitValueException e) {
            throw new VagrantException(e.getMessage());
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new VagrantException(e.getMessage(), e);
        }
    }
    
}
