package com.fizzed.crux.jackson;

public enum JavaTimeFormat {

    /**
     * ISO 8601 date and time format such as 2026-05-19T13:44:500Z
     */
    ISO_8601,
    /**
     * Epoch milliseconds format such as 1684474490000
     */
    EPOCH_MILLIS,
    /**
     * Epoch seconds format such as 1684474490
     */
    EPOCH_SECS;

}