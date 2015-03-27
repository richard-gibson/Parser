package com.ninecy.app;

import com.ninecy.parser.*;
import com.ninecy.regex.RegexPatterns;
import com.ninecy.util.ArgsUtil;

import java.io.*;
import java.util.function.Function;

/**
 *
 * User: Richard
 * Date: 21.03.15
 *
 */
public class ParserApp {
    /**
     * method used to run parser from CLI using sys in/out
     * as input/output streams.
     * @param args 2 arguments
     *         : manditory specification for matching data
     *         : optional verbose flag that defaults as false if not set
     */
    public static void main(String[] args) {

        String specification = ArgsUtil.getMandatoryProperty("specification", args);
        Boolean verboseMode = Boolean.valueOf(ArgsUtil.getProperty("verbose", args).orElse("false"));

        //try with resources used so no need for finally block
        try(InputStream in = System.in;
            OutputStream out = System.out) {


            PatternBuilder<PatternBuilderResult> regexPatternBuilder =
                    new RegexPatternBuilder(RegexPatterns.TEMPLATE.getPattern());

            PatternBuilderResult patternBuilderResult = regexPatternBuilder.build(specification);
            Parser<RegexParserResult> parser = new RegexParser(patternBuilderResult);

            runParserStream(in,out, parser, verboseMode);

        }
        catch (IOException | PatternBuilderException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }

    /**
     *  Main workflow method used to process data
     *  setup reader, writer then processes data through simple pipeline
     *
     *  read from input => parse => filter positive => transform to verbose/simple format => write to output
     *
     * @param in InputStream
     * @param out OutputStream
     * @param parser to parse input
     * @param verboseMode flag set to output extra match data
     */
    public static void runParserStream(InputStream in, OutputStream out,
                                       Parser<? extends ParserResult> parser, Boolean verboseMode) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        PrintWriter writer = new PrintWriter(out,true);

        //set output format based on verboseMode parameter
        Function<ParserResult, String> parserResultFormat =
                verboseMode ? ParserResult::verboseResult : ParserResult::shortResult;

        bufferedReader.lines()
            .map(parser::parse)
            .filter(ParserResult::isMatchFound)
            .map(parserResultFormat)
            .forEach(writer::println);
    }


}
