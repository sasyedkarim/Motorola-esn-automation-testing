package co.uk.motorola.esn;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber", "junit:target/junit_cucumber.xml", "json:target/cucumber.json"},
        features = "@src/test/resources/1.txt",
        tags = ("@vansmoketestwj"),
        monochrome = true,
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class RunCukesSequence {
}
