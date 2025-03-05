import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/E2E.feature",
        glue = "E2EStepDefs",
        plugin = {"pretty", "html:target/cucumber-e2e-reports.html"},
        tags = "@E2E"
)
public class TestRunner {
}
