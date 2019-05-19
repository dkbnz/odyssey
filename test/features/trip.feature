Feature: Trip API Endpoint

  Scenario: Add a new trip with two destinations
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            },
            {
              "destination_id" : "567",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the received status code corresponds with a Created response

  Scenario: Delete a trip as the trip's owner
    Given I have a running application
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
    Given I have a running application
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
    Given I have a running application
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
