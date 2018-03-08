/*
 * Copyright 2017 Fizzed, Inc.
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
package com.fizzed.crux.okhttp;

import com.fizzed.crux.uri.Uri;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class OkHttpOptionsTest {
 
    static public class TestOptions extends OkHttpOptions<TestOptions> {
        
        private String accessToken;
        
        @SuppressWarnings("OverridableMethodCallInConstructor")
        public TestOptions() {
            this.bindingPropertyMap.bindString("access_token", TestOptions::setAccessToken);
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
        
    }
    
    @Test
    public void create() {
        OkHttpOptions options = new OkHttpOptions();
        options.setUri(new Uri("http://localhost?connect_timeout=1000"));
        
        assertThat(options.getConnectTimeout(), is(1000L));
    }
    
    @Test
    public void createSubclass() {
        TestOptions options = new TestOptions();
        options.setUri(new Uri("http://localhost?access_token=test"));
        
        assertThat(options.getAccessToken(), is("test"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createWithInvalidProperty() {
        TestOptions options = new TestOptions();
        options.setUri(new Uri("http://localhost?access_token2=test"), false);
        assertThat(options.getAccessToken(), is("test"));
    }
    
}