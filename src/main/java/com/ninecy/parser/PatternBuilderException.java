package com.ninecy.parser;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class PatternBuilderException extends Exception {
    public PatternBuilderException(String errorMessage) {
        super(errorMessage);
    }

    public PatternBuilderException(String errorMessage, Exception e) {
        super(errorMessage,e);
    }
}
