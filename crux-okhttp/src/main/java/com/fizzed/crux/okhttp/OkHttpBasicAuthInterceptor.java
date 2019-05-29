package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Credentials;

public class OkHttpBasicAuthInterceptor extends OkHttpAuthorizationInterceptor {

    protected final IOSupplier<BasicAuthCredentials> supplier;
    
    public OkHttpBasicAuthInterceptor(String username, String password) {
        this(new BasicAuthCredentials(username, password));
    }
    
    public OkHttpBasicAuthInterceptor(BasicAuthCredentials credentials) {
        this(() -> credentials);
        Objects.requireNonNull(credentials, "credentials was null");
    }
    
    public OkHttpBasicAuthInterceptor(IOSupplier<BasicAuthCredentials> supplier) {
        Objects.requireNonNull(supplier, "supplier was null");
        this.supplier = supplier;
    }
    
    @Override
    public String buildAuthorizationHeader() throws IOException {
        BasicAuthCredentials creds = this.supplier.get();
        
        return Credentials.basic(creds.getUsername(), creds.getPassword());
    }
    
}