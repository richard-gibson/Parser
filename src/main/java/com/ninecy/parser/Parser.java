package com.ninecy.parser;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public interface Parser<T> {
    T parse(String input);
}