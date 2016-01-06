package com.ninecy.parser;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public interface PatternBuilder<T> {
    T build(String input) throws PatternBuilderException;
}
