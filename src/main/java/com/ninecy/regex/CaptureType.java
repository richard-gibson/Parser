package com.ninecy.regex;

/**
 * User: Richard
 * Date: 21.03.15
 *
 * Enumeration to decide capture type base on capture token
 * String. Capture type is used to select relevant regex pattern
 *
 */
public enum CaptureType {
    DEFAULT(""),
    GREEDY("G"),
    WHITESPACE("S"),
    UNKNOWN(null);

    private final String identifier;

    CaptureType(String identifier) {
        this.identifier = identifier;
    }

    public static CaptureType getType(String name) {

        String firstChar = name.isEmpty() ? "" : name.substring(0,1);
        for (CaptureType value : values()) {
            if (value.identifier.equals(firstChar)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
