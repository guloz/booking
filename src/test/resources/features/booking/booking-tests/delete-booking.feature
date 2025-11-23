@booking @api @delete
Feature: Delete Booking

  Background:
    Given I have valid credentials
    And I create a booking with template "booking_payload.json"
    Then I save "bookingid" from response as "savedBookingId"

  Scenario: Delete an existing booking
    When I delete booking with id "savedBookingId"
    Then the booking response status code should be 201
    When I get booking with id "savedBookingId"
    Then the booking response status code should be 404

  Scenario: Delete a non-existing booking
    When I delete booking with id "999999"
    Then the booking response status code should be 405
