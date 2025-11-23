@booking @api @get
Feature: Get Booking

  Background:
    Given I have valid credentials
    And I have multiple bookings created from templates
      | template                    |
      | booking_payload.json        |
      | second_booking_payload.json |
    Then I save "bookingid" from response as "savedBookingId"


  Scenario: Get all bookings
    Given I have valid credentials
    When I request all bookings
    Then the response status code should be 200

  Scenario Outline: Get booking by ID
    Given I have valid credentials
    When I get booking with id "savedBookingId"
    Then the booking response status code should be <status_code>
    Then the booking firstname should be '<firstname>'

    Examples:
      | status_code | firstname         |
      | 200         | defaultSecondName |
