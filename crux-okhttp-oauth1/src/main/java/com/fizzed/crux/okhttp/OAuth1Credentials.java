/*
 * Copyright 2019 Fizzed, Inc.
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

import java.util.Objects;

public class OAuth1Credentials {
 
    protected String consumerKey;
    protected String consumerSecret;
    protected String accessToken;
    protected String accessSecret;
    protected String signatureMethod;

    public OAuth1Credentials() {
    }
    
    public OAuth1Credentials(
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String accessSecret,
            String signatureMethod) {
        
        Objects.requireNonNull(consumerKey, "consumerKey was null");
        Objects.requireNonNull(consumerSecret, "consumerSecret was null");
        Objects.requireNonNull(accessToken, "accessToken was null");
        Objects.requireNonNull(accessSecret, "accessSecret was null");
        
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.signatureMethod = signatureMethod;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.consumerKey);
        hash = 53 * hash + Objects.hashCode(this.consumerSecret);
        hash = 53 * hash + Objects.hashCode(this.accessToken);
        hash = 53 * hash + Objects.hashCode(this.accessSecret);
        hash = 53 * hash + Objects.hashCode(this.signatureMethod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OAuth1Credentials other = (OAuth1Credentials) obj;
        if (!Objects.equals(this.consumerKey, other.consumerKey)) {
            return false;
        }
        if (!Objects.equals(this.consumerSecret, other.consumerSecret)) {
            return false;
        }
        if (!Objects.equals(this.accessToken, other.accessToken)) {
            return false;
        }
        if (!Objects.equals(this.accessSecret, other.accessSecret)) {
            return false;
        }
        if (!Objects.equals(this.signatureMethod, other.signatureMethod)) {
            return false;
        }
        return true;
    }

}