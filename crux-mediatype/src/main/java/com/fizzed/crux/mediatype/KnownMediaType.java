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

import static com.fizzed.crux.mediatype.KnownMediaType.APPLICATION_XML;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.Set;

/**
 * https://www.iana.org/assignments/media-types/media-types.xhtml
 * 
 * @author jjlauer
 */
public enum KnownMediaType {
 
    // order of declaration is critical!
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_OCTET_STREAM_NON_STANDARD("application/octetstream", APPLICATION_OCTET_STREAM),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JAR("application/java-archive", asList("jar")),
    APPLICATION_JAVASCRIPT("application/javascript", asList("js")),
    APPLICATION_JSON("application/json", asList("json")),
    APPLICATION_VND_API_JSON("application/vnd.api+json", APPLICATION_JSON),
    APPLICATION_XML("application/xml", asList("xml")),
    APPLICATION_PDF("application/pdf", asList("pdf")),
    APPLICATION_MSWORD("application/msword", asList("doc", "dot")),
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", asList("docx")),
    APPLICATION_VND_MS_POWERPOINT("application/vnd.ms-powerpoint", asList("ppt", "pot", "pps", "ppa")),
    APPLICATION_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", asList("pptx")),
    APPLICATION_VND_MS_EXCEL("application/vnd.ms-excel", asList("xls", "xlt", "xla")),
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", asList("xlsx")),
    APPLICATION_ZIP("application/zip", asList("zip")),
    IMAGE_X_ICO("image/x-icon", asList("ico")),
    IMAGE_PNG("image/png", asList("png")),
    IMAGE_JPEG("image/jpeg", asList("jpg", "jpeg")),
    IMAGE_JPEG_NON_STANDARD("image/jpg", IMAGE_JPEG),
    IMAGE_GIF("image/gif", asList("gif")),
    IMAGE_BMP("image/bmp", asList("bmp")),
    IMAGE_TIFF("image/tiff", asList("tiff", "tif")),
    IMAGE_SVG_XML("image/svg+xml", asList("svg")),
    IMAGE_WEBP("image/webp", asList("webp")),
    FONT_TTF("font/ttf", asList("ttf")),
    FONT_WOFF("font/woff", asList("woff")),
    FONT_WOFF2("font/woff2", asList("woff2")),
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    MULTIPART_MIXED("multipart/mixed"),
    MULTIPART_RELATED("multipart/related"),
    VIDEO_3GPP("video/3gpp", asList("3gp", "3gpp")),
    VIDEO_X_MATROSKA("video/x-matroska", asList("mkv")),
    VIDEO_X_FLV("video/x-flv", asList("flv")),
    VIDEO_H264("video/h264", asList("h264")),
    VIDEO_MP4("video/mp4", asList("mp4")),
    VIDEO_MPEG("video/mpeg", asList("mpeg", "mpg")),
    VIDEO_OGG("video/ogg", asList("ogg")),
    VIDEO_QUICKTIME("video/quicktime", asList("quicktime")),
    VIDEO_WEBM("video/webm", asList("webm")),
    TEXT_CSS("text/css", asList("css")),
    TEXT_CSV("text/csv", asList("csv")),
    TEXT_PLAIN("text/plain", asList("txt")),
    TEXT_HTML("text/html", asList("html", "htm")),
    TEXT_XML("text/xml", APPLICATION_XML),                                      // text/xml legacy => application/xml preferred
    MESSAGE_RFC822("message/rfc822", asList("msg", "eml"));
    
    static private final Map<String,KnownMediaType> LABELS;
    static private final Map<String,KnownMediaType> EXTENSIONS;
    static private final Map<KnownMediaType,Set<KnownMediaType>> ALIASES;
    static {
        LABELS = new HashMap<>();
        EXTENSIONS = new HashMap<>();
        ALIASES = new HashMap<>();
        for (KnownMediaType mediaType : KnownMediaType.values()) {
            // case insensitive label
            String label = mediaType.getLabel().toLowerCase();
            if (LABELS.containsKey(label)) {
                throw new IllegalArgumentException("Uh oh - duplicate label " + label
                    + " found with " + mediaType + ". You probably want to check your code.");
            }
            LABELS.put(label, mediaType);
            
            // case insensitive file extension
            if (mediaType.getOwnExtensions() != null) {
                for (String extension : mediaType.getOwnExtensions()) {
                    extension = extension.toLowerCase();    // case insensitive
                    if (EXTENSIONS.containsKey(extension)) {
                        throw new IllegalArgumentException("Uh oh - duplicate file extension " + extension
                            + " found with " + mediaType + ". You probably want to register as an alias.");
                    }
                    EXTENSIONS.put(extension, mediaType);
                }
            }
            
            // aliases by parent...
            if (mediaType.aliasOf != null) {
                Set<KnownMediaType> aliases = ALIASES.computeIfAbsent(
                    mediaType.aliasOf, (kmt) -> new HashSet<>());
                aliases.add(mediaType);
            }
        }
    }
    
