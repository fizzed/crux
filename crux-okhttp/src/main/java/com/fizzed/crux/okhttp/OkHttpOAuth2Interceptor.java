package com.fizzed.crux.okhttp;

import static com.fizzed.crux.util.Maybe.maybe;
import java.io.IOException;
import java.util.Objects;

public class OkHttpOAuth2Interceptor extends OkHttpAuthorizationInterceptor {

    protected final IOSupplier<AccessTokenCredentials> supplier;
    
    public OkHttpOAuth2Interceptor(String accessToken) {
        this(null, accessToken);
    }
    
    public OkHttpOAuth2Interceptor(String tokenType, String accessToken) {
        this(new AccessTokenCredentials(accessToken, tokenType));
    }
    
    public OkHttpOAuth2Interceptor(AccessTokenCredentials credentials) {
        this(() -> credentials);
        Objects.requireNonNull(credentials, "credentials was null");
    }
    
    public OkHttpOAuth2Interceptor(IOSupplier<AccessTokenCredentials> supplier) {
        Objects.requireNonNull(supplier, "supplier was null");
        this.supplier = supplier;
    }
    
    @Override
    public String buildAuthorizationHeader() throws IOException {
        final AccessTokenCredentials creds = this.supplier.get();
        
        final String tt = maybe(creds.getTokenType())
            .orElse("Bearer");
        
        final String at = creds.getAccessToken();
        
        int size = tt.length() + 1 + at.length();
        
        return new StringBuilder(size)
            .append(tt)
            .append(" ")
            .append(at)
            .toString();
    }
    
}