package com.ninecy.parser;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import com.ninecy.app.ParserApp;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static com.ninecy.regex.RegexPatterns.TEMPLATE;

/**
 * User: Richard
 * Date: 21.03.15
 *
 * ParserIntegrationTest class, methods linked to cucumber
 * using regex in annotations.
 * A new ParserIntegrationTest instance is created for each test run in feature file
 * to counteract the horrible mutable state needed between methods.
 *
 */
public class ParserIntegrationTest {
    private Parser<RegexParserResult> regexParser;
    private RegexParserResult regexParserResult;
    private Boolean verboseMode;
    private List<String> parserOutput;
    private List<String> parserInput;

    @Given("^a parser using specification (.*?)$")
    public void a_parser_using_specification(String specification) throws Throwable {
        regexParser = new RegexParser(new RegexPatternBuilder(TEMPLATE.getPattern())
                                    .build(specification));

    }

    @When("^the parser parses (.*?)$")
    public void the_parser_parses(String input) throws Throwable {
        regexParserResult = regexParser.parse(input);
    }

    @Then("^the result match is (.*?) with captured data (.*?)$")
    public void the_result_match_is_with_captured_data(Boolean match, String capturedFields) throws Throwable {
        assertEquals(match, regexParserResult.isMatchFound());
        assertEquals(Arrays.asList(capturedFields.split(",")), regexParserResult.getMatchResults());

    }

    @Then("^the result match is (.*?) with no captured data$")
    public void the_result_match_is_with_captured_data(Boolean match) throws Throwable {
        assertEquals(match, regexParserResult.isMatchFound());
        assertEquals(0, regexParserResult.getMatchResults().size());

    }


    @Given("^verbose mode is (.*)$")
    public void verbose_mode_is(Boolean isVerbose) throws Throwable {
            this.verboseMode = isVerbose;
    }

    @Given("^the following data as input$")
    public void the_following_data_as_input(DataTable inputData) throws Throwable {
        parserInput = inputData.asList(String.class);
    }

    @When("^the parser processes the input data$")
    public void the_parser_processes_the_input_data() throws Throwable {


        //load input data from feature table into input stream
        try (ByteArrayOutputStream inBaos = new ByteArrayOutputStream()) {
            for (String line : parserInput) {
                inBaos.write((line + "\n").getBytes());
            }
            InputStream inputStream = new ByteArrayInputStream(inBaos.toByteArray());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ParserApp.runParserStream(inputStream, outputStream, regexParser, verboseMode);
            parserOutput = Arrays.asList(outputStream.toString(UTF_8.name()).split("\n"));
        } catch (IOException e) {
            System.err.println(String.format("Error processing input data"));
            //throw to fail test
            throw e;
        }

    }

    @Then("^the following output is produced$")
    public void the_following_output_is_produced(DataTable outputData) throws Throwable {
        assertEquals(outputData.asList(String.class),parserOutput);
    }

}
