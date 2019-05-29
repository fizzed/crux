package com.fizzed.crux.okhttp;

import java.io.IOException;
import java.util.Objects;
import oauth.signpost.signature.HmacSha1MessageSigner;
import okhttp3.Interceptor;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class OkHttpOAuth1Interceptor implements Interceptor {

    protected final IOSupplier<OAuth1Credentials> supplier;
    // used for caching...
    protected OAuth1Credentials lastCredentials;
    protected OkHttpOAuthConsumer oauthConsumer;
    protected SigningInterceptor signingInterceptor;
    
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
        
        this(new OAuth1Credentials(consumerKey, consumerSecret, accessToken, accessSecret, signatureMethod));
        Objects.requireNonNull(consumerKey, "consumerKey was null");
        Objects.requireNonNull(consumerSecret, "consumerSecret was null");
        Objects.requireNonNull(accessToken, "accessToken was null");
        Objects.requireNonNull(accessSecret, "accessSecret was null");
    }
    
    public OkHttpOAuth1Interceptor(
            OAuth1Credentials credentials) {
        
        this(() -> credentials);
        Objects.requireNonNull(credentials, "credentials was null");
    }
    
    public OkHttpOAuth1Interceptor(
            IOSupplier<OAuth1Credentials> supplier) {
        
        Objects.requireNonNull(supplier, "supplier was null");
        this.supplier = supplier;
    }
    
    public SigningInterceptor buildSigningInterceptor() throws IOException {
        final OAuth1Credentials credentials = this.supplier.get();
        
        // expensive to build the consumer and signer -- if the creds haven't
        // changed then we can simply use the previous version we built...
        if (!Objects.equals(credentials, this.lastCredentials)) {
            // build consumer
            this.oauthConsumer = new OkHttpOAuthConsumer(
                credentials.getConsumerKey(), credentials.getConsumerSecret());
        
            if (credentials.getSignatureMethod() != null) {
                switch (credentials.getSignatureMethod().toLowerCase()) {
                    case "rsa-sha1":
                        this.oauthConsumer.setMessageSigner(new RsaSha1OAuthMessageSigner());
                        break;
                    case "hmac-sha1":
                        this.oauthConsumer.setMessageSigner(new HmacSha1MessageSigner());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported signature method " + credentials.getSignatureMethod());
                }
            }

            this.oauthConsumer.setTokenWithSecret(
                credentials.getAccessToken(), credentials.getAccessSecret());
            
            this.signingInterceptor = new SigningInterceptor(this.oauthConsumer);
        }
        
        return this.signingInterceptor;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        final SigningInterceptor si = this.buildSigningInterceptor();
        
        return si.intercept(chain);
    }

}