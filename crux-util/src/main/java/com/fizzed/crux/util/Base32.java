/*
 * Adapted from Amazon AWS Base32 codec for control over padding being added
 * by default and optionally included on decoding.  Their code was the same
 * License as this.
 *
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
 * 
 */
package com.fizzed.crux.util;

import java.nio.charset.StandardCharsets;

public class Base32 {
    
    static private final int MASK_2BITS = (1 << 2) - 1;
    static private final int MASK_3BITS = (1 << 3) - 1;
    static private final int MASK_4BITS = (1 << 4) - 1;
    static private final int MASK_5BITS = (1 << 5) - 1;
    // Base 32 alphabet as defined at http://www.ietf.org/rfc/rfc4648.txt
    static private final byte PAD = '=';
    
    //static private final byte[] ALPHABETS = upperAlphabets();
    
    //static private final char[] UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
    //static private final char[] LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz234567".toCharArray();

    static private byte[] internalEncode(byte[] encodeAlphabet, byte[] src, boolean padded) {
        final int num5bytes = src.length / 5;
        final int remainder = src.length % 5;
        
        if (remainder == 0)
        {
            byte[] dest = new byte[num5bytes * 8];
    
            for (int s=0,d=0; s < src.length; s+=5, d+=8)
                encode5bytes(encodeAlphabet, src, s, dest, d);
            
            return dest;
        }
        
        int length = (num5bytes+1) * 8;
        
        if (!padded) {
            switch (remainder) {
                case 1:
                    length -= 6;
                    break;
                case 2:
                    length -= 4;
                    break;
                case 3:
                    length -= 3;
                    break;
                case 4:
                    length -= 1;
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        
        byte[] dest = new byte[length];
        int s=0, d=0;
        
        for (; s < src.length-remainder; s+=5, d+=8)
            encode5bytes(encodeAlphabet, src, s, dest, d);
        
        switch(remainder) {
            case 1:
                encode1byte(encodeAlphabet, src, s, dest, d, padded);
                break;
            case 2:
                encode2bytes(encodeAlphabet, src, s, dest, d, padded);
                break;
            case 3:
                encode3bytes(encodeAlphabet, src, s, dest, d, padded);
                break;
            case 4:
                encode4bytes(encodeAlphabet, src, s, dest, d, padded);
                break;
            default:
                throw new IllegalStateException();
        }
        return dest;
    }
    
    static private void encode5bytes(byte[] encodeAlphabet, byte[] src, int s, byte[] dest, int d) {
        // operator precedence in descending order: >>> or <<, &, |
        byte p;
        dest[d++] = encodeAlphabet[(p=src[s++]) >>> 3 & MASK_5BITS];                         // 5 
        dest[d++] = encodeAlphabet[(p & MASK_3BITS) << 2 | (p=src[s++]) >>> 6 & MASK_2BITS]; // 3 2
        dest[d++] = encodeAlphabet[p >>> 1 & MASK_5BITS];                                    //   5
        dest[d++] = encodeAlphabet[(p & 1) << 4 | (p=src[s++]) >>> 4 & MASK_4BITS];          //   1 4
        dest[d++] = encodeAlphabet[(p & MASK_4BITS) << 1 | (p=src[s++]) >>> 7 & 1];          //     4 1
        dest[d++] = encodeAlphabet[p >>> 2 & MASK_5BITS];                                    //       5
        dest[d++] = encodeAlphabet[(p & MASK_2BITS) << 3 | (p=src[s]) >>> 5 & MASK_3BITS];   //       2 3
        dest[d] = encodeAlphabet[p & MASK_5BITS];                                            //         5
    }
    
    static private void encode4bytes(byte[] encodeAlphabet, byte[] src, int s, byte[] dest, int d, boolean padding) {
        // operator precedence in descending order: >>> or <<, &, |
        byte p;
        dest[d++] = encodeAlphabet[(p=src[s++]) >>> 3 & MASK_5BITS];                         // 5 
        dest[d++] = encodeAlphabet[(p & MASK_3BITS) << 2 | (p=src[s++]) >>> 6 & MASK_2BITS]; // 3 2
        dest[d++] = encodeAlphabet[p >>> 1 & MASK_5BITS];                                    //   5
        dest[d++] = encodeAlphabet[(p & 1) << 4 | (p=src[s++]) >>> 4 & MASK_4BITS];          //   1 4
        dest[d++] = encodeAlphabet[(p & MASK_4BITS) << 1 | (p=src[s]) >>> 7 & 1];            //     4 1
        dest[d++] = encodeAlphabet[p >>> 2 & MASK_5BITS];                                    //       5
        dest[d++] = encodeAlphabet[(p & MASK_2BITS) << 3];                                   //       2
        if (padding) {
            dest[d] = PAD;
        }
    }
    
    static private void encode3bytes(byte[] encodeAlphabet, byte[] src, int s, byte[] dest, int d, boolean padding) {
        // operator precedence in descending order: >>> or <<, &, |
        byte p;
        dest[d++] = encodeAlphabet[(p=src[s++]) >>> 3 & MASK_5BITS];                         // 5 
        dest[d++] = encodeAlphabet[(p & MASK_3BITS) << 2 | (p=src[s++]) >>> 6 & MASK_2BITS]; // 3 2
        dest[d++] = encodeAlphabet[p >>> 1 & MASK_5BITS];                                    //   5
        dest[d++] = encodeAlphabet[(p & 1) << 4 | (p=src[s]) >>> 4 & MASK_4BITS];            //   1 4
        dest[d++] = encodeAlphabet[(p & MASK_4BITS) << 1];                                   //     4
        
        if (padding) {
            for (int i=0; i < 3; i++)
                dest[d++] = PAD;                                                                       
        }
    }
    
    static private void encode2bytes(byte[] encodeAlphabet, byte[] src, int s, byte[] dest, int d, boolean padding) {
        // operator precedence in descending order: >>> or <<, &, |
        byte p;
        dest[d++] = encodeAlphabet[(p=src[s++]) >>> 3 & MASK_5BITS];                         // 5 
        dest[d++] = encodeAlphabet[(p & MASK_3BITS) << 2 | (p=src[s]) >>> 6 & MASK_2BITS];   // 3 2
        dest[d++] = encodeAlphabet[p >>> 1 & MASK_5BITS];                                    //   5
        dest[d++] = encodeAlphabet[(p & 1) << 4];                                            //   1
        
        if (padding) {
            for (int i=0; i < 4; i++)
                dest[d++] = PAD;                                                                       
        }
    }
    
    static private void encode1byte(byte[] encodeAlphabet, byte[] src, int s, byte[] dest, int d, boolean padding) {
        // operator precedence in descending order: >>> or <<, &, |
        byte p;
        dest[d++] = encodeAlphabet[(p=src[s]) >>> 3 & MASK_5BITS];                            // 5 
        dest[d++] = encodeAlphabet[(p & MASK_3BITS) << 2];                                    // 3
        
        if (padding) {
            for (int i=0; i < 6; i++)
                dest[d++] = PAD;
        }
    }
    
    static private void decode5bytes(byte[] src, int s, byte[] dest, int d) {
        int p=0;
        // operator precedence in descending order: >>> or <<, &, |
        dest[d++] = (byte)
                    (
                        pos(src[s++]) << 3
                        | (p=pos(src[s++])) >>> 2 & MASK_3BITS
                    )
                    ;                                               // 5 3
        dest[d++] = (byte)
                    (
                        (p & MASK_2BITS) << 6
                        | pos(src[s++]) << 1 
                        | (p=pos(src[s++])) >>> 4 & 1
                    )
                    ;                                               //   2 5 1
        dest[d++] = (byte)
                    (
                        (p & MASK_4BITS) << 4 
                        | (p=pos(src[s++])) >>> 1 & MASK_4BITS
                    )
                    ;                                               //       4 4
        dest[d++] = (byte)
                    (
                        (p & 1) << 7
                        | pos(src[s++]) << 2
                        | (p=pos(src[s++])) >>> 3 & MASK_2BITS
                    )
                    ;                                               //         1 5 2
        dest[d]   = (byte)
                    (
                        (p & MASK_3BITS) << 5 
                        | pos(src[s])
                    );                                              //             3 5
    }
    
    /**
     * @param n the number of final quantum in bytes to decode into.  Ranges from 1 to 4, inclusive.
     */
    static private void decode1to4bytes(int n, byte[] src, int s, byte[] dest, int d) {
        int p=0;
        // operator precedence in descending order: >>> or <<, &, |
        dest[d++] = (byte)
                    (
                        pos(src[s++]) << 3
                        | (p=pos(src[s++])) >>> 2 & MASK_3BITS
                    )
                    ;                                               // 5 3
        if (n == 1) {
            sanityCheckLastPos(p, MASK_2BITS);
            return;
        }
        
        dest[d++] = (byte)
                    (
                        (p & MASK_2BITS) << 6
                        | (pos(src[s++])) << 1 
                        | (p=pos(src[s++])) >>> 4 & 1
                    )
                    ;                                               //   2 5 1
        if (n == 2) {
            sanityCheckLastPos(p, MASK_4BITS);
            return;
        }
        
        dest[d++] = (byte)
                    (
                        (p & MASK_4BITS) << 4 
                        | (p=pos(src[s++])) >>> 1 & MASK_4BITS
                    )
                    ;                                               //       4 4
        if (n == 3) {
            sanityCheckLastPos(p, 1);
            return;
        }
        
        dest[d]   = (byte)
                    (
                        (p & 1) << 7
                        | pos(src[s++]) << 2
                        | (p=pos(src[s])) >>> 3 & MASK_2BITS
                    )
                    ;                                               //         1 5 2
        sanityCheckLastPos(p, MASK_3BITS);
    }
    
    static private byte[] internalDecode(byte[] src, final int l) {
//        if (length % 8 != 0)
//            throw new IllegalArgumentException
//            ("Input is expected to be encoded in multiple of 8 bytes but found: " + length);

        int possiblePads = 8 - (l % 8);
        int pads = possiblePads != 8 ? possiblePads : 0;  // assume any length missing from 8 are pads that are missing
        // adjust length like pads existed
        int length = l + pads;
        int last = l-1;

        // max possible padding in b32 encoding is 6
        for (; pads < 6 && last > -1; last--, pads++) {
            if (src[last] != PAD)
                break;
        }
        
        final int fq; // final quantum in unit of bytes
        
        switch(pads) {
            case 0:
                fq=5;
                break; // final quantum of encoding input is an integral multiple of 40 bits
            case 1:
                fq=4;
                break; // final quantum of encoding input is exactly 32 bits
            case 3:
                fq=3;
                break; // final quantum of encoding input is exactly 24 bits
            case 4:
                fq=2;
                break; // final quantum of encoding input is exactly 16 bits
            case 6:
                fq=1;
                break; // final quantum of encoding input is exactly 8 bits
            default:
                throw new IllegalArgumentException("Invalid number of paddings " + pads);
        }
        final byte[] dest = new byte[length / 8 * 5 - (5-fq)]; 
        int s=0, d=0;
        
        // % has a higher precedence than - than <
        for (; d < dest.length - fq%5; s+=8,d+=5)
            decode5bytes(src, s, dest, d);

        if (fq < 5)
            decode1to4bytes(fq, src, s, dest, d);
        return dest;
    }
    
    static private int pos(byte in) {
        int pos = LazyHolder.DECODED[in];
        
        if (pos > -1)
            return pos;
        throw new IllegalArgumentException("Invalid base 32 character: \'" + (char)in + "\'");
    }
    
    static public String encode(byte[] bytes) {
        return encode(bytes, true, false);
    }
    
    static public String encode(byte[] bytes, boolean padded) {
        return encode(bytes, padded, false);
    }
    
    static public String encode(byte[] bytes, boolean padded, boolean lower) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length == 0) {
            return "";
        }
        byte[] encoded = internalEncode(lower ? LOWER_CHARS : UPPER_CHARS, bytes, padded);
        return new String(encoded, StandardCharsets.ISO_8859_1);
    }
    
