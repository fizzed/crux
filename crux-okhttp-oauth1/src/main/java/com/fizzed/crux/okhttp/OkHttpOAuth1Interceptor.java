package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class OkHttpOAuth1Interceptor implements Interceptor {

    private final OkHttpOAuthConsumer oauthConsumer;
    private final SigningInterceptor signingInterceptor;
    
    public OkHttpOAuth1Interceptor(
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String accessSecret) {
        Objects.requireNonNull(consumerKey, "consumerKey was null");
        Objects.requireNonNull(consumerSecret, "consumerSecret was null");
        Objects.requireNonNull(accessToken, "accessToken was null");
        Objects.requireNonNull(accessSecret, "accessSecret was null");
        this.oauthConsumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        this.oauthConsumer.setTokenWithSecret(accessToken, accessSecret);
        this.signingInterceptor = new SigningInterceptor(this.oauthConsumer);
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        return this.signingInterceptor.intercept(chain);
    }
    
}
