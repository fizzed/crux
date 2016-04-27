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
package com.fizzed.crux.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class SecureCodesTest {
    
    @Test
    public void code24() {
        SecureCode code = SecureCodes.getInstance().code24();
        System.out.println("code24: " + code);
        
        assertThat(code.toString().length(), is(32));
    }
    
    @Test
    public void code36() {
        SecureCode code = SecureCodes.getInstance().code36();
        System.out.println("code36: " + code);
        
        assertThat(code.toString().length(), is(48));
    }
    
    @Test
    public void code48() {
        SecureCode code = SecureCodes.getInstance().code48();
        System.out.println("code48: " + code);
        
        assertThat(code.toString().length(), is(64));
    }
    
    @Test
    public void code60() {
        SecureCode code = SecureCodes.getInstance().code60();
        System.out.println("code60: " + code);
        
        assertThat(code.toString().length(), is(80));
    }
    
}
