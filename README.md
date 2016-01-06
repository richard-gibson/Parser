# Parser Application
## Deliverables Source code
Required to run compiled code JDK/JRE 1.8+
mvn clean install
To obtain compiled program untar Parser-1.0-bundle.tar.gz from target and navigate to Parser-1.0 directory
program can be run using wrapper shell script
Usage: ./runParser.sh
-d optional flag to produce verbose output if set -s pattern specification parameter
example
echo "big brown fox jumps over the lazy dog" | ./runParser.sh -d -s "%{0} jumps over %{1}"

## Testing
To run Parser tests from src directory use command
mvn test
### Tests created
Unit tests created to check Parser and PatternBuilder basic functionality and exception handling
Integration tests using Parser and PatternBuilder Classes together
<pre>
Feature: Parser correctly loads Specification and uses to match relevant data from input

  Scenario Outline: Process matching input
    Given a parser using specification <specification>
    When the parser parses <input>
    Then the result match is <match_result> with captured data <captured_data>

  Scenario Outline: Skip non-matching input
    Given a parser using specification <specification>
    When the parser parses <input>
    Then the result match is <match_result> with no captured data

  Scenario: Parser Application parses data from input stream and publishes correct matches to
    output stream as verbose output
      Given a parser using specification foo %{0} is a %{1} hidden
      And verbose mode is true
      And the following data as input


</pre>
## Possible Improvements
* Use a separate set of regex that do not capture tokens when '-d' flag not set, currently '-d' flag only controls output formatting. Instead different implementations
of PatternBuilder and ParserResult could be used dependent on whether flag is set to use more efficient regex that don't capture tokens.
* Hand off processing of each line to separate threads, this would affect the order of output but is a simple change as processing code is already threadsafe due to following reasons:
* Parser instances are immutable
* For pipeline to process a line
         parse => filter positive => transform to verbose/simple format
each function is carried out with out side effects.
* Writing to OutputStream is synchronized, so writer can be shared between threads.
