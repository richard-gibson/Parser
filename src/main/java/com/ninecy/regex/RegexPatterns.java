package com.ninecy.regex;

/**
 * User: Richard
 * Date: 21.03.15
 *
 * Enumeration to keep all regex in one place
 *
 */
public enum RegexPatterns {

    TEMPLATE("\\%\\{(\\d+(?:(?:S\\d+)|G)?)\\}"),
    NON_GREEDY("(.*?)"),
    EOL_CAPTURE("(.*)"),
    GREEDY("(.*)"),
    VARIABLE_WHITESPACE("((?:\\w+\\s){%%}(?:\\w+))"),
    ZERO_WHITESPACE("(\\w+)$"),
    ESCAPE_METACHARS_REPLACEMENT("\\\\$0"),
    ESCAPE_METACHARS("[\\<\\(\\[\\{\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]");

    private final String pattern;

    private RegexPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    }
