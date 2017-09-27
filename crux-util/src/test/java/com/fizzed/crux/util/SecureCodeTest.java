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
import static org.junit.Assert.fail;
import org.junit.Test;

public class SecureCodeTest {
    
    // 128-bit
    static final byte[] CODE16_0 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    static final byte[] CODE24_0 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
    // 256-bit
    static final byte[] CODE32_0 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
    
    @Test
    public void encode() {
        SecureCode code;
        
        code = new SecureCode(new byte[] { 0 });
        assertThat(code.toString(), is("AA"));
        
        code = new SecureCode(new byte[] { 0, 1 });
        assertThat(code.toString(), is("AAE"));
        
        code = new SecureCode(new byte[] { 1, 2 });
        assertThat(code.toString(), is("AQI"));
        
        code = new SecureCode(new byte[] { 2, 3 });
        assertThat(code.toString(), is("AgM"));
        
        code = new SecureCode(new byte[] { 2, 3, 4 });
        assertThat(code.toString(), is("AgME"));
        
        code = new SecureCode(CODE16_0);
        assertThat(code.toString(), is("AAECAwQFBgcICQoLDA0ODw"));
        
        code = new SecureCode(CODE24_0);
        assertThat(code.toString(), is("AAECAwQFBgcICQoLDA0ODxAREhMUFRYX"));
        
        code = new SecureCode(CODE32_0);
        assertThat(code.toString(), is("AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8"));
    }
    
    @Test
    public void decode() {
        SecureCode code;
        
        code = new SecureCode("AAE=");
        assertThat(code.getBytes(), is(new byte[] { 0, 1 }));
        
        // leave padding char off (same value)
        code = new SecureCode("AAE");
        assertThat(code.getBytes(), is(new byte[] { 0, 1 }));

        code = new SecureCode("AAECAwQFBgcICQoLDA0ODw==");
        assertThat(code.getBytes(), is(CODE16_0));
        
        // leave padding char off
        code = new SecureCode("AAECAwQFBgcICQoLDA0ODw");
        assertThat(code.getBytes(), is(CODE16_0));
        
        code = new SecureCode("AAECAwQFBgcICQoLDA0ODxAREhMUFRYX");
        assertThat(code.getBytes(), is(CODE24_0));
        
        code = new SecureCode("AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8");
        assertThat(code.getBytes(), is(CODE32_0));
        
        // add padding
        code = new SecureCode("AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8=");
        assertThat(code.getBytes(), is(CODE32_0));
        
        try {
            code = new SecureCode("AAECAwQFBgcICQoLDA0ODw=");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
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
