import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Class setup to run cucumber features
 * will check all classes in com.ninecy.parse test package
 * for methods with regex in Given/When/Then annotations that
 * match the feature file.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        glue = { "com.ninecy.parser"},
        features = {"classpath:cukes/Parser.feature"})
public class RunParserIntegrationTest {}

