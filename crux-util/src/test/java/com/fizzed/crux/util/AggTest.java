/*
 * Copyright 2020 Fizzed, Inc.
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

import com.fizzed.crux.util.Agg.Max;
import com.fizzed.crux.util.Agg.Min;
import java.util.Comparator;
import static java.util.Comparator.naturalOrder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class AggTest {
 
    @Test
    public void min1() {
        Min min = Agg.min(Integer.TYPE);
        
        assertThat(min.get(), is(nullValue()));
        
        min.apply(4);
        
        assertThat(min.get(), is(4));
        
        min.apply(5);
        
        assertThat(min.get(), is(4));
        
        min.apply(3);
        
        assertThat(min.get(), is(3));
        
        min.apply(2);
        
        assertThat(min.get(), is(2));
        
    }
    
    @Test
    public void min2() {
        Min<Integer> min = Agg.min(10);
        
        assertThat(min.get(), is(10));
        
        min.apply(4);
        
        assertThat(min.get(), is(4));
        
        min.apply(5);
        
        assertThat(min.get(), is(4));
        
        min.apply(3);
        
        assertThat(min.get(), is(3));
        
        min.apply(2);
        
        assertThat(min.get(), is(2));
        
        min.apply(null);
        
        assertThat(min.get(), is(2));        
    }
    
    @Test
    public void minIncludeNulls() {
        Comparator<Integer> c = Comparator.nullsFirst(naturalOrder());
        
        Min<Integer> max = Agg.min(c, 10);
        
        assertThat(max.get(), is(10));
        
        max.apply(4, true);
        
        assertThat(max.get(), is(4));
        
        max.apply(12, true);
        
        assertThat(max.get(), is(4));
        
        max.apply(null, true);
        
        assertThat(max.get(), is(nullValue()));        
    }
    
    @Test
    public void max1() {
        Max<Integer> max = Agg.max(Integer.TYPE);
        
        assertThat(max.get(), is(nullValue()));
        
        max.apply(4);
        
        assertThat(max.get(), is(4));
        
        max.apply(5);
        
        assertThat(max.get(), is(5));
        
        max.apply(3);
        
        assertThat(max.get(), is(5));
        
        max.apply(2);
        
        assertThat(max.get(), is(5));
        
        max.apply(6);
        
        assertThat(max.get(), is(6));
        
    }
    
    @Test
    public void max2() {
        Max<Integer> min = Agg.max(10);
        
        assertThat(min.get(), is(10));
        
        min.apply(4);
        
        assertThat(min.get(), is(10));
        
        min.apply(12);
        
        assertThat(min.get(), is(12));
        
        min.apply(null);
        
        assertThat(min.get(), is(12));        
    }
    
    @Test
    public void maxIncludeNulls() {
        Comparator<Integer> c = Comparator.nullsFirst(naturalOrder());
        
        Max<Integer> max = Agg.max(c, 10);
        
        assertThat(max.get(), is(10));
        
        max.apply(4, true);
        
        assertThat(max.get(), is(10));
        
        max.apply(12, true);
        
        assertThat(max.get(), is(12));
        
        max.apply(null, true);
        
        assertThat(max.get(), is(12));        
    }
    
}