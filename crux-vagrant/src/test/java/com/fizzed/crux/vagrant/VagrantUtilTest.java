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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class VagrantUtilTest {
    
    @Test
    public void parseStatus() throws URISyntaxException, IOException {
        URI uri = VagrantUtilTest.class.getResource("/vagrant/status1.txt").toURI();
        Path statusFile = Paths.get(uri);
        
        Map<String, VagrantStatus> statuses
            = VagrantUtil.parseStatus(Files.readAllLines(statusFile));
        
        assertThat(statuses, hasKey("debian8"));
        assertThat(statuses, hasKey("centos7"));
        assertThat(statuses, hasKey("ubuntu1404"));
        
        VagrantStatus debian8 = statuses.get("debian8");
        assertThat(debian8.isRunning(), is(true));
        assertThat(debian8.getValues(), hasKey("provider-name"));
        
        VagrantStatus centos7 = statuses.get("centos7");
        assertThat(centos7.isRunning(), is(true));
        assertThat(centos7.getValues(), hasKey("provider-name"));
        
        VagrantStatus ubuntu1404 = statuses.get("ubuntu1404");
        assertThat(ubuntu1404.isRunning(), is(true));
        assertThat(ubuntu1404.getValues(), hasKey("provider-name"));
    }
    
}
