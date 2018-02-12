package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import oauth.signpost.signature.HmacSha1MessageSigner;
import okhttp3.Interceptor;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class OkHttpOAuth1Interceptor implements Interceptor {

    private final OkHttpOAuthConsumer oauthConsumer;
    private final SigningInterceptor signingInterceptor;
    
    @Deprecated
    public OkHttpOAuth1Interceptor(
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String accessSecret) {
        this(consumerKey, consumerSecret, accessToken, accessSecret, null);
    }
    
    public OkHttpOAuth1Interceptor(
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String accessSecret,
            String signatureMethod) {
        Objects.requireNonNull(consumerKey, "consumerKey was null");
        Objects.requireNonNull(consumerSecret, "consumerSecret was null");
        Objects.requireNonNull(accessToken, "accessToken was null");
        Objects.requireNonNull(accessSecret, "accessSecret was null");
        this.oauthConsumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        
        if (signatureMethod != null) {
            if ("RSA-SHA1".equalsIgnoreCase(signatureMethod)) {
                this.oauthConsumer.setMessageSigner(new RsaSha1OAuthMessageSigner());
            } else if ("HMAC-SHA1".equalsIgnoreCase(signatureMethod)) {
                this.oauthConsumer.setMessageSigner(new HmacSha1MessageSigner());
            } else {
                throw new IllegalArgumentException("Unsupported signature method " + signatureMethod);
            }
        }
        
        this.oauthConsumer.setTokenWithSecret(accessToken, accessSecret);
        this.signingInterceptor = new SigningInterceptor(this.oauthConsumer);
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        return this.signingInterceptor.intercept(chain);
    }
    
}
