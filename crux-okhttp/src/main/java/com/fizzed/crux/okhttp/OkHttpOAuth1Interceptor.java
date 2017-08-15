package com.fizzed.crux.okhttp;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class OkHttpOAuth1Interceptor implements Interceptor {

    private final OkHttpOAuthConsumer oauthConsumer;
    private final SigningInterceptor signingInterceptor;
    
    public OkHttpOAuth1Interceptor(String consumerKey, String consumerSecret,
                                    String accessToken, String accessSecret) {
        this.oauthConsumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        this.oauthConsumer.setTokenWithSecret(accessToken, accessSecret);
        this.signingInterceptor = new SigningInterceptor(this.oauthConsumer);
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        return this.signingInterceptor.intercept(chain);
    }
    
}
