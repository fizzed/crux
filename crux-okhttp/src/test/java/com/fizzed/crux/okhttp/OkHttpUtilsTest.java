package com.fizzed.crux.okhttp;

import okhttp3.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OkHttpUtilsTest {
 
    @Test
    public void hasContentType() {
        assertThat(OkHttpUtils.hasContentType("application/json", "application/json"), is(true));
        assertThat(OkHttpUtils.hasContentType("application/json", "APPLICATION/JSON"), is(true));
        assertThat(OkHttpUtils.hasContentType("application/json; charset=utf-8", "APPLICATION/JSON"), is(true));
        assertThat(OkHttpUtils.hasContentType("application/json; charset=utf-8", "APPLICATION/JSON; charset=utf-8"), is(true));
    }
    
    @Test
    public void hasStatusCode() {
        Response response = mock(Response.class);
        
        when(response.code()).thenReturn(200);
        assertThat(OkHttpUtils.hasStatusCode(response, 200), is(true));
        assertThat(OkHttpUtils.hasStatusCode(response, 201), is(false));
        assertThat(OkHttpUtils.hasStatusCode(response, 201, 200), is(true));
    }
    
    @Test
    public void hasStatusCodeRange() {
        Response response = mock(Response.class);
        
        when(response.code()).thenReturn(200);
        assertThat(OkHttpUtils.hasStatusCodeRange(response, 200, 300), is(true));
        assertThat(OkHttpUtils.hasStatusCodeRange(response, 201, 201), is(false));
        assertThat(OkHttpUtils.hasStatusCodeRange(response, 200, 202), is(true));
    }
    
}