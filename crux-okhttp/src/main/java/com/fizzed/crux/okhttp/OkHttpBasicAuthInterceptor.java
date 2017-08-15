package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpBasicAuthInterceptor implements Interceptor {

    private final String authorizationHeader;
    
    public OkHttpBasicAuthInterceptor(String username, String password) {
        Objects.requireNonNull(username, "username was null");
        Objects.requireNonNull(password, "password was null");
        this.authorizationHeader = Credentials.basic(username, password);
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        // modify request and set authorization header
        Request request = chain.request().newBuilder()
            .header("Authorization", this.authorizationHeader)
            .build();
        return chain.proceed(request);
    }

}
