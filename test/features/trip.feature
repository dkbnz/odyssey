Feature: Trip API Endpoint

  Scenario: Add a new trip with two destinations
    Given I have an application running
    And I am logged in with credentials
      | Username                | Password  |
      | admin@travelea.com      | admin1    |
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : "1990-12-12",
              "end_date" : "1991-12-12"
            },
            {
              "destination_id" : "567",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the response status code is Created

  Scenario: Delete a trip as the trip's owner
    Given I have an application running
    And I am logged in with credentials
      | Username                | Password  |
      | guestUser@travelea.com  | guest123  |
    And I own the trip with the following name
      | Name            |
      | Test Adventure  |
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the response status code is OK

  Scenario: Delete other user's trip as an admin
    Given I have an application running
    And I am logged in with credentials
      | Username                | Password  |
      | admin@travelea.com      | admin1    |
    And I do not own the trip with the following name
      | Name            |
      | Test Adventure  |
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the response status code is OK

  Scenario: Delete a another user's trip as a standard user
    Given I have an application running
    And I am logged in with credentials
      | Username                | Password  |
      | guestUser@travelea.com  | guest123  |
    And I do not own the trip with the following name
      | Name            |
      | Test Adventure  |
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the response status code is Unauthorised
