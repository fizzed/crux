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
import java.util.concurrent.TimeoutException;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class VagrantClients {

    static public boolean isVagrantInstalled() {
        try {
            ProcessResult result
                = new ProcessExecutor()
                    // "vagrant version" verifies you have latest version, while "vagrant -v"
                    // quickly prints version and then exits
                    .command(VagrantUtil.COMMAND, "-v")
                    .readOutput(true)
                    .exitValueNormal()
                    .execute();
            return true;
        } catch (IOException | InterruptedException | TimeoutException | InvalidExitValueException e) {
            return false;
        }
    }
    
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
        // is vagrant installed?
        if (isVagrantInstalled()) { 
            try {
                // try to fetch initial status in order to fallback to empty...
                VagrantClient cachingClient = cachingClient();
                cachingClient.status();     // may throw exception
                return cachingClient;
            } catch (Exception e) {
                // do nothing, let fallthru to end
            }
        }
        
        return emptyClient();
    }
    
}
