@booking @api @patch
Feature: Patch Booking

  Scenario Outline: Partially update an existing booking
    Given I have valid credentials
    And I have an existing booking with id "<bookingId>"
    When I partially update the booking firstname to "<partialFirstname>"
    Then the booking response status code should be <status_code>

    When I get booking with id "<bookingId>"
    Then the booking response status code should be <status_code>
    Then the booking firstname should be '<partialFirstname>'

    Examples:
      | status_code | bookingId | partialFirstname |
      | 200         | 2377      | Bob              |
