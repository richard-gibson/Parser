package com.ninecy.parser;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static com.ninecy.regex.RegexPatterns.*;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class RegexPatternBuilderTest {


    @Test
    public void testDefaultRegexBuild() throws Exception {

        assertPatternBuilderResult("the %{0} %{1} ran away",
            String.format("the %s %s ran away", NON_GREEDY.getPattern(), NON_GREEDY.getPattern()),
            Arrays.asList("0", "1")
        );
    }


    @Test
    public void testEOLRegexBuild() throws Exception {

        assertPatternBuilderResult("the %{0} ran away %{1}",
            String.format("the %s ran away %s", NON_GREEDY.getPattern(), EOL_CAPTURE.getPattern()),
            Arrays.asList("0", "1")
        );
    }

    @Test
    public void testSpaceDelimitedRegexBuild() throws Exception {
        String s1Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "1");
        String s4Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "4");

        assertPatternBuilderResult("the %{0S1} %{2S4} ran away %{1S0}",
            String.format("the %s %s ran away %s", s1Replace, s4Replace, ZERO_WHITESPACE.getPattern()),
            Arrays.asList("0S1", "2S4", "1S0")
        );

    }

    @Test
         public void testGreedyRegexBuild() throws Exception {
        String greedyPattern = GREEDY.getPattern();

        assertPatternBuilderResult("bar %{0G} foo %{1G}",
                String.format("bar %s foo %s", greedyPattern,greedyPattern),
                Arrays.asList("0G", "1G")
        );
    }


    @Test
    public void testMixedRegexBuild() throws Exception {
        String s1Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "1");


        assertPatternBuilderResult("is %{2S1} message %{0} ballpark %{1G} more %{3} %{4S0} text",
            String.format(
                    "is %s message %s ballpark %s more %s %s text",
                    s1Replace, NON_GREEDY.getPattern(),
                    GREEDY.getPattern(), NON_GREEDY.getPattern(),
                    ZERO_WHITESPACE.getPattern()),
            Arrays.asList("2S1", "0", "1G", "3", "4S0")
        );

    }

    @Test
    public void testEscapeRegexMetaCharsBuild() throws Exception {
        String s1Replace = VARIABLE_WHITESPACE.getPattern().replace("%%", "1");


        assertPatternBuilderResult("is %{2S1} me<<>>ssage %{0} ballpark %{1G} mo(.*)?re %{3} %{4S0} te.xt...",
                String.format(
                        "is %s me\\<\\<\\>\\>ssage %s ballpark %s mo\\(\\.\\*\\)\\?re %s %s te\\.xt\\.\\.\\.",
                        s1Replace, NON_GREEDY.getPattern(),
                        GREEDY.getPattern(), NON_GREEDY.getPattern(),
                        ZERO_WHITESPACE.getPattern()),
                Arrays.asList("2S1", "0", "1G", "3", "4S0")
        );

    }


    @Test(expected=PatternBuilderException.class)
    public void testMalformedPatternException() throws PatternBuilderException {
        new RegexPatternBuilder("???");
    }

    @Test(expected=PatternBuilderException.class)
    public void testDuplicateDefaultPlaceholderException() throws Exception {
        RegexPatternBuilder regexPatternBuilder = new RegexPatternBuilder(TEMPLATE.getPattern());
        regexPatternBuilder.build("is %{0} message %{0}");
    }

    @Test(expected=PatternBuilderException.class)
         public void testDuplicateGreedyPlaceholderException() throws Exception {
        RegexPatternBuilder regexPatternBuilder = new RegexPatternBuilder(TEMPLATE.getPattern());
        regexPatternBuilder.build("is %{1G} message %{1G}");
    }

    @Test(expected=PatternBuilderException.class)
    public void testDuplicateSpacePlaceholderException() throws Exception {
        RegexPatternBuilder regexPatternBuilder = new RegexPatternBuilder(TEMPLATE.getPattern());
        regexPatternBuilder.build("is %{2S1} message %{2S1}");
    }

    @Test(expected=PatternBuilderException.class)
    public void testMixedDuplicatePlaceholderException() throws Exception {
        RegexPatternBuilder regexPatternBuilder = new RegexPatternBuilder(TEMPLATE.getPattern());
        regexPatternBuilder.build("is %{1} message %{1G} here %{1S3}");
    }


    private void assertPatternBuilderResult(String specification,
                                            String expectedRegex,
                                            List<String> expectedLocations) throws PatternBuilderException {
        RegexPatternBuilder regexPatternBuilder = new RegexPatternBuilder(TEMPLATE.getPattern());
        PatternBuilderResult patternBuilderResult = regexPatternBuilder.build(specification);

        //comparing string value of regex as Pattern Object doesn't implement equals
        assertEquals(expectedRegex, patternBuilderResult.getPattern().toString());

        assertEquals(expectedLocations, patternBuilderResult.getLocations());
    }



}