package com.ninecy.parser;

import java.util.List;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */
public class RegexParserResult implements ParserResult {
    private final boolean matchFound;
    private final String input;
    private final List<String> matchResults;
    private final List<String> locations;

    public RegexParserResult(boolean matchFound, String input,
                             List<String> matchResults, List<String> locations) {
        this.matchFound = matchFound;
        this.input = input;
        this.matchResults = matchResults;
        this.locations = locations;
    }

    public boolean isMatchFound() {
        return matchFound;
    }

    public List<String> getMatchResults() {
        return matchResults;
    }

    public String shortResult() {
        return input;
    }


    public String verboseResult() {
        StringBuilder sb = new StringBuilder();
        sb.append(input).append("\n");

        //modulus check as there can be multiple specification matches per line.

        if(!locations.isEmpty() &&
                !matchResults.isEmpty() &&
                matchResults.size()%locations.size()==0) {

            for(int i=0; i<matchResults.size(); i++) {
                sb.append(
                     String.format("%%{%s} - '%s'\n",
                         locations.get(i%locations.size()),
                         matchResults.get(i))
                );

            }
        } else {
            sb.append(locations).append("\n");
            sb.append(matchResults).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "RegexParserResult{" +
                "matchFound=" + matchFound +
                ", input='" + input + '\'' +
                ", matchResults=" + matchResults +
                ", locations=" + locations +
                '}';
    }
}
