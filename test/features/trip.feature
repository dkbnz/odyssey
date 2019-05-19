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


  Scenario: Attempt to add a trip with one destination
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "Unplanned bottle store excursion",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the received status code corresponds with a BadRequest response


  Scenario: Attempt to add a trip with no name
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent:
      """
        {
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
    Then the received status code corresponds with a BadRequest response


  Scenario: Attempt to add a trip with duplicate destinations in series
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "There and there again.",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            },
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the received status code corresponds with a BadRequest response


  Scenario: Attempt to add a trip with inappropriately ordered dates
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : "1993-12-12",
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
    Then the received status code corresponds with a BadRequest response


