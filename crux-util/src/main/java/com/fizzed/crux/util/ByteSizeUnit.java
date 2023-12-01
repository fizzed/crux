package com.fizzed.crux.util;

/**
 * https://en.wikipedia.org/wiki/Megabyte
 */
public enum ByteSizeUnit {

    B("B", 1L),
    KiB("K", 1024L),
    MiB("M", 1048576L),
    GiB("G", 1073741824L),
    TiB("T", 1073741824L*1024),
    PiB("P", 1073741824L*1024*1024),
    EiB("E", 1073741824L*1024*1024*1024);
    // can't go any higher, we'll overflow a long

    final private long multiplier;
    final private String shortId;

    ByteSizeUnit(String shortId, long multiplier) {
        this.shortId = shortId;
        this.multiplier = multiplier;
    }

    public String getShortId() {
        return shortId;
    }

    public long getMultiplier() {
        return multiplier;
    }

    public long toByteSize(long size) {
        return size * this.multiplier;
    }

    public double fromByteSize(long byteSize) {
        return ((double)byteSize / (double)this.multiplier);
    }

    static public ByteSizeUnit resolve(String unit) {
        if (unit != null) {
            for (ByteSizeUnit u : ByteSizeUnit.values()) {
                if (u.name().equalsIgnoreCase(unit)) {
                    return u;
                } else if (u.getShortId().equalsIgnoreCase(unit)) {
                    return u;
                }
            }
        }
        return null;
    }

}