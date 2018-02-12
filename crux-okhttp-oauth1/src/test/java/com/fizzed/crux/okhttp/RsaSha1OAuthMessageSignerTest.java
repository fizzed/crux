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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class RsaSha1OAuthMessageSignerTest {
    
    static public final String RSA_KEY1 = "-----BEGIN PRIVATE KEY-----\n" +
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALbW9cReSoSvG4/R\n" +
        "CmmGTRsjdMZMBHZExUHnn98zjgi+q8xnavFgkYnxZLUwIywueNiIvdeYOiCQCEWq\n" +
        "JUeE2fGSAnViMDf7FuQ5ExRhfJjBH7T9DdXIa5CJz62u3sCvXacvRGByVLKUoD9s\n" +
        "sXfZSLGW6lwOUTwD3D3Hjvy0iZ5BAgMBAAECgYEAnyrqsJPVE0TNHZnBErQTk7h+\n" +
        "A9JRYBPmFMVq64eZ8grqy/qLoq2KhSdboE/Tjra2BOqlOXYPxCEe0maYJXaFssl3\n" +
        "8Ycx2+tfRHh1yD3Sk8Eet/6p6TJ8aULd75p8K0pmzcKGp9p60yU5TmqcN6u+SC01\n" +
        "oyrD/zmGqi5blNUJyjECQQDiHXdvg+OUFjrNjikOGItW+zVbLoT5RE/OmPjX8Uxl\n" +
        "wRZpBErRgRjgapT3gQeooVGzgSjPF41cM/fApxnzs2FLAkEAzwFIi8JNfJSTj1Wa\n" +
        "jyD9VYbMNmfnl9Qm+nfLb1Rhktmkx/Qdl5/5Hj9gq/TSJ0IJmBKh+D4bSmNjbA5M\n" +
        "nGhTIwJAWGW2rKT+EoXCRVwfVsBnsQCScmw0VhLLtHP/TMSr3lfnP/UKnu7+X7Wr\n" +
        "OTt7WASBIVEMODH39KToZauuLKTjEwJAdW/I9GDbDN5NiHeMI78XebnriMIRxTT6\n" +
        "mAX2R8abRHBs53M6hpafAeX6thNCVjtErh2D/g1d9inEcbz7AZC9+QJAJzensEWz\n" +
        "2SiNqyzIF0BWgtc3vpTjLwwRHBXkmpt8tCm/5apgtEhr/aQx1wYnFJp+z96WfErP\n" +
        "LFKYVWLsJFJn0A==\n" +
        "-----END PRIVATE KEY-----";

    @Test
    public void sign() throws Exception {
        RsaSha1OAuthMessageSigner signer = new RsaSha1OAuthMessageSigner();
        
        signer.setConsumerSecret(RSA_KEY1);
        
        String signature = signer.sign("data to sign");
        
        assertThat(signature, is("hPmtKJsHoGEj8GQhVwRNd9oyenSkCehU/RkCzz1DNs5RO9YabWNZQa/7dT/1KwzbMffRXUlp981uOfnk0/eQhEsJKitt1h9G+2n6ByrKE1GBbsHpX3rRzex/cmmueVTuom0bBJ4gjBwz15TF6f99qQqBOA/HtYsBoQsejpY94gA="));
    }
    
}