    private final String label;
    private final List<String> extensions;
    private final KnownMediaType aliasOf;
    
    KnownMediaType(String label) {
        this(label, null, null);
    }
    
    KnownMediaType(String label, List<String> extensions) {
        this(label, extensions, null);
    }
    
    KnownMediaType(String label, KnownMediaType aliasOf) {
        this(label, null, aliasOf);
    }
    
    KnownMediaType(String label, List<String> extensions, KnownMediaType aliasOf) {
        this.label = label;
        this.extensions = extensions;
        this.aliasOf = aliasOf;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * Gets the first (main) file extension associated with this media type.
     * If the media type is an alias to a different media type then the first (main)
     * file extension of that is returned instead.
     * @return 
     */
    public String getFirstExtension() {
        List<String> ext = this.getExtensions();
        
        if (ext == null || ext.isEmpty()) {
            return null;
        }
        
        return ext.get(0);
    }
    
    /**
     * Gets the file extensions associated with this media type.
     * If the media type is an alias to a different media type then the
     * file extensions of that is returned instead.
     * @return 
     */
    public List<String> getExtensions() {
        if (this.extensions == null) {
            // alias?
            if (this.aliasOf != null) {
                return this.aliasOf.getExtensions();
            }
        }
        return this.extensions;
    }

    public String getOwnFirstExtension() {
        if (this.extensions == null || this.extensions.isEmpty()) {
            return null;
        }
        return this.extensions.get(0);
    }
    
    public List<String> getOwnExtensions() {
        return this.extensions;
    }
    
    public KnownMediaType getAliasOf() {
        return this.aliasOf;
    }
    
    /**
     * Normalize a known media type by recursing thru its aliasOf list till it
     * gets to a known media type that doesn't have an alias.
     * 
     * @return 
     */
    public KnownMediaType normalize() {
        KnownMediaType kmt = this;
        while (kmt.getAliasOf() != null) {
            kmt = kmt.getAliasOf();
        }
        return kmt;
    }
    
    public boolean isSame(KnownMediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (this.equals(mediaType)) {
            return true;
        }
        // do we have any aliases?
        Set<KnownMediaType> ourAliases = ALIASES.get(this);
        if (ourAliases != null && ourAliases.contains(mediaType)) {
            return true;
        }
        // are we an alias of something else?
        if (this.aliasOf != null && this.aliasOf.isSame(mediaType)) {
            return true;
        }
        return false;
    }
    
    // helpers
    
    static public Optional<KnownMediaType> fromLabel(String label) {
        if (label != null) {
            label = label.trim().toLowerCase();                     // sanitize a bit
            return ofNullable(LABELS.get(label));
        }
        return Optional.empty();
    }
    
    static public Optional<KnownMediaType> fromExtension(String extension) {
        if (extension != null) {
            extension = extension.trim().toLowerCase();             // sanitize a bit
            return ofNullable(EXTENSIONS.get(extension));
        }
        return Optional.empty();
    }
    
    static public Optional<KnownMediaType> fromFileName(String filename) {
        if (filename != null && filename.length() > 1) {
            filename = filename.toLowerCase();
            // strip out first file extension
            int periodPos = filename.lastIndexOf('.');
            if (periodPos >= 0) {
                String extension = filename.substring(periodPos+1);
                return ofNullable(EXTENSIONS.get(extension));
            }
        }
        return Optional.empty();
    }
    
    static public Optional<KnownMediaType> fromHeader(String header) {
        ContentType contentType = ContentType.parse(header);
        
        return ofNullable(contentType)
            .map(v -> v.getKnownMediaType());
    }
    
}