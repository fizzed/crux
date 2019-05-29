package com.fizzed.crux.okhttp;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OkHttpAuthorizationInterceptor implements Interceptor {
    
    protected boolean alwaysOverride;

    public boolean isAlwaysOverride() {
        return alwaysOverride;
    }

    public void setAlwaysOverride(boolean alwaysOverride) {
        this.alwaysOverride = alwaysOverride;
    }

    abstract public String buildAuthorizationHeader() throws IOException;
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (this.isAlwaysOverride() ||
                !chain.request().headers().names().contains("Authorization")) {
            
            final String authorizationHeader = this.buildAuthorizationHeader();
            
            // modify request and set authorization header (if not yet set)
            Request request = chain.request().newBuilder()
                .header("Authorization", authorizationHeader)
                .build();
            
            return chain.proceed(request);
        } else {
            return chain.proceed(chain.request());
        }
    }

}