package com.ninecy.util;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class StringUtil {


    /**
     * Takes longest prefix of elements that satisfy given predicate.
     */
    public static String takeWhile(Predicate<Character> predicate,String input) {
        char[] inpChars = input.toCharArray();
        int i = 0;
        while (i < inpChars.length && predicate.test(inpChars[i]))
            i++;

        return String.valueOf(Arrays.copyOfRange(inpChars, 0, i));
    }


    /**
     * Drops longest prefix of elements that satisfy a predicate.
     */
    public static String dropWhile(Predicate<Character> predicate,String input){
        char[] inpChars = input.toCharArray();
        int i = 0;
        while (i < inpChars.length && predicate.test(inpChars[i]))
            i++;

        return String.valueOf(Arrays.copyOfRange(inpChars, i, inpChars.length));
    }
}
