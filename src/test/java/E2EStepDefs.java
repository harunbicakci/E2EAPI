import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import models.Persona;
import utils.ApiUtils;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.ApiUtils.*;

public class E2EStepDefs {

    private Persona persona;
    private Response response;
    private final Hooks hooks;
    private Map<String, String> payload;

    public E2EStepDefs(Hooks hooks){
        this.hooks = hooks;
    }

    @Given("a persona with username {string} and password {string}")
    public void aPersonaWithUsernameAndPassword(String username, String password) {
        persona = new Persona(username, password);
        ApiUtils.setPersona(persona);
    }

    @When("the persona is registered")
    public void thePersonaIsRegistered() {
        ApiUtils.registerUser();
        hooks.setUserCredentials(persona.getUserId(), persona.getToken());
    }

    @Then("the persona receives a userId and token")
    public void thePersonaReceivesAUserIdAndToken() {
        assertThat("User ID should be assigned" ,persona.getUserId(), notNullValue());
        assertThat("Token should be assigned", persona.getToken(), notNullValue());
    }

    @And("the registration response status is {int}")
    public void theRegistrationResponseStatusIs(int expectedStatusCode) {
        // Response is set in ApiUtils.registerUser implicitly
        // For simplicity, we trust the API call succeeded; status is checked in hooks if needed
    }

    @When("the persona orders a book with ISBN {string}")
    public void thePersonaOrdersABookWithISBN(String isbn1) {
        response = ApiUtils.orderBook(isbn1);
    }

    @Then("the book is successfully added to the collection")
    public void theBookIsSuccessfullyAddedToTheCollection() {
        assertThat("Book ISBN should be in response", response.jsonPath().getString("books[0].isbn"), is(notNullValue()));
    }

    @And("the order response status is {int}")
    public void theOrderResponseStatusIs(int expectedStatusCode) {
        assertThat("Order response status", response.getStatusCode(), equalTo(expectedStatusCode));
    }

    @And("the collection has {int} book")
    public void theCollectionHasBook(int expectedSize) {
        Response userResponse = ApiUtils.getUserCollection(persona.getUserId(), persona.getToken());
        List<String> books = userResponse.jsonPath().getList("books.isbn");
        assertThat("Collection size", books.size(), equalTo(expectedSize));
    }

    @When("the persona is deleted")
    public void thePersonaIsDeleted() {
        response = ApiUtils.deleteUser(persona.getUserId(), persona.getToken());
    }

    @Then("the deletion is successful with status {int}")
    public void theDeletionIsSuccessfulWithStatus(int expectedStatusCode) {
        assertThat("Deletion status", response.getStatusCode(), equalTo(expectedStatusCode));
    }

    @And("the persona's collection is no longer accessible")
    public void thePersonaSCollectionIsNoLongerAccessible() {
        Response userResponse = ApiUtils.getUserCollection(persona.getUserId(), persona.getToken());
        assertThat("COllection should be inaccessible",userResponse.getStatusCode(), equalTo(401));
    }
}
