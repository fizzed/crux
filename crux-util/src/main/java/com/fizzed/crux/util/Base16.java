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

public class Base16 {
 
    static public class Codec {
    
        static private final char[] LOWER_CHARS = "0123456789abcdef".toCharArray();
        static private final char[] UPPER_CHARS = "0123456789ABCDEF".toCharArray();
        
        static public int decodeChar(char c) {
            switch (c) {
                case '0':
                    return 0;
                case '1':
                    return 1;
                case '2':
                    return 2;
                case '3':
                    return 3;
                case '4':
                    return 4;
                case '5':
                    return 5;
                case '6':
                    return 6;
                case '7':
                    return 7;
                case '8':
                    return 8;
                case '9':
                    return 9;
                case 'A':
                case 'a':
                    return 10;
                case 'B':
                case 'b':
                    return 11;
                case 'C':
                case 'c':
                    return 12;
                case 'D':
                case 'd':
                    return 13;
                case 'E':
                case 'e':
                    return 14;
                case 'F':
                case 'f':
                    return 15;
                default:
                    throw new IllegalArgumentException("Invalid hex char " + c);
            }
        }
        
        static public String encode(char[] encodeAlphabet, byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            return encode(encodeAlphabet, bytes, bytes.length);
        }

        static public String encode(char[] encodeAlphabet, byte[] bytes, int maxLength) {
            if (bytes == null) {
                return null;
            }

            final int length = maxLength >= 0 && maxLength < bytes.length ? maxLength : bytes.length;

            if (length <= 0) {
                return "";
            }

            final StringBuilder buf = new StringBuilder(2 * length);

            for (int i = 0 ; i < length ; i++) {
                buf.append(encodeAlphabet[(0x0f & (bytes[i] >> 4))]);
                buf.append(encodeAlphabet[(0x0f & bytes[i])]);
            }

            return buf.toString();
        }

        static public byte[] decode(String b16) {
            if (b16 == null) {
                return null;
            }
            int stringLength = b16.length();
            if (stringLength == 0) {
                return new byte[0];
            }
            if (stringLength % 2 != 0) {
                throw new IllegalArgumentException("Invalid length for hex string");
            }
            int byteLength = stringLength / 2;
            byte[] bytes = new byte[byteLength];

            for (int i = 0 ; i < byteLength; i++) {
                bytes[i] |= (0x0f & decodeChar(b16.charAt(i*2))) << 4;
                bytes[i] |= (0x0f & decodeChar(b16.charAt((i*2)+1)));
            }
            return bytes;
        }
        
    }
    
    static public String encode(byte[] bytes) {
        return encode(bytes, -1);
    }
    
    static public String encode(byte[] bytes, int maxLength) {
        return encode(bytes, false, maxLength);
    }
    
    static public String encode(byte[] bytes, boolean upper) {
        return encode(bytes, upper, -1);
    }
    
    static public String encode(byte[] bytes, boolean upper, int maxLength) {
        return Codec.encode(upper ? Codec.UPPER_CHARS : Codec.LOWER_CHARS, bytes, maxLength);
    }
    
    static public byte[] decode(String b16) {
        return Codec.decode(b16);
    }
    
}