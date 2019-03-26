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

import static com.fizzed.crux.util.UUIDs.toBytes;
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
        assertThat(Base16.encode(toBytes("470276ef-152d-4a43-8bf7-ffe70affb551")), is("470276ef152d4a438bf7ffe70affb551"));
        assertThat(Base16.encode(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }), is("0123456789abcdef"));
    }
 
    @Test
    public void encodeUpper() throws UnsupportedEncodingException {
        assertThat(Base16.encode(null), is(nullValue()));
        assertThat(Base16.encode(new byte[0]), is(""));
        assertThat(Base16.encode(new byte[1]), is("00"));
        assertThat(Base16.encode(new byte[2]), is("0000"));
        assertThat(Base16.encode("test".getBytes("UTF-8")), is("74657374"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8"), true), is("E282AC"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8"), true, 2), is("E282"));
        assertThat(Base16.encode("\u20AC".getBytes("UTF-8"), true, 0), is(""));
        assertThat(Base16.encode(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }, true), is("0123456789ABCDEF"));
    }
    
    @Test
    public void decode() throws UnsupportedEncodingException {
        assertThat(Base16.decode(null), is(nullValue()));
        assertThat(Base16.decode(""), is(new byte[0]));
        assertThat(Base16.decode("00"), is(new byte[1]));
        assertThat(Base16.decode("0000"), is(new byte[2]));
        assertThat(Base16.decode("74657374"), is("test".getBytes("UTF-8")));
        assertThat(Base16.decode("e282ac"), is("\u20AC".getBytes("UTF-8")));
        assertThat(Base16.decode("E282AC"), is("\u20AC".getBytes("UTF-8")));
        assertThat(Base16.decode("e282ac"), is("\u20AC".getBytes("UTF-8")));
        assertThat(Base16.decode("0123456789ABCDEF"), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
        assertThat(Base16.decode("0123456789abcdef"), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
    }

    @Test
    public void encodeDecodeHuge() {
        for (int length = 0; length < 30000; length+=57) {
            byte[] bytes = new byte[length];
            // initialize data
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte)(i % 127);
            }
            String encoded = Base16.encode(bytes);
            byte[] decoded = Base16.decode(encoded);
            assertThat(decoded, is(bytes));
        }
    }
    
}