package com.fizzed.crux.okhttp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Signature;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SignatureBaseString;

/**
 * Jersey impl: https://github.com/jersey/jersey/blob/master/security/oauth1-signature/src/main/java/org/glassfish/jersey/oauth1/signature/RsaSha1Method.java
 * 
 * @author Joe Lauer
 */
@SuppressWarnings("serial")
public class OAuthRsaSha1MessageSigner extends OAuthMessageSigner {

	@Override
    public String getSignatureMethod() {
        return "RSA-SHA1";
    }

    @Override
    public String sign(HttpRequest request, HttpParameters requestParams)
            throws OAuthMessageSignerException {
        try {
            String sbs = new SignatureBaseString(request, requestParams).generate();
            OAuth.debugOut("SBS", sbs);
            byte[] text = sbs.getBytes(OAuth.ENCODING);

            Signature privateSignature = Signature.getInstance("SHA1withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(text);

            byte[] signature = privateSignature.sign();
            
            return base64Encode(signature).trim();
            
            
//            String keyString = OAuth.percentEncode(getConsumerSecret()) + '&'
//                    + OAuth.percentEncode(getTokenSecret());
//            byte[] keyBytes = keyString.getBytes(OAuth.ENCODING);
//
//            SecretKey key = new SecretKeySpec(keyBytes, MAC_NAME);
//            Mac mac = Mac.getInstance(MAC_NAME);
//            mac.init(key);
//
//            String sbs = new SignatureBaseString(request, requestParams).generate();
//            OAuth.debugOut("SBS", sbs);
//            byte[] text = sbs.getBytes(OAuth.ENCODING);
//
//            return base64Encode(mac.doFinal(text)).trim();
        } catch (GeneralSecurityException e) {
            throw new OAuthMessageSignerException(e);
        } catch (UnsupportedEncodingException e) {
            throw new OAuthMessageSignerException(e);
        }
    }
}