@booking @api @client-validation
Feature: Delete Booking

  Scenario Outline: Invalid create booking requests returns HTTP 400
    Given I have booking template "booking_payload.json"
    When I send invalid create booking request with data
      | firstname | <firstname> |
      | lastname  | <lastname>  |
    Then the client validation status code should be <statusCode>

    Examples:
      | firstname | lastname | statusCode |
      |           | Doe      | 400        |
      | John      |          | 400        |
      | null      | null     | 400        |


  Scenario: Missing Authorisation header returns HTTP 401
    Given I have booking template "booking_payload.json"
    And I remove the authorization header
    When I create a booking with template
    Then the booking response status code should be 401
