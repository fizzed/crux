/*
 * Copyright 2018 Fizzed, Inc.
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
package com.fizzed.crux.mediatype;

import java.nio.charset.StandardCharsets;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class ContentTypeTest {
    
    @Test
    public void parseNull() {
        ContentType contentType = ContentType.parse(null);
        
        assertThat(contentType.getParsed(), is(nullValue()));
        assertThat(contentType.getKnownMediaType(), is(KnownMediaType.APPLICATION_OCTET_STREAM));
        assertThat(contentType.getType(), is(nullValue()));
        assertThat(contentType.getSubType(), is(nullValue()));
        assertThat(contentType.getCharset(), is(nullValue()));
    }
    
    @Test
    public void parseBlank() {
        ContentType contentType = ContentType.parse("  ");
        
        assertThat(contentType.getParsed(), is("  "));
        assertThat(contentType.getKnownMediaType(), is(KnownMediaType.APPLICATION_OCTET_STREAM));
        assertThat(contentType.getType(), is(nullValue()));
        assertThat(contentType.getSubType(), is(nullValue()));
        assertThat(contentType.getCharset(), is(nullValue()));
    }
    
    @Test
    public void parseBare() {
        ContentType contentType = ContentType.parse("application/json");
        
        assertThat(contentType.getParsed(), is("application/json"));
        assertThat(contentType.getKnownMediaType(), is(KnownMediaType.APPLICATION_JSON));
        assertThat(contentType.getType(), is("application"));
        assertThat(contentType.getSubType(), is("json"));
        assertThat(contentType.getCharset(), is(nullValue()));
    }
    
    @Test
    public void parseWithCharset() {
        ContentType contentType = ContentType.parse("application/json; charset=utf-8");
        
        assertThat(contentType.getParsed(), is("application/json; charset=utf-8"));
        assertThat(contentType.toString(), is("application/json; charset=utf-8"));
        assertThat(contentType.getKnownMediaType(), is(KnownMediaType.APPLICATION_JSON));
        assertThat(contentType.getType(), is("application"));
        assertThat(contentType.getSubType(), is("json"));
        assertThat(contentType.getCharset(), is(StandardCharsets.UTF_8));
    }
    
    @Test
    public void parseCaseInsensitive() {
        ContentType contentType = ContentType.parse("application/JSON");
        
        assertThat(contentType.getParsed(), is("application/JSON"));
        assertThat(contentType.getKnownMediaType(), is(KnownMediaType.APPLICATION_JSON));
        assertThat(contentType.getType(), is("application"));
        assertThat(contentType.getSubType(), is("json"));
        assertThat(contentType.getCharset(), is(nullValue()));
    }
    
}
