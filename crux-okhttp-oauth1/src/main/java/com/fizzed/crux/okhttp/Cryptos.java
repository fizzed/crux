/*
 * Copyright 2018 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.okhttp;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 *  openssl genrsa -out privatekey.pem 1024
 *  openssl pkcs8 -topk8 -inform PEM -in privatekey.pem -out privatekey.pem.pkcs8 -nocrypt
 * 
 *  OR
 * 
 *  openssl genrsa 1024 | openssl pkcs8 -topk8 -inform PEM -nocrypt
 * 
 * @author jjlauer
 */
public class Cryptos {
 
    static private final String KEY_RSA = "RSA";
    static private final String BEGIN_PKCS8 = "-----BEGIN PRIVATE KEY-----";
    static private final String END_PKCS8 = "-----END PRIVATE KEY-----";
    static private final String BEGIN_PKCS1 = "-----BEGIN RSA PRIVATE KEY-----";
    static private final String END_PKCS1 = "-----END RSA PRIVATE KEY-----";
    
    static public PrivateKey loadRSAPrivateKey(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // chop off pkcs#8 begin/end header (if it exists)
        if (content.startsWith(BEGIN_PKCS8)) {
            if (!content.endsWith(END_PKCS8)) {
                throw new IllegalArgumentException("Key content did not include " + END_PKCS8);
            }
            content = content.replace(BEGIN_PKCS8, "");
            content = content.replace(END_PKCS8, "");
            content = content.trim();
        }
        
        // chop off pkcs#1 begin/end header (if it exists)
        if (content.startsWith(BEGIN_PKCS1)) {
            if (!content.endsWith(END_PKCS1)) {
                throw new IllegalArgumentException("Key content did not include " + END_PKCS1);
            }
            content = content.replace(BEGIN_PKCS1, "");
            content = content.replace(END_PKCS1, "");
            content = content.trim();
        }
        
        byte[] bytes = Base64.getMimeDecoder().decode(content);
        
        final KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        
        final EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);

        return keyFactory.generatePrivate(keySpec);
    }
    
}