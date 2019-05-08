Feature: Trip API Endpoint

  Scenario: Add a new trip
    Given The state of the application is that it is running
    And I am logged into the application which is running
    When the following json containing a trip is sent
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1",
              "start_date" : "06/05/2019",
              "end_date" : "06/06/2019"
            },
            {
              "destination_id" : "2",
              "start_date" : "06/06/2019",
              "end_date" : "06/07/2019"
            }
          ]
        }
      """
    Then the received status code corresponds with a Created response
