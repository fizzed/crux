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

import java.io.UnsupportedEncodingException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class Base16Test {
 
    @Test
    public void encode() throws UnsupportedEncodingException {
        assertThat(Base16.encode(null), is(nullValue()));
        assertThat(Base16.encode(new byte[0]), is(""));
        assertThat(Base16.encode(new byte[1]), is("00"));
        assertThat(Base16.encode(new byte[2]), is("0000"));
        assertThat(Base16.encode("test".getBytes("UTF-8")), is("74657374"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8")), is("e282ac"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8"), 2), is("e282"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8"), 0), is(""));
    }
    
}