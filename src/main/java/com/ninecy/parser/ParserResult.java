package com.ninecy.parser;

/**
 *
 * User: Richard
 * Date: 21.03.15
 *
 */
public interface ParserResult {
    String verboseResult();
    String shortResult();
    boolean isMatchFound();
}
