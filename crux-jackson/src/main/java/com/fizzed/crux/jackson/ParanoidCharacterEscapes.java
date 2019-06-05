package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * Customer character escapes for Jackson for the paranoid.  Useful to embedding
 * json in html or a script tag where the forward slash will be escaped. Ensures
 * no one can unsafely do an XSS attack for adding a "</script>" tag.
 * 
 * 
 *   this.objectMapper.getFactory().setCharacterEscapes(ParanoidCharacterEscapes.INSTANCE);
 * 
 * 
 * @author jjlauer
 */
public class ParanoidCharacterEscapes extends CharacterEscapes {

    static private final int[] ASCII_ESCAPES = CharacterEscapes.standardAsciiEscapesForJSON();
    static {
        // since / is ascii we need to tell jackson to escape it
        ASCII_ESCAPES['/'] = ESCAPE_CUSTOM;
    }
    static private final SerializedString ESCAPED_FORWARD_SLASH = new SerializedString("\\/");
    
    static public final ParanoidCharacterEscapes INSTANCE = new ParanoidCharacterEscapes();

    @Override
    public SerializableString getEscapeSequence(int ch) {
        switch (ch) {
            case '/':
                return ESCAPED_FORWARD_SLASH;
            default:
                return null;
        }
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return ASCII_ESCAPES;
    }
    
}