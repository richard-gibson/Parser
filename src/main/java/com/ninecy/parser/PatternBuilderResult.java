package com.ninecy.parser;

import java.util.List;
import java.util.regex.Pattern;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class PatternBuilderResult {
    private final Pattern pattern;
    private final List<String> locations;

    public PatternBuilderResult(Pattern pattern, List<String> locations) {
        this.pattern = pattern;
        this.locations = locations;
    }

    public Pattern getPattern() {return pattern;}
    public List<String> getLocations() {return locations;}

    @Override
    public String toString() {
        return String.format("PatternBuilderResult{pattern='%s', locations=%s}", pattern, locations);
    }
}
