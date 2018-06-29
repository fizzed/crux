package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpOAuth2Interceptor implements Interceptor {

    private final String authorizationHeader;
    
    public OkHttpOAuth2Interceptor(String accessToken) {
        this(null, accessToken);
    }
    
    public OkHttpOAuth2Interceptor(String tokenType, String accessToken) {
        Objects.requireNonNull(accessToken, "accessToken was null");
        this.authorizationHeader = (tokenType == null ? "Bearer" : tokenType)
            + " " + accessToken;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!chain.request().headers().names().contains("Authorization")) {
            // modify request and set authorization header
            Request request = chain.request().newBuilder()
                .header("Authorization", authorizationHeader)
                .build();
            return chain.proceed(request);
        } else {
            return chain.proceed(chain.request());
        }
    }

}
