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
    Then the received status code corresponds with a Created response