    static public byte[] decode(String b32) {
        if (b32 == null)
            return null;
        if (b32.length() == 0)
            return new byte[0];
        
        byte[] buf = b32.getBytes(StandardCharsets.ISO_8859_1);
        
        return internalDecode(buf, buf.length);
    }
    
    static private final int OFFSET_OF_2 = '2' - 26;
    
    static private class LazyHolder {
        private static final byte[] DECODED = decodeTable();
        
        private static byte[] decodeTable() {
            final byte[] dest = new byte['z'+1];
            
            for (int i=0; i <= 'z'; i++) 
            {
                if (i >= 'A' && i <= 'Z')
                    dest[i] = (byte)(i - 'A');
                else if (i >= '2' && i <= '7')
                    dest[i] = (byte)(i - OFFSET_OF_2);
                else if (i >= 'a' && i <= 'z')
                    dest[i] = (byte)(i - 'a');
                else 
                    dest[i] = -1;
            }
            return dest;
        }
    }

    static private final byte[] UPPER_CHARS = upperAlphabets();
    static private final byte[] LOWER_CHARS = lowerAlphabets();
    
    static private byte[] upperAlphabets() {
        // Base 32 alphabet as defined at http://www.ietf.org/rfc/rfc4648.txt
        return toBytesDirect("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");
    }

    static private byte[] lowerAlphabets() {
        // Base 32 alphabet as defined at http://www.ietf.org/rfc/rfc4648.txt
        return toBytesDirect("abcdefghijklmnopqrstuvwxyz234567");
    }
    
    static private byte[] toBytesDirect(final String singleOctets) {
        final char[] src = singleOctets.toCharArray();
        final byte[] dest = new byte[src.length];
        
        for (int i=0; i < dest.length; i++) {
            final char c = src[i];
            if (c > Byte.MAX_VALUE)
                throw new IllegalArgumentException("Invalid character found at position " + i + " for " + singleOctets);
            dest[i] = (byte)c;
        }
        
        return dest;
    }
    
    static private void sanityCheckLastPos(int pos, int mask) {
        if ((pos & mask) != 0) {
            throw new IllegalArgumentException
                ("Invalid last non-pad character detected");
        }
    }
    
}