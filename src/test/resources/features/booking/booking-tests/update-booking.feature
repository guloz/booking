@booking @api @update
Feature: Update Booking

  Scenario Outline: Update an existing booking
    Given I have valid credentials
    And I have an existing booking with id "<bookingId>"
    When I update the booking with firstname "<newFirstname>" and lastname "<newLastname>"
    And total price "<newTotalPrice>"
    And deposit paid "<newDepositPaid>"
    And checkin "<newCheckin>" and checkout "<newCheckout>"
    And additional needs "<newAdditionalNeeds>"
    Then the booking response status code should be <status_code>
    Then the booking firstname should be '<newFirstname>'

    Examples:
      | status_code | bookingId | newFirstname | newLastname | newTotalPrice | newDepositPaid | newCheckin | newCheckout | newAdditionalNeeds |
      | 200         | 2377      | Alice        | Smith       | 300           | true           | 2021-03-15 | 2021-03-20  | Dinner             |


