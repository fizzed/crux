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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class DefaultVagrantClient extends BaseVagrantClient {
    
    static public class Builder extends VagrantClient.Builder<Builder> {
        
        @Override
        public VagrantClient build() throws VagrantException {
            return new DefaultVagrantClient(this.workingDirectory);
        }
        
    }

    protected DefaultVagrantClient(Path workingDirectory) {
        super(workingDirectory);
    }

    @Override
    public Map<String,MachineStatus> status() throws VagrantException {
        try {
            ProcessResult result
                = new ProcessExecutor()
                    .command(VagrantUtil.COMMAND, "status", "--machine-readable")
                    .readOutput(true)
                    .exitValueNormal()
                    .execute();

            List<String> lines = VagrantUtil.parseLines(result);

            return VagrantUtil.parseStatus(lines);
        } catch (InvalidExitValueException e) {
            throw new VagrantException(e.getMessage());
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new VagrantException(e.getMessage(), e);
        }
    }
    
    @Override
    public void sshConfigWrite(Path sshConfigFile, String... machineNames) throws VagrantException {
        try {
            // build command so we can append machine names if necessary
            List<String> commands = new ArrayList<>();
            commands.add(VagrantUtil.COMMAND);
            commands.add("ssh-config");
            
            if (machineNames != null) {
                commands.addAll(Arrays.asList(machineNames));
            }
            
            ProcessResult result
                = new ProcessExecutor()
                    .command(commands)
                    .readOutput(true)
                    .exitValueNormal()
                    .execute();
            
            // save .ssh-config
            Files.write(sshConfigFile,
                        result.output(),
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
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
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (InvalidExitValueException e) {
            throw new VagrantException(e.getMessage());
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new VagrantException(e.getMessage(), e);
        }
    }
    
}
