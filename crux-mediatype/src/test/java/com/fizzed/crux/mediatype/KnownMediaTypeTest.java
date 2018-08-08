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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class KnownMediaTypeTest {
    
    @Test
    public void fromLabel() {
        assertThat(KnownMediaType.fromLabel("application/json").orElse(null),
            is(KnownMediaType.APPLICATION_JSON));
        assertThat(KnownMediaType.fromLabel(" application/json ").orElse(null),
            is(KnownMediaType.APPLICATION_JSON));
        assertThat(KnownMediaType.fromLabel("apPlication/Json").orElse(null),
            is(KnownMediaType.APPLICATION_JSON));
    }
    
    @Test
    public void fromExtension() {
        assertThat(KnownMediaType.fromExtension("json").orElse(null),
            is(KnownMediaType.APPLICATION_JSON));
        assertThat(KnownMediaType.fromExtension("jpg").orElse(null),
            is(KnownMediaType.IMAGE_JPEG));
        assertThat(KnownMediaType.fromExtension("jpeg").orElse(null),
            is(KnownMediaType.IMAGE_JPEG));
    }
    
    @Test
    public void fromFileName() {
        assertThat(KnownMediaType.fromFileName("test.jpg").orElse(null), is(KnownMediaType.IMAGE_JPEG));
        assertThat(KnownMediaType.fromFileName("test.jpeg").orElse(null), is(KnownMediaType.IMAGE_JPEG));
        assertThat(KnownMediaType.fromFileName(".jpeg").orElse(null), is(KnownMediaType.IMAGE_JPEG));
        assertThat(KnownMediaType.fromFileName(".json").orElse(null), is(KnownMediaType.APPLICATION_JSON));
        assertThat(KnownMediaType.fromFileName("").orElse(null), is(nullValue()));
        assertThat(KnownMediaType.fromFileName(null).orElse(null), is(nullValue()));
    }
    
    @Test
    public void fromHeader() {
        assertThat(KnownMediaType.fromHeader("image/jpeg").orElse(null), is(KnownMediaType.IMAGE_JPEG));
    }
    
    @Test
    public void isSame() {
        assertThat(KnownMediaType.APPLICATION_JSON.isSame(null), is(false));
        assertThat(KnownMediaType.APPLICATION_JSON.isSame(KnownMediaType.IMAGE_GIF), is(false));
        assertThat(KnownMediaType.APPLICATION_JSON.isSame(KnownMediaType.APPLICATION_JSON), is(true));
        assertThat(KnownMediaType.APPLICATION_JSON.isSame(KnownMediaType.APPLICATION_VND_API_JSON), is(true));
    }
    
}
