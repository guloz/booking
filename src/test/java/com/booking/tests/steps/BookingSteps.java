package com.booking.tests.steps;

import com.booking.tests.core.TokenClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingSteps {

    private static String baseUrl;
    private Response response;
    private JSONObject payload;
    private boolean sendAuthToken = true;
    private RequestSpecification request;

    // Scenario context for saving variables
    private Map<String, Object> scenarioContext = new HashMap<>();

    // Public zero-arg constructor for Cucumber
    public BookingSteps() {
    }

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            baseUrl = prop.getProperty("base.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void resetAuthFlag() {
        sendAuthToken = true;
    }

    // --------------------------
    // JSON Template Handling
    // --------------------------
    private void loadTemplate(String templatePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(templatePath)));
        payload = new JSONObject(content);
    }

    private void updatePayload(Map<String, String> data) {
        for (String key : data.keySet()) {
            String value = data.get(key);

            if (key.equals("checkin") || key.equals("checkout")) {
                if (!payload.has("bookingdates")) {
                    payload.put("bookingdates", new JSONObject());
                }
                payload.getJSONObject("bookingdates").put(key, value);
            } else if (key.equals("totalprice")) {
                payload.put(key, Integer.parseInt(value));
            } else if (key.equals("depositpaid")) {
                payload.put(key, Boolean.parseBoolean(value));
            } else {
                payload.put(key, value);
            }
        }
    }

    // --------------------------
    // Request Helper
    // --------------------------
    private RequestSpecification givenRequest() {
        request = RestAssured.given().contentType(ContentType.JSON);

        if (sendAuthToken) {
            String token = (String) scenarioContext.get("authToken");
            request.header("Cookie", "token=" + token);
        }

        return request;
    }

    // --------------------------
    // Steps
    // --------------------------
    @Given("I have valid credentials")
    public void i_have_valid_credentials() {
        String token = new TokenClient().getToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is missing! Cannot continue tests.");
        }
        scenarioContext.put("authToken", token); // save for later use
    }

    @Given("I create a booking with template {string}")
    public void i_create_a_booking_with_template_string(String template) throws IOException {
        i_have_booking_template(template);
        i_create_a_booking_with_template();
    }

    @Given("I have booking template {string}")
    public void i_have_booking_template(String template) throws IOException {
        loadTemplate("src/test/resources/payloads/" + template);
    }

    @And("I remove the authorization header")
    public void i_remove_the_authorization_header() {
        sendAuthToken = false;
    }

    @When("I create a booking with template")
    public void i_create_a_booking_with_template() {
        response = givenRequest()
                .body(payload.toString())
                .post(baseUrl + "/booking");
        response.then().log().all(true);
    }

    @When("I create a booking with data")
    public void i_create_a_booking_with_data(io.cucumber.datatable.DataTable table) {
        Map<String, String> data = table.asMap(String.class, String.class);
        updatePayload(data);

        response = givenRequest()
                .body(payload.toString())
                .post(baseUrl + "/booking");
        response.then().log().all(true);
    }

    @When("I send invalid create booking request with data")
    public void i_send_invalid_create_booking_request_with_data(io.cucumber.datatable.DataTable table) {
        Map<String, String> data = table.asMap(String.class, String.class);
        updatePayload(data);

        response = givenRequest()
                .body(payload.toString())
                .post(baseUrl + "/booking");
        response.then().log().all(true);
    }

    @When("I get booking with id {int}")
    public void i_get_booking_with_id(int bookingId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get(baseUrl + "/booking/" + bookingId);
        response.then().log().all(true);
    }

    @When("I get booking with id {string}")
    public void i_get_booking_with_id_string(String idOrVariable) {
        int bookingId;
        if (scenarioContext.containsKey(idOrVariable)) {
            bookingId = (Integer) scenarioContext.get(idOrVariable);
        } else {
            bookingId = Integer.parseInt(idOrVariable);
        }
        i_get_booking_with_id(bookingId);
    }

    @When("I delete booking with id {int}")
    public void i_delete_booking_with_id(int bookingId) {
        response = givenRequest()
                .delete(baseUrl + "/booking/" + bookingId);
        response.then().log().all(true);
    }

    @When("I delete booking with id {string}")
    public void i_delete_booking_with_id_string(String idOrVariable) {
        int bookingId;
        if (scenarioContext.containsKey(idOrVariable)) {
            bookingId = (Integer) scenarioContext.get(idOrVariable);
        } else {
            bookingId = Integer.parseInt(idOrVariable);
        }
        i_delete_booking_with_id(bookingId);
    }

    @Then("the booking response status code should be {int}")
    public void the_booking_response_status_code_should_be(int code) {
        assertThat(response.getStatusCode(), equalTo(code));
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
    }

    @Then("the client validation status code should be {int}")
    public void the_client_validation_status_code_should_be(int code) {
        assertThat(response.getStatusCode(), equalTo(code));
    }

    @Then("the booking firstname should be {string}")
    public void the_booking_firstname_should_be(String expectedFirstname) {
        String actualFirstname;

        // First try the nested "booking.firstname"
        if (response.jsonPath().getMap("").containsKey("booking")) {
            actualFirstname = response.jsonPath().getString("booking.firstname");
        } else {
            actualFirstname = response.jsonPath().getString("firstname");
        }

        assertThat(actualFirstname, equalTo(expectedFirstname));
    }

    @Then("I save {string} from response as {string}")
    public void i_save_field_from_response(String field, String variableName) {
        Object value = response.jsonPath().get(field);
        scenarioContext.put(variableName, value);
        System.out.println("Saved " + variableName + ": " + value);
    }

    @Given("I have multiple bookings created from templates")
    public void i_have_multiple_bookings_created_from_templates(io.cucumber.datatable.DataTable table) throws IOException {
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            String template = row.get("template");
            i_have_booking_template(template);
            i_create_a_booking_with_template();
            the_booking_response_status_code_should_be(200);
        }
    }
    @When("I request all bookings")
    public void i_request_all_bookings() {
        // Use token if required
        RequestSpecification req = RestAssured.given().contentType(ContentType.JSON);
        if (scenarioContext.containsKey("authToken")) {
            req.header("Cookie", "token=" + scenarioContext.get("authToken"));
        }

        response = req.get(baseUrl + "/booking");
        response.then().log().all(true);
    }
    @When("I request booking with id {string}")
    public void i_request_booking_with_id(String bookingIdStr) {
        int bookingId;

        // If the step references a saved variable from previous steps
        if (scenarioContext.containsKey(bookingIdStr)) {
            bookingId = (Integer) scenarioContext.get(bookingIdStr);
        } else {
            bookingId = Integer.parseInt(bookingIdStr);
        }

        RequestSpecification req = RestAssured.given().contentType(ContentType.JSON);

        // Add token if you need auth
        if (scenarioContext.containsKey("authToken")) {
            req.header("Cookie", "token=" + scenarioContext.get("authToken"));
        }

        response = req.get(baseUrl + "/booking/" + bookingId);
        response.then().log().all(true);
    }

}
