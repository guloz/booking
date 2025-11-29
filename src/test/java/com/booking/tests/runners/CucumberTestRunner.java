package com.booking.tests.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",   // path to your feature files
        glue = {"com.booking.tests.steps"},         // your step definitions package
        plugin = {
                "pretty",
                "json:target/cucumber.json",
                "html:target/cucumber-report.html"
        },
        monochrome = true
)
public class CucumberTestRunner {
}
