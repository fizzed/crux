package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUserAgentInterceptor implements Interceptor {

    protected final IOSupplier<String> supplier;
    
    public OkHttpUserAgentInterceptor(String userAgent) {
        this(() -> userAgent);
    }
    
    public OkHttpUserAgentInterceptor(IOSupplier<String> supplier) {
        Objects.requireNonNull(supplier, "supplier was null");
        this.supplier = supplier;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String v = this.supplier.get();
        if (v != null) {
            Request newRequest = chain.request().newBuilder()
                .header("User-Agent", v)
                .build();
            
            return chain.proceed(newRequest);
        }
        
        // otherwise, fallback to standard processing
        return chain.proceed(chain.request());
    }
    
}