# Booking API Automation Framework  
End-to-end API test automation for **Restful Booker** using **Java + Cucumber + RestAssured**.

---

# 1. Project Overview
This framework tests all endpoints of the Restful Booker API:

- **Create Booking**
- **Get Booking**
- **Update Booking (PUT)**
- **Partial Update (PATCH)**
- **Delete Booking**
- **Get All Bookings**

It uses:

| Component | Purpose |
|----------|---------|
| **Cucumber** | BDD scenarios |
| **RestAssured** | HTTP requests |
| **Hamcrest** | Assertions |
| **Maven** | Build + dependency mgmt |
| **Scenario Context** | Saving dynamic variables (bookingid, token, etc.) |

---

# 2. Project Structure

```
src
 └── test
     ├── java
     │    └── com.booking.tests
     │         ├── steps
     │         │     └── BookingSteps.java
     │         └── core
     │               └── TokenClient.java
     ├── resources
     │     ├── features
     │     │      └── booking-tests
     │     │            ├── create-booking.feature
     │     │            ├── get-booking.feature
     │     │            ├── delete-booking.feature
     │     │            ├── patch-booking.feature
     │     │            └── list-bookings.feature
     │     ├── payloads
     │     │      └── booking_payload.json
     │     └── config.properties
     └── pom.xml
```

---

# 3. Configuration (`config.properties`)

```
base.url=https://restful-booker.herokuapp.com
username=admin
password=password123
```

---

# 4. Token Handling

### `TokenClient.java`
Handles authentication **using username/password from config file** (no hardcoding):

```java
package com.booking.tests.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TokenClient {

    private static String baseUrl;
    private static String username;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            Properties props = new Properties();
            props.load(fis);

            baseUrl = props.getProperty("base.url");
            username = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {

        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body.toString())
                .post(baseUrl + "/auth")
                .then()
                .extract()
                .path("token");
    }
}
```

---

# 5. Running Tests

### Run entire test suite:
```
mvn clean test
```

### Run only delete tests:
```
mvn test -Dcucumber.filter.tags="@delete"
```

---

# 6. Example Booking Payload (`booking_payload.json`)

```json
{
  "firstname": "defaultFirstName",
  "lastname": "defaultLastName",
  "totalprice": 123,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2020-01-01",
    "checkout": "2020-01-02"
  },
  "additionalneeds": "Breakfast"
}
```

---

# 7. Scenario Context (Saving Values Between Steps)

The framework supports saving values like:

```
Then I save "bookingid" from response as "savedBookingId"
```

The value then becomes available in all later steps.

---

# 8. Features Summary

### ✔ Create Booking  
Creates booking using a template or data table.

### ✔ Get Booking  
Uses saved booking ID or explicit ID.

### ✔ List Bookings  
GET /booking

### ✔ Patch Booking  
Partial update using saved ID.

### ✔ Delete Booking  
Deletes booking and verifies deletion.

---

# 9. Step Definitions – All Supported Steps

You can use:

```
Given I have valid credentials
Given I have booking template "booking_payload.json"
When I create a booking with template
Then I save "bookingid" from response as "savedBookingId"
When I delete booking with id "savedBookingId"
When I get booking with id "savedBookingId"
Then the booking response status code should be 404
```

Everything matches exactly with your `BookingSteps.java`.

---

# 10. Troubleshooting

| Issue | Reason | Fix |
|------|--------|-----|
| **403 Forbidden** | No token sent | Ensure `I have valid credentials` is in Background |
| **Step undefined** | Step text mismatch | Copy steps EXACTLY as defined in README |
| **booking.firstname = null** | You used wrong booking ID | Ensure you saved `"bookingid"` not `"booking.id"` |

---

# 11. Roadmap

- Add logging + reporting (Allure)
- Add environment switching (dev/qa/stg)
- Add test data generator

---

# 12. Credits
Created for automated testing of **Restful Booker** API using clean BDD practices.
