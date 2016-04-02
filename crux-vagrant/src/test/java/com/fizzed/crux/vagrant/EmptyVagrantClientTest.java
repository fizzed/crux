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
import java.net.URISyntaxException;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class EmptyVagrantClientTest {
    
    @Test
    public void allMethods() throws URISyntaxException, IOException {
        VagrantClient client = VagrantClients.emptyClient();
        assertThat(client.areAllMachinesRunning(), is(false));
        assertThat(client.areAnyMachinesRunning(), is(false));
        assertThat(client.machinesRunning(), hasSize(0));
        assertThat(client.sshConfig("this-does-not-exist"), is(nullValue()));
        assertThat(client.status(), anEmptyMap());
        assertThat(client.workingDirectory(), is(not(nullValue())));
    }
    
}
