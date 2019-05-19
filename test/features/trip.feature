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