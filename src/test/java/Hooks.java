import utils.ApiUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private String userId;
    private String token;

    @Before("@E2E")
    public void beforeScenario(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        userId = null;
        token = null;
    }

    public void setUserCredentials(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    @After("@E2E")
    public void afterScenario(Scenario scenario) {
        logger.info("Completed scenario: {}", scenario.getName());

        if (userId != null && token != null) {
            try {
                Response response = ApiUtils.deleteUser(userId, token);
                if (response.getStatusCode() == 204) {
                    logger.info("Cleanup successful: User {} deleted.", userId);
                } else {
                    logger.warn("Cleanup failed for user {}: Status code {}", userId, response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error during cleanup for user {}: {}", userId, e.getMessage());
            }
        }

        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }
    }
}