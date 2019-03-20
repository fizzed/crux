/*
 * Copyright 2019 Fizzed, Inc.
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
package com.fizzed.crux.util;

import java.io.InputStream;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class HasherTest {
 
    @Test
    public void md5() throws Exception {
        Hasher hasher;
        
        hasher = Hasher.md5();
        
        // empty (zero bytes)
        assertThat(hasher.asHex(), is("d41d8cd98f00b204e9800998ecf8427e"));
        
        try {
            hasher.update(new byte[0]);
            fail("should have failed");
        } catch (IllegalStateException e) {
            // expected
        }
        
        hasher = Hasher.md5();
        
        hasher.update(new byte[] { 1, 2, 3, 4, 5 });
        
        assertThat(hasher.asHex(), is("7cfdd07889b3295d6a550914ab35e068"));
        
        
        hasher = Hasher.md5();
        
        hasher.update(new byte[] { 1, 2, 3 });
        hasher.update(new byte[] { 4, 5 });
        
        assertThat(hasher.asHex(), is("7cfdd07889b3295d6a550914ab35e068"));
        
        
        hasher = Hasher.md5();
        
        hasher.update(new byte[] { 1 });
        hasher.update(new byte[] { 2 });
        hasher.update(new byte[] { 3 });
        hasher.update(new byte[] { 4, 5 });
        
        assertThat(hasher.asHex(), is("7cfdd07889b3295d6a550914ab35e068"));
        
        
        hasher = Hasher.md5();
        
        hasher.update("test".getBytes("UTF-8"));
        
        assertThat(hasher.asHex(), is("098f6bcd4621d373cade4e832627b4f6"));
        
        
        // try a file now...
        try (InputStream input = Resources.newInputStream("/fixtures/rect4136.png")) {
            Hasher hasher2 = Hasher.md5();
            
            long read = InOuts.consume(input, (bytes, length) -> {
                hasher2.update(bytes, 0, length);
            });
            
            assertThat(read, is(58321L));
            assertThat(hasher2.asHex(), is("392c2e223f14328b4c7b3f8cae5c4dce"));
        }
    }
    
}