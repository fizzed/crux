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
package com.fizzed.crux.util;

import java.io.UnsupportedEncodingException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class Base32Test {
 
    @Test
    public void encode() throws UnsupportedEncodingException {
        assertThat(Base32.encode(null), is(nullValue()));
        assertThat(Base32.encode(new byte[0]), is(""));
        assertThat(Base32.encode(new byte[1]), is("AA======"));
        assertThat(Base32.encode(new byte[1], false), is("AA"));
        assertThat(Base32.encode(new byte[2]), is("AAAA===="));
        assertThat(Base32.encode(new byte[2], false), is("AAAA"));
        assertThat(Base32.encode(new byte[3]), is("AAAAA==="));
        assertThat(Base32.encode(new byte[3], false), is("AAAAA"));
        assertThat(Base32.encode(new byte[4]), is("AAAAAAA="));
        assertThat(Base32.encode(new byte[4], false), is("AAAAAAA"));
        assertThat(Base32.encode(new byte[5]), is("AAAAAAAA"));
        assertThat(Base32.encode(new byte[5], false), is("AAAAAAAA"));
        assertThat(Base32.encode(new byte[6]), is("AAAAAAAAAA======"));
        assertThat(Base32.encode(new byte[6], false), is("AAAAAAAAAA"));
        assertThat(Base32.encode("test".getBytes("UTF-8")), is("ORSXG5A="));
        assertThat(Base32.encode("test1".getBytes("UTF-8")), is("ORSXG5BR"));
        assertThat(Base32.encode(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }), is("AERUKZ4JVPG66==="));
        assertThat(Base32.encode("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".getBytes("UTF-8")), is("JRXXEZLNEBUXA43VNUQGI33MN5ZCA43JOQQGC3LFOQWCAY3PNZZWKY3UMV2HK4RAMFSGS4DJONRWS3THEBSWY2LUFQQHGZLEEBSG6IDFNF2XG3LPMQQHIZLNOBXXEIDJNZRWSZDJMR2W45BAOV2CA3DBMJXXEZJAMV2CAZDPNRXXEZJANVQWO3TBEBQWY2LROVQS4ICVOQQGK3TJNUQGCZBANVUW42LNEB3GK3TJMFWSYIDROVUXGIDON5ZXI4TVMQQGK6DFOJRWS5DBORUW63RAOVWGYYLNMNXSA3DBMJXXE2LTEBXGS43JEB2XIIDBNRUXC5LJOAQGK6BAMVQSAY3PNVWW6ZDPEBRW63TTMVYXKYLUFYQEI5LJOMQGC5LUMUQGS4TVOJSSAZDPNRXXEIDJNYQHEZLQOJSWQZLOMRSXE2LUEBUW4IDWN5WHK4DUMF2GKIDWMVWGS5BAMVZXGZJAMNUWY3DVNUQGI33MN5ZGKIDFOUQGM5LHNFQXIIDOOVWGYYJAOBQXE2LBOR2XELRAIV4GGZLQORSXK4RAONUW45BAN5RWGYLFMNQXIIDDOVYGSZDBORQXIIDON5XCA4DSN5UWIZLOOQWCA43VNZ2CA2LOEBRXK3DQMEQHC5LJEBXWMZTJMNUWCIDEMVZWK4TVNZ2CA3LPNRWGS5BAMFXGS3JANFSCAZLTOQQGYYLCN5ZHK3JO"));
    }
    
    @Test
    public void decode() throws UnsupportedEncodingException {
        assertThat(Base32.decode(null), is(nullValue()));
        assertThat(Base32.decode(""), is(new byte[0]));
        assertThat(Base32.decode("AA======"), is(new byte[1]));
        assertThat(Base32.decode("AA"), is(new byte[1]));
        assertThat(Base32.decode("AA="), is(new byte[1]));
        assertThat(Base32.decode("AA=="), is(new byte[1]));
        assertThat(Base32.decode("AA==="), is(new byte[1]));
        assertThat(Base32.decode("AA===="), is(new byte[1]));
        assertThat(Base32.decode("AA====="), is(new byte[1]));
        assertThat(Base32.decode("AAAA===="), is(new byte[2]));
        assertThat(Base32.decode("AAAA==="), is(new byte[2]));
        assertThat(Base32.decode("AAAA=="), is(new byte[2]));
        assertThat(Base32.decode("AAAA="), is(new byte[2]));
        assertThat(Base32.decode("AAAA"), is(new byte[2]));
        assertThat(Base32.decode("AAAAA==="), is(new byte[3]));
        assertThat(Base32.decode("AAAAA=="), is(new byte[3]));
        assertThat(Base32.decode("AAAAA="), is(new byte[3]));
        assertThat(Base32.decode("AAAAA"), is(new byte[3]));
        assertThat(Base32.decode("AAAAAAA="), is(new byte[4]));
        assertThat(Base32.decode("AAAAAAA"), is(new byte[4]));
        assertThat(Base32.decode("AAAAAAAA"), is(new byte[5]));
        assertThat(Base32.decode("ORSXG5A="), is("test".getBytes("UTF-8")));
        assertThat(Base32.decode("ORSXG5A"), is("test".getBytes("UTF-8")));
        assertThat(Base32.decode("ORSXG5BR"), is("test1".getBytes("UTF-8")));
        assertThat(Base32.decode("AERUKZ4JVPG66==="), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
        assertThat(Base32.decode("AERUKZ4JVPG66=="), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
        assertThat(Base32.decode("AERUKZ4JVPG66="), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
        assertThat(Base32.decode("AERUKZ4JVPG66"), is(new byte[] { (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF }));
        assertThat(Base32.decode("JRXXEZLNEBUXA43VNUQGI33MN5ZCA43JOQQGC3LFOQWCAY3PNZZWKY3UMV2HK4RAMFSGS4DJONRWS3THEBSWY2LUFQQHGZLEEBSG6IDFNF2XG3LPMQQHIZLNOBXXEIDJNZRWSZDJMR2W45BAOV2CA3DBMJXXEZJAMV2CAZDPNRXXEZJANVQWO3TBEBQWY2LROVQS4ICVOQQGK3TJNUQGCZBANVUW42LNEB3GK3TJMFWSYIDROVUXGIDON5ZXI4TVMQQGK6DFOJRWS5DBORUW63RAOVWGYYLNMNXSA3DBMJXXE2LTEBXGS43JEB2XIIDBNRUXC5LJOAQGK6BAMVQSAY3PNVWW6ZDPEBRW63TTMVYXKYLUFYQEI5LJOMQGC5LUMUQGS4TVOJSSAZDPNRXXEIDJNYQHEZLQOJSWQZLOMRSXE2LUEBUW4IDWN5WHK4DUMF2GKIDWMVWGS5BAMVZXGZJAMNUWY3DVNUQGI33MN5ZGKIDFOUQGM5LHNFQXIIDOOVWGYYJAOBQXE2LBOR2XELRAIV4GGZLQORSXK4RAONUW45BAN5RWGYLFMNQXIIDDOVYGSZDBORQXIIDON5XCA4DSN5UWIZLOOQWCA43VNZ2CA2LOEBRXK3DQMEQHC5LJEBXWMZTJMNUWCIDEMVZWK4TVNZ2CA3LPNRWGS5BAMFXGS3JANFSCAZLTOQQGYYLCN5ZHK3JO"), is("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".getBytes("UTF-8")));
        
        try {
            assertThat(Base32.decode("AAAAAAAA="), is(new byte[5]));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            assertThat(Base32.decode("AAAAAAAA=="), is(new byte[5]));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            assertThat(Base32.decode("AAAAAAAA==="), is(new byte[5]));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
}