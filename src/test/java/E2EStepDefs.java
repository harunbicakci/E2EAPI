import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import models.PojoPersona;
import utils.ApiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.ApiUtils.*;

public class E2EStepDefs {

    private PojoPersona persona;
    private Response response;
    private Map<String, String> payload;

    @Given("a persona with username {string} and password {string}")
    public void aPersonaWithUsernameAndPassword(String username, String password) {
        persona = new PojoPersona(username, password);
    }

    @When("the persona is registered via the API")
    public void thePersonaIsRegisteredViaTheAPI() {
        // Use a Map for JSON mapping instead of String.format
        payload = createUserPayload();

        // Register User // createPayload() method creates a Map<String, String> payload
        response = ApiUtils.post(POST_USER_ENDPOINT, payload);
        persona.setUserId(response.jsonPath().getString("userID"));

        // Generate token using the same payload
        response = ApiUtils.post(GENERATE_TOKEN_ENDPOINT, payload);
        persona.setToken(response.jsonPath().getString("token"));

    }

    @Then("the persona receives a userId and token")
    public void thePersonaReceivesAUserIdAndToken() {
        assertThat("User ID should not be null" ,persona.getUserId(), notNullValue());
        assertThat("Token should not be null", persona.getToken(), notNullValue());
    }

    @And("the response status code is {int}")
    public void theResponseStatusCodeIs(int expectedStatusCode) {
        assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
    }

    @When("the persona orders a book with ISBN {string}")
    public void thePersonaOrdersABookWithISBN(String isbn1) {

        response = ApiUtils.getRequestSpec()
                .header("Authorization", "Bearer " + persona.getToken())
                .body(createBookPayload(isbn1))
                .post(POST_BOOKS_ENDPOINT);
    }

    @Then("the book is added to the persona's collection")
    public void theBookIsAddedToThePersonaSCollection() {
        assertThat(response.jsonPath().getString("books[0].isbn"), notNullValue());
    }

    @And("the collection has {int} book")
    public void theCollectionHasBook(int expectedSize) {
        Response userResponse = ApiUtils.getWithAuth(POST_USER_ENDPOINT + persona.getUserId(), persona.getToken());
        List<String> books = userResponse.jsonPath().getList("books.isbn");
        assertThat(books.size(), equalTo(expectedSize));
    }

    @And("the collection has {int} books")
    public void theCollectionHasBooks(int expectedSize) {
        Response userResponse = ApiUtils.getWithAuth(POST_USER_ENDPOINT + persona.getUserId(), persona.getToken());
        List<String> books = userResponse.jsonPath().getList("books.isbn");
        assertThat(books.size(), equalTo(expectedSize));
    }

    @When("the persona is dleted via the API")
    public void thePersonaIsDletedViaTheAPI() {
        response = ApiUtils.getRequestSpec()
                .header("Authorization", "Bearer " + persona.getToken())
                .delete(POST_USER_ENDPOINT + persona.getUserId());
    }

    @Then("the deletion is successful with status code {int}")
    public void theDeletionIsSuccessfulWithStatusCode(int expectedStatusCode) {
        assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
    }

    @And("the persona's collection is empty when queried")
    public void thePersonaSCollectionIsEmptyWhenQueried() {
        Response userResponse = ApiUtils.getWithAuth(POST_USER_ENDPOINT + persona.getUserId(), persona.getToken());
        assertThat(userResponse.getStatusCode(), equalTo(401));
    }
}
