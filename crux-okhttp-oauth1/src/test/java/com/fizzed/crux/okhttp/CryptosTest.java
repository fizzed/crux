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

import java.security.PrivateKey;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class CryptosTest {
    
    @Test
    public void loadPkcs8PrivateKey() throws Exception {
        // pkcs#8 format
        PrivateKey privateKey1 = Cryptos.loadRSAPrivateKey(
            "-----BEGIN PRIVATE KEY-----\n" +
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4XqoFGsUsTCNri\n" +
            "wcRb6RuxsJjdPA/iq7XIx04M6j1ZgaPlF78nZgtSct1KhZNVcmYrJiUEl4/aRHxp\n" +
            "8ftPgRA6doa0Vbiv7mEObklrwKyU12JMr/eZ+2rZE7v9C3LsogKAaTWaPFgzymWt\n" +
            "jULSugFvqO6setVgzZOiWPZ+QGBvAgMBAAECgYBqP+Iepfu84aPGbI4vicvunc/+\n" +
            "cVH78w9U6Sd1WcSni9rpV8iBIK1p0KjBRcEJi+4Iaz/D2yA32KEFBVaqyFJzXxL7\n" +
            "ehX+0DJ9JwnUwOYYmbvjAC0uDBh6/J3++2N72ca9G3UbqwfmUOqzhK5s77uUBHjS\n" +
            "N2fyjkOQU+WaMoulqQJBAPE5AwbhdQYVMDW+SYOFk3FXcb0KsG0SAkcZ1ASf0zq+\n" +
            "4aEx8Eyi+/hcKrDx40/LqDxZ3NkGprj4FGzc0Rupd/0CQQDJvM0Zd4dYzJ9x0K/V\n" +
            "Xp/1DpfaqspdIvH3OjYQw/cZhG0t5GmtirPmDPBYfJV7wdNhziVUYrtgfIG4u00v\n" +
            "KxfbAkEAiOdtg1sz+obN/MKJoH6QJtSVNdA6PzzoVAghn5pB3OZ20fCwzB34WYWd\n" +
            "qR0vgJs6WT47LGUs/G+3z+0nNMbi1QJAJgG+9kxRoIY0h+HULrO8GRQdweGpbHCX\n" +
            "+4bpBrGUzSbo1tuQmVRnXjET2ufl1cIHjAale8d6G8x5OA95lChfPwJAXPfwUzR5\n" +
            "xbmGWBUDMobJ2WgGbjdXPNu+isWBg35cXBYombzrA92Q9OO50JH2FOgwKa7BGW/H\n" +
            "Xs4TKWVySp/V6w==\n" +
            "-----END PRIVATE KEY-----");
        
        assertThat(privateKey1.getAlgorithm(), is("RSA"));
        assertThat(privateKey1.getFormat(), is("PKCS#8"));
        
        // same key, no header though
        PrivateKey privateKey2 = Cryptos.loadRSAPrivateKey(
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4XqoFGsUsTCNri\n" +
            "wcRb6RuxsJjdPA/iq7XIx04M6j1ZgaPlF78nZgtSct1KhZNVcmYrJiUEl4/aRHxp\n" +
            "8ftPgRA6doa0Vbiv7mEObklrwKyU12JMr/eZ+2rZE7v9C3LsogKAaTWaPFgzymWt\n" +
            "jULSugFvqO6setVgzZOiWPZ+QGBvAgMBAAECgYBqP+Iepfu84aPGbI4vicvunc/+\n" +
            "cVH78w9U6Sd1WcSni9rpV8iBIK1p0KjBRcEJi+4Iaz/D2yA32KEFBVaqyFJzXxL7\n" +
            "ehX+0DJ9JwnUwOYYmbvjAC0uDBh6/J3++2N72ca9G3UbqwfmUOqzhK5s77uUBHjS\n" +
            "N2fyjkOQU+WaMoulqQJBAPE5AwbhdQYVMDW+SYOFk3FXcb0KsG0SAkcZ1ASf0zq+\n" +
            "4aEx8Eyi+/hcKrDx40/LqDxZ3NkGprj4FGzc0Rupd/0CQQDJvM0Zd4dYzJ9x0K/V\n" +
            "Xp/1DpfaqspdIvH3OjYQw/cZhG0t5GmtirPmDPBYfJV7wdNhziVUYrtgfIG4u00v\n" +
            "KxfbAkEAiOdtg1sz+obN/MKJoH6QJtSVNdA6PzzoVAghn5pB3OZ20fCwzB34WYWd\n" +
            "qR0vgJs6WT47LGUs/G+3z+0nNMbi1QJAJgG+9kxRoIY0h+HULrO8GRQdweGpbHCX\n" +
            "+4bpBrGUzSbo1tuQmVRnXjET2ufl1cIHjAale8d6G8x5OA95lChfPwJAXPfwUzR5\n" +
            "xbmGWBUDMobJ2WgGbjdXPNu+isWBg35cXBYombzrA92Q9OO50JH2FOgwKa7BGW/H\n" +
            "Xs4TKWVySp/V6w==");
        
        assertThat(privateKey2.getAlgorithm(), is("RSA"));
        assertThat(privateKey2.getFormat(), is("PKCS#8"));
        
        assertThat(privateKey2.getEncoded(), is(privateKey1.getEncoded()));
        
        // same key all on one line
        PrivateKey privateKey3 = Cryptos.loadRSAPrivateKey(
            "-----BEGIN PRIVATE KEY-----\nMIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4XqoFGsUsTCNriwcRb6RuxsJjdPA/iq7XIx04M6j1ZgaPlF78nZgtSct1KhZNVcmYrJiUEl4/aRHxp8ftPgRA6doa0Vbiv7mEObklrwKyU12JMr/eZ+2rZE7v9C3LsogKAaTWaPFgzymWtjULSugFvqO6setVgzZOiWPZ+QGBvAgMBAAECgYBqP+Iepfu84aPGbI4vicvunc/+cVH78w9U6Sd1WcSni9rpV8iBIK1p0KjBRcEJi+4Iaz/D2yA32KEFBVaqyFJzXxL7ehX+0DJ9JwnUwOYYmbvjAC0uDBh6/J3++2N72ca9G3UbqwfmUOqzhK5s77uUBHjSN2fyjkOQU+WaMoulqQJBAPE5AwbhdQYVMDW+SYOFk3FXcb0KsG0SAkcZ1ASf0zq+4aEx8Eyi+/hcKrDx40/LqDxZ3NkGprj4FGzc0Rupd/0CQQDJvM0Zd4dYzJ9x0K/VXp/1DpfaqspdIvH3OjYQw/cZhG0t5GmtirPmDPBYfJV7wdNhziVUYrtgfIG4u00vKxfbAkEAiOdtg1sz+obN/MKJoH6QJtSVNdA6PzzoVAghn5pB3OZ20fCwzB34WYWdqR0vgJs6WT47LGUs/G+3z+0nNMbi1QJAJgG+9kxRoIY0h+HULrO8GRQdweGpbHCX+4bpBrGUzSbo1tuQmVRnXjET2ufl1cIHjAale8d6G8x5OA95lChfPwJAXPfwUzR5xbmGWBUDMobJ2WgGbjdXPNu+isWBg35cXBYombzrA92Q9OO50JH2FOgwKa7BGW/HXs4TKWVySp/V6w==\n-----END PRIVATE KEY-----");
        
        assertThat(privateKey3.getAlgorithm(), is("RSA"));
        assertThat(privateKey3.getFormat(), is("PKCS#8"));
        
        assertThat(privateKey3.getEncoded(), is(privateKey1.getEncoded()));
    }
    
    @Test
    public void loadPkcs1PrivateKey() throws Exception {
        // if bouncy castle used then pkcs#1 formats just work too
        Security.addProvider(new BouncyCastleProvider());
        
        // pkcs#1 format
        PrivateKey privateKey1 = Cryptos.loadRSAPrivateKey(
            //"-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXQIBAAKBgQDJVO5bpfuYVIMRuF4zUyOhGYrQj8ujw/l1620HRttcKmoqVXB9\n" +
            "wfmaTP9+Qwuc5DjvHxi0QIODr9vUe3e6lCG2HYtw3Rc1o7cuYELiPv6f3grBkDlf\n" +
            "F8XLwyckOHw0AK1ko7yRL9XHQpztax+w7v2pDaZBkIgFv/IE5KmqWRHirwIDAQAB\n" +
            "AoGAZ7VvPxiGPJa0GYmuRKP8A31jy0F3Nbm6o+qM9CWk05opq/rtAmrMR5aIOaMR\n" +
            "tVS+mzaahCeDV29cPt0G9L5fikT0n45IF59d52s11Vl50DXvTLAVhf8m2L98W6Po\n" +
            "/6Gv+IWozf/H+aW3RNKvKvKzdI0ZTOkIAnwk6ivdA8CPAJECQQDwKlHivhEsanBy\n" +
            "DVwMVuOCmdVZjaJjDi8TiV56uHmgBbC7cm9fr5K6I6GIA6AZ82mRRWzvLdDKa4QG\n" +
            "xwkCKuwXAkEA1psmrPEj3QvyBqXPur3OHm5BnLslP/fRx+GxoPGWPwTp1YTOPA7O\n" +
            "0o3rJiropmaIfgpC2VrJb632uddRrntlKQJBANN9cP6rQZRLZxoIibaUDWVE8owa\n" +
            "HZX9N5eMsJaBPRmd6TQoYctxYVRpbCXKi4JOx/gVmwhE4EhgxbLyMwyln6ECQQCo\n" +
            "fXuqAFFF9WD58yja1raDkdEVuqEOTgV4RyxszgBGThdAulopMP8UtLPQbZPnQU0c\n" +
            "l/XQLMVGV3EEi5bnxJ2xAkBb/JHpokPgPHPCvJZHwI7pWGVckHmGxO096tGrD5Hi\n" +
            "hdxAtwsxkXXm8biFhbXx62sbdrS2yp5CCP9YEjv+ohX2"
            //"-----END RSA PRIVATE KEY-----"
            );
        
        assertThat(privateKey1.getAlgorithm(), is("RSA"));
        assertThat(privateKey1.getFormat(), is("PKCS#8"));
        
        // pkcs#1 format
        PrivateKey privateKey2 = Cryptos.loadRSAPrivateKey(
            "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXQIBAAKBgQDJVO5bpfuYVIMRuF4zUyOhGYrQj8ujw/l1620HRttcKmoqVXB9\n" +
            "wfmaTP9+Qwuc5DjvHxi0QIODr9vUe3e6lCG2HYtw3Rc1o7cuYELiPv6f3grBkDlf\n" +
            "F8XLwyckOHw0AK1ko7yRL9XHQpztax+w7v2pDaZBkIgFv/IE5KmqWRHirwIDAQAB\n" +
            "AoGAZ7VvPxiGPJa0GYmuRKP8A31jy0F3Nbm6o+qM9CWk05opq/rtAmrMR5aIOaMR\n" +
            "tVS+mzaahCeDV29cPt0G9L5fikT0n45IF59d52s11Vl50DXvTLAVhf8m2L98W6Po\n" +
            "/6Gv+IWozf/H+aW3RNKvKvKzdI0ZTOkIAnwk6ivdA8CPAJECQQDwKlHivhEsanBy\n" +
            "DVwMVuOCmdVZjaJjDi8TiV56uHmgBbC7cm9fr5K6I6GIA6AZ82mRRWzvLdDKa4QG\n" +
            "xwkCKuwXAkEA1psmrPEj3QvyBqXPur3OHm5BnLslP/fRx+GxoPGWPwTp1YTOPA7O\n" +
            "0o3rJiropmaIfgpC2VrJb632uddRrntlKQJBANN9cP6rQZRLZxoIibaUDWVE8owa\n" +
            "HZX9N5eMsJaBPRmd6TQoYctxYVRpbCXKi4JOx/gVmwhE4EhgxbLyMwyln6ECQQCo\n" +
            "fXuqAFFF9WD58yja1raDkdEVuqEOTgV4RyxszgBGThdAulopMP8UtLPQbZPnQU0c\n" +
            "l/XQLMVGV3EEi5bnxJ2xAkBb/JHpokPgPHPCvJZHwI7pWGVckHmGxO096tGrD5Hi\n" +
            "hdxAtwsxkXXm8biFhbXx62sbdrS2yp5CCP9YEjv+ohX2\n" +
            "-----END RSA PRIVATE KEY-----"
            );
        
        assertThat(privateKey2.getAlgorithm(), is("RSA"));
        assertThat(privateKey2.getFormat(), is("PKCS#8"));
        
        assertThat(privateKey2.getEncoded(), is(privateKey1.getEncoded()));
    }
    
}