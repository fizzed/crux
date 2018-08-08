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
package com.fizzed.crux.mediatype;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Code based almost entirely on okhttp ContentType utility class except this
 * version will parse unlimited parameters.
 *
 * https://tools.ietf.org/html/rfc2045
 *
 * http://www.iana.org/assignments/media-types/media-types.xhtml
 *
 * getType "/" getSubType *(";" parameter) ; Matching of media getType and
 * getSubType ; is ALWAYS case-insensitive.
 *
 * @author jjlauer
 */
public class ContentType {

    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final Pattern TYPE_SUBTYPE = Pattern.compile(TOKEN + "/" + TOKEN);
    private static final Pattern PARAMETER = Pattern.compile(
        ";\\s*(?:" + TOKEN + "=(?:" + TOKEN + "|" + QUOTED + "))?");

    private final String parsed;
    private final KnownMediaType knownMediaType;
    private final String type;              // e.g. "image"
    private final String subtype;           // e.g. "png"
    private final String charset;

    private ContentType(String parsed, String type, String subtype, String charset) {
        this.parsed = parsed;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
        this.knownMediaType = KnownMediaType.fromLabel(type + "/" + subtype).orElse(null);
    }

    /**
     * Returns a media getType for {@code string}.
     *
     * @param string
     * @return
     * @throws IllegalArgumentException if {@code string} is not a well-formed
     * media getType.
     */
    static public ContentType parse(String string) {

        Matcher typeSubtype = TYPE_SUBTYPE.matcher(string);
        if (!typeSubtype.lookingAt()) {
            throw new IllegalArgumentException("No subtype found for: \"" + string + '"');
        }
        String type = typeSubtype.group(1).toLowerCase(Locale.US);
        String subtype = typeSubtype.group(2).toLowerCase(Locale.US);

        String charset = null;
        Matcher parameter = PARAMETER.matcher(string);
        for (int s = typeSubtype.end(); s < string.length(); s = parameter.end()) {
            parameter.region(s, string.length());
            if (!parameter.lookingAt()) {
                throw new IllegalArgumentException("Parameter is not formatted correctly: \""
                    + string.substring(s)
                    + "\" for: \""
                    + string
                    + '"');
            }

            String name = parameter.group(1);
            if (name == null || !name.equalsIgnoreCase("charset")) {
                continue;
            }
            String charsetParameter;
            String token = parameter.group(2);
            if (token != null) {
                // If the token is 'single-quoted' it's invalid! But we're lenient and strip the quotes.
                charsetParameter = (token.startsWith("'") && token.endsWith("'") && token.length() > 2)
                    ? token.substring(1, token.length() - 1)
                    : token;
            } else {
                // Value is "double-quoted". That's valid and our regex group already strips the quotes.
                charsetParameter = parameter.group(3);
            }
            if (charset != null && !charsetParameter.equalsIgnoreCase(charset)) {
                throw new IllegalArgumentException("Multiple charsets defined: \""
                    + charset
                    + "\" and: \""
                    + charsetParameter
                    + "\" for: \""
                    + string
                    + '"');
            }
            charset = charsetParameter;
        }

        return new ContentType(string, type, subtype, charset);
    }

    /**
     * Returns a media getType for {@code string}, or null if {@code string} is
     * not a well-formed media getType.
     *
     * @param string
     * @return
     */
//    static public ContentType parse(String string) {
//        try {
//            return parse(string);
//        } catch (IllegalArgumentException ignored) {
//            return null;
//        }
//    }
    
    
    public String getParsed() {
        return this.parsed;
    }
    
    public KnownMediaType getKnownMediaType() {
        return this.knownMediaType;
    }

    /**
     * Returns the high-level media getType, such as "text", "image", "audio",
     * "video", or "application".
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Returns a specific media getSubType, such as "plain" or "png", "mpeg",
     * "mp4" or "xml".
     *
     * @return
     */
    public String getSubType() {
        return this.subtype;
    }

    /**
     * Returns the getCharset of this media getType, or null if this media
     * getType doesn't specify a getCharset.
     *
     * @return
     */
    public Charset getCharset() {
        //return setCharset(null);
         return this.charset != null ? Charset.forName(charset) : null;
    }

    @Override
    public String toString() {
        return this.parsed;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ContentType && ((ContentType) other).parsed.equals(parsed);
    }
    
    @Override
    public int hashCode() {
        return parsed.hashCode();
    }
}