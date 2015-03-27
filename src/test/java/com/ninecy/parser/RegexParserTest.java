package com.ninecy.parser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.ninecy.regex.RegexPatterns.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class RegexParserTest {


    @Test
    public void testDefaultMatchParse() {
        String defaultMatchPattern =
                String.format("foo %s is a %s", NON_GREEDY.getPattern(), EOL_CAPTURE.getPattern());
        assertParserResult(
                defaultMatchPattern,
                "foo blah is a bar",
                Arrays.asList("blah", "bar")
        );
        assertParserResult(
                defaultMatchPattern,
                "foo blah is a very big boat",
                Arrays.asList("blah", "very big boat")
        );
    }


    @Test
    public void testDefaultMatchFailsToParse() {
        String defaultMatchPattern =
                String.format("foo %s is a %s", NON_GREEDY.getPattern(), EOL_CAPTURE.getPattern());

        assertFailedParserResult(defaultMatchPattern,"foo blah is bar");
        assertFailedParserResult(defaultMatchPattern,"foo blah");
        assertFailedParserResult(defaultMatchPattern,"foo blah is");

    }


    @Test
    public void testZeroSpaceMatchParse() {
        String zeroSpaceMatchPattern =
                String.format("foo %s is a %s", NON_GREEDY.getPattern(), ZERO_WHITESPACE.getPattern());
        assertParserResult(
                zeroSpaceMatchPattern,
                "foo blah is a bar",
                Arrays.asList("blah", "bar")
        );
    }


    @Test
    public void testZeroSpaceMatchFailsToParse() {
        String zeroSpaceMatchPattern =
                String.format("foo %s is a %s", NON_GREEDY.getPattern(), ZERO_WHITESPACE.getPattern());

        assertFailedParserResult(zeroSpaceMatchPattern,"foo blah is a very big boat");
        assertFailedParserResult(zeroSpaceMatchPattern,"foo blah is bar");
        assertFailedParserResult(zeroSpaceMatchPattern,"foo blah");
        assertFailedParserResult(zeroSpaceMatchPattern,"foo blah is");

    }

    @Test
    public void testSpaceMatchParse() {
        String s1Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "1");
        String spaceMatchPattern = String.format("the %s %s ran away", s1Replace, NON_GREEDY.getPattern());
        assertParserResult(
                spaceMatchPattern,
                "the big brown fox ran away",
                Arrays.asList("big brown", "fox")
        );

        assertParserResult(
                spaceMatchPattern,
                "the big red and brown fox ran away",
                Arrays.asList("big red", "and brown fox")
        );
    }

    @Test
    public void testSpaceMatchFailsToParse() {
        String s1Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "1");
        String zeroSpaceMatchPattern = String.format("the %s %s ran away", s1Replace, NON_GREEDY.getPattern());

        assertFailedParserResult(zeroSpaceMatchPattern,"the brown fox ran away");
        assertFailedParserResult(zeroSpaceMatchPattern,"the fox ran away");

    }


    @Test
    public void testGreedyMatchParse() {
        String greedyMatchPattern = String.format("bar %s foo %s", GREEDY.getPattern(), EOL_CAPTURE.getPattern());
        assertParserResult(
                greedyMatchPattern,
                "bar foo bar foo bar foo bar foo",
                Arrays.asList("foo bar foo bar", "bar foo")
        );

    }

    @Test
    public void testNonGreedyMatchParse() {
        String greedyMatchPattern = String.format("bar %s foo %s", NON_GREEDY.getPattern(), EOL_CAPTURE.getPattern());
        assertParserResult(
                greedyMatchPattern,
                "bar foo bar foo bar foo bar foo",
                Arrays.asList("foo bar", "bar foo bar foo")
        );

    }



    private void assertFailedParserResult(String matchPattern, String parseInput) {

        PatternBuilderResult patternBuilderResult =
                new PatternBuilderResult(Pattern.compile(matchPattern), new ArrayList<>());

        RegexParser regexParser = new RegexParser(patternBuilderResult);
        RegexParserResult regexParserResult = regexParser.parse(parseInput);

        assertFalse("Parser match found", regexParserResult.isMatchFound());
        assertTrue("Parser match results found", regexParserResult.getMatchResults().isEmpty());

    }

    private void assertParserResult(String matchPattern, String parseInput, List<String> capturedElems) {
        PatternBuilderResult patternBuilderResult =
                new PatternBuilderResult(Pattern.compile(matchPattern), new ArrayList<>());

        RegexParser regexParser = new RegexParser(patternBuilderResult);
        RegexParserResult regexParserResult = regexParser.parse(parseInput);

        assertTrue("Parser match not found",regexParserResult.isMatchFound());
        assertEquals(capturedElems, regexParserResult.getMatchResults());

    }

}