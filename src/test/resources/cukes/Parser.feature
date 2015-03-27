#
# Run feature file as part of mvn test cycle
# or using RunParserIntegrationTest class
#

Feature: Parser correctly loads Specification and uses to match relevant data from input

  Scenario Outline: Process matching input
    Given a parser using specification <specification>
    When the parser parses <input>
    Then the result match is <match_result> with captured data <captured_data>

  Examples:
    | specification | input | match_result | captured_data |
    | foo %{0} is a %{1} | foo blah is a bar | true | blah,bar |
    | foo %{0} is a %{1} | foo blah boz is a very big boat | true | blah boz,very big boat |
    | foo %{0} is a %{1S0}| foo blah is a bar | true | blah,bar |
    | the %{1} %{0S0} | the big brown fox ran away         | true | big brown fox ran,away |
    | the %{0S1} %{1} away | the big brown fox ran away         | true | big brown,fox ran |
    | the %{0S2} %{1} away | the big bad brown fox ran away     | true | big bad brown,fox rcd an |
    | the %{0S3} %{1} away | the big big bad brown fox ran away | true | big big bad brown,fox ran |
    | bar %{0G} foo %{1} | bar foo bar foo bar foo bar foo  | true | foo bar foo bar,bar foo |
    | bar %{0} foo %{1} | bar foo bar foo bar foo bar foo  | true | foo bar,bar foo bar foo |
    | ba]r %{0} fo<<<...>>>o %{1} | ba]r fo[[o bar fo<<<...>>>o bar foo bar foo  | true | fo[[o bar,bar foo bar foo |
    | %{1} jumps over ^[[[+// %{0} fence | big brown fox jumps over ^[[[+// the fence  | true | big brown fox,the |

  Scenario Outline: Skip non-matching input
    Given a parser using specification <specification>
    When the parser parses <input>
    Then the result match is <match_result> with no captured data

  Examples:
    | specification | input | match_result |
    | foo %{0} is a %{1S0}| foo blah is a very big boat | false |
    | foo %{0} is a %{1S0}| foo blah is bar | false |
    | foo %{0} is a %{1S0}| foo blah | false |
    | foo %{0} is a %{1S0}| foo blah is | false |

  Scenario: Parser Application parses data from input stream and publishes correct matches to
  output stream as verbose output
    Given a parser using specification foo %{0} is a %{1} hidden
    And verbose mode is true
    And the following data as input
      | hidden in some text foo blah is a bar hidden in some text |
      | un matched test text                                         |
      | hidden in some text foo blah1 is a bar1 hidden in some text foo blah2 is a bar2 hidden in asdd |
      | un matched test text                                         |
      | hidden in some text foo blah is a very big boat hidden in some text |
      | hidden in some text foo [<>.] is a very big boat hidden in some text |
    When the parser processes the input data
    Then the following output is produced
      | hidden in some text foo blah is a bar hidden in some text |
      | %{0} - 'blah' |
      | %{1} - 'bar' |
      |  |
      | hidden in some text foo blah1 is a bar1 hidden in some text foo blah2 is a bar2 hidden in asdd |
      | %{0} - 'blah1' |
      | %{1} - 'bar1' |
      | %{0} - 'blah2' |
      | %{1} - 'bar2' |
      |  |
      | hidden in some text foo blah is a very big boat hidden in some text |
      | %{0} - 'blah' |
      | %{1} - 'very big boat' |
      |  |
      | hidden in some text foo [<>.] is a very big boat hidden in some text |
      | %{0} - '[<>.]' |
      | %{1} - 'very big boat' |


  Scenario: Parser Application parses data from input stream and publishes correct matches to
  output stream short output
    Given a parser using specification foo %{0} is a %{1} hidden
    And verbose mode is false
    And the following data as input
      | hidden in some text foo blah is a bar hidden in some text |
      | un matched test text                                         |
      | hidden in some text foo blah1 is a bar1 hidden in some text foo blah2 is a bar2 hidden in asdd |
      | un matched test text                                         |
      | un ]]..*[[ test text                                         |
      | hidden in some text foo blah is a very big boat hidden in some text |
      | hidden in some text foo [<>.] is a very big boat hidden in some text |
    When the parser processes the input data
    Then the following output is produced
      | hidden in some text foo blah is a bar hidden in some text |
      | hidden in some text foo blah1 is a bar1 hidden in some text foo blah2 is a bar2 hidden in asdd |
      | hidden in some text foo blah is a very big boat hidden in some text |
      | hidden in some text foo [<>.] is a very big boat hidden in some text |
