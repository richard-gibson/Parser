package com.ninecy.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class RegexParser implements Parser<RegexParserResult> {
    private final Pattern pattern;
    private final List<String> locations;

    public RegexParser(PatternBuilderResult patternBuilderResult) {
        this.pattern = patternBuilderResult.getPattern();
        this.locations = patternBuilderResult.getLocations();
    }

    /**
     * parse method runs regex from specification against input lines
     * all matches found are added to a match list, regex for each specification token
     * should only return 1 capture group meaning all match results > 0 should be stored
     *
     * @param input line parsed from input source
     * @return RegexParserResult
     */
    @Override
    public RegexParserResult parse(String input) {
        Matcher matcher = pattern.matcher(input);
        List<String> matches = new ArrayList<>();
        boolean matchFound = false;

        while(matcher.find()) {
         matchFound = true;
         for(int i=1; i<=matcher.groupCount(); i++) {
             String m = matcher.group(i);
             matches.add(m);
         }
        }
        return new RegexParserResult(matchFound,input,matches, locations);
    }
}
