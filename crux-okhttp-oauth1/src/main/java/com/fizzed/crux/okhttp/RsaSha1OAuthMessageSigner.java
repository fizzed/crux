package com.fizzed.crux.okhttp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SignatureBaseString;

/**
 * Implements RSA-SHA1 message signature for OAuth v1.  Adapted primarily
 * from jersey's impl: https://github.com/jersey/jersey/blob/master/security/oauth1-signature/src/main/java/org/glassfish/jersey/oauth1/signature/RsaSha1Method.java
 * 
 * To generate a key that can be used by this class:
 * 
 *  openssl genrsa -out privatekey.pem 1024
 *  openssl req -new -x509 -key privatekey.pem -out publickey.cer -days 1825
 *  openssl pkcs8 -topk8 -inform PEM -in privatekey.pem -out privatekey.pem.pkcs8 -nocrypt
 * 
 * @author jjlauer
 */
@SuppressWarnings("serial")
public class RsaSha1OAuthMessageSigner extends OAuthMessageSigner {

    static public final String METHOD = "RSA-SHA1";
    static private final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    
	@Override
    public String getSignatureMethod() {
        return METHOD;
    }

    @Override
    public String sign(HttpRequest request, HttpParameters requestParams)
            throws OAuthMessageSignerException {
        
        final String data = new SignatureBaseString(request, requestParams).generate();
        
        return this.sign(data);
    }
    
    // package-level for testing
    String sign(String data) throws OAuthMessageSignerException {
        try {
            final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            
            PrivateKey privateKey = Cryptos.loadRSAPrivateKey(this.getConsumerSecret());
            
            final byte[] dataBytes = data.getBytes(OAuth.ENCODING);

            signature.initSign(privateKey);
            signature.update(dataBytes);

            byte[] signatureBytes = signature.sign();
            
            return base64Encode(signatureBytes).trim();
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new OAuthMessageSignerException(e);
        }
    }
    
}