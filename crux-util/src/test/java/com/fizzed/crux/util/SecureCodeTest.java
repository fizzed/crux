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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class SecureCodeTest {
    
    @Test
    public void toStringWorks() {
        SecureCode code0 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code1 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code2 = new SecureCode(new byte[] { 2, 3 });
        
        assertThat(code0.toString(), is("AQI="));
        assertThat(code1.toString(), is("AQI="));
        assertThat(code2.toString(), is("AgM="));
    }

    @Test
    public void hashCodeWorks() {
        SecureCode code0 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code1 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code2 = new SecureCode(new byte[] { 2, 3 });
        
        assertThat(code0.hashCode(), is(code1.hashCode()));
        assertThat(code1.hashCode(), is(not(code2.hashCode())));
    }
    
    @Test
    public void equalsWorks() {
        SecureCode code0 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code1 = new SecureCode(new byte[] { 1, 2 });
        SecureCode code2 = new SecureCode(new byte[] { 2, 3 });
        
        assertThat(code0, is(code1));
        assertThat(code1, is(not(code2)));
    }
    
}
