package com.ninecy.parser;

import com.ninecy.regex.CaptureType;
import com.ninecy.regex.RegexPatterns;
import com.ninecy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */

public class RegexPatternBuilder implements PatternBuilder<PatternBuilderResult> {
    private final Pattern pattern;
    private final Pattern escapeCharPattern;

    public RegexPatternBuilder(String specificationExtractor) throws PatternBuilderException {
        this.pattern = compilePattern(specificationExtractor);
        this.escapeCharPattern = compilePattern(RegexPatterns.ESCAPE_METACHARS.getPattern());
    }

    /**
     * Method used to build regex from submitted specification
     * specification is parsed to retrieve each capture token and
     * replace with suitable regex.
     * capture tokens are also stored in result for output formatting
     *
     * @param specification candidate specification to be converted to regex
     * @return PatternBuilderResult
     * @throws PatternBuilderException
     */
    public PatternBuilderResult build(String specification) throws PatternBuilderException {
        StringBuilder patternStringBuilder = new StringBuilder();
        Matcher captureTokenMatcher = pattern.matcher(specification);
        List<String> locations = new ArrayList<>();

        int charLoc = 0;
        while(captureTokenMatcher.find()) {
            String match = captureTokenMatcher.group(1);

            String regexReplacement = genRegex(captureTokenMatcher.end(), match, specification);
            String literalReplacement = escapeLiteralRegexChars(specification.substring(charLoc, captureTokenMatcher.start()));

            patternStringBuilder
                    .append(literalReplacement)
                    .append(regexReplacement);

            locations.add(match);

            charLoc = captureTokenMatcher.end();


        }

        patternStringBuilder
                .append(escapeLiteralRegexChars(specification.substring(charLoc, specification.length())));

        return new PatternBuilderResult(
                compilePattern(patternStringBuilder.toString()),
                ensureUniqueIndex(locations));

    }



    /**
     *  Wrapper to compile pattern to parse specification and the pattern created from the
     *  specification.
     * @param pattern candidate pattern as String
     * @return successfully compiles regex pattern
     * @throws PatternBuilderException safer unchecked exception for calling app to handle
     */
    private Pattern compilePattern(String pattern) throws PatternBuilderException {
        try{
           return Pattern.compile(pattern);
        } catch (PatternSyntaxException pse) {
            throw new PatternBuilderException("unable to compile regex for pattern: "+pattern, pse);
        }
    }


    private String escapeLiteralRegexChars(String literalText) {
        return escapeCharPattern
                .matcher(literalText)
                .replaceAll(RegexPatterns.ESCAPE_METACHARS_REPLACEMENT.getPattern());
    }

    /**
     * generates regex dependant on capture token and position in specification,
     * a different regex will be generated for some capture tokens if at the end
     * of the specification
     *
     * enums used for some Type safety when examining token type
     * and to contain regex in one place
     *
     * @param charLoc location after match to check if end of specification
     * @param captureToken token to gen regex from
     * @param specification complete specification passed
     * @return relevant regex
     * @throws PatternBuilderException
     */
    private String genRegex(int charLoc, String captureToken, String specification) throws PatternBuilderException {

        //Remove leading digits from string before retrieving capture type
        String strippedLeadingDigits = StringUtil.dropWhile(Character::isDigit,captureToken);

        switch (CaptureType.getType(strippedLeadingDigits)) {
            case DEFAULT:
                return  genNonGreedyRegex(charLoc,specification,
                        RegexPatterns.EOL_CAPTURE.getPattern(),
                        RegexPatterns.NON_GREEDY.getPattern());
            case GREEDY:
                return RegexPatterns.GREEDY.getPattern();
            case WHITESPACE:
                return genWhiteSpaceRegex(
                        Integer.parseInt(
                            StringUtil.dropWhile(Character::isAlphabetic, strippedLeadingDigits)
                        ),
                        RegexPatterns.VARIABLE_WHITESPACE.getPattern(),
                        RegexPatterns.ZERO_WHITESPACE.getPattern());

            case UNKNOWN: default:
                throw new PatternBuilderException("Unable to generate regex from" + captureToken);
        }
    }

    /**
     * Ensure Index List elements are unique by creating Set from elements
     * and comparing size.
     * @param locations list of
     * @return original list if no duplicates
     * @throws PatternBuilderException if duplicate capture types found
     */
    private List<String> ensureUniqueIndex(List<String> locations) throws PatternBuilderException {

        List<String>captureTokenIndexes =
                locations.stream().map(
                        elem -> StringUtil.takeWhile(Character::isDigit, elem)
                        ).collect(Collectors.toList());

        if (new HashSet<>(captureTokenIndexes).size() < captureTokenIndexes.size())
              throw new PatternBuilderException("duplicate index used in capture types: "+locations);

        return locations;
    }

    /**
     * A little extra logic needed for space limitation matches,
     * zero space limitation needs a different regex pattern to other
     * space limitation regex matches
     *
     * @param noOfWhiteSpace int value added to multiWhitespace template
     * @param multiWhiteSpace regex template to be used for space limitation > 0
     * @param zeroWhiteSpace regex template to be used for space limitation == 0
     * @return relevant capture type
     */

    private String genWhiteSpaceRegex(int noOfWhiteSpace,
                                      String multiWhiteSpace,
                                      String zeroWhiteSpace) {

        return (noOfWhiteSpace == 0) ? zeroWhiteSpace
            : multiWhiteSpace.replace("%%", String.valueOf(noOfWhiteSpace));
    }

    /**
     * generates regex pattern dependant on capture token and position in specification
     *
     * @param charloc location after match to check if end of specification
     * @param specification input specification
     * @param eolcapture capture type if end of spec
     * @param nonGreedyCapture capture type if not end of spec
     * @return relevant capture type
     */
    private String genNonGreedyRegex(int charloc,
                                     String specification,
                                      String eolcapture,
                                      String nonGreedyCapture) {

        return (charloc == specification.length()) ? eolcapture : nonGreedyCapture;
    }
}
