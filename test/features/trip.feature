Feature: Trip API Endpoint

  Scenario: Add a new trip with two destinations
    Given the application is running
    And I am logged in as an admin user
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
    Then the status code received is 201


  Scenario: Delete a trip as the trip's owner
    Given the application is running
    And I am logged in
    And I own the trip with the following data
      """
        {
          "trip_name": "Test Adventure",
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
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the status code received is 200


  Scenario: Delete other user's trip as an admin
    Given the application is running
    And I am logged in as an admin user
    And I do not own the trip with the following data
      """
        {
          "trip_name": "Test Adventure",
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
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the status code received is 200


  Scenario: Delete a another user's trip as a standard user
    Given the application is running
    And I am logged in
    And I do not own the trip with the following data
      """
        {
          "trip_name": "Test Adventure",
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
    When I delete the trip with the following name
      | Name            |
      | Test Adventure  |
    Then the status code received is 403


  Scenario: Attempt to add a trip with one destination
    Given the application is running
    And I am logged in
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
    Then the status code received is 400


  Scenario: Attempt to add a trip with no name
    Given the application is running
    And I am logged in
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
    Then the status code received is 400


  Scenario: Attempt to add a trip with duplicate destinations in series
    Given the application is running
    And I am logged in
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
    Then the status code received is 400


  Scenario: Attempt to add a trip with inappropriately ordered dates
    Given the application is running
    And I am logged in
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
    Then the status code received is 400


  Scenario: Edit a trip as the trip's owner with valid name
    Given the application is running
    And I am logged in
    And I own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "Adventure",
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
    Then the status code received is 200


  Scenario: Edit a trip as the trip's owner with valid destinations
    Given the application is running
    And I am logged in
    And I own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "Edit Adventure",
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
            },
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the status code received is 200


  Scenario: Edit a trip as the trip's owner with invalid name
    Given the application is running
    And I am logged in
    And I own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "",
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
    Then the status code received is 400


  Scenario: Edit a trip as the trip's owner with invalid destinations
    Given the application is running
    And I am logged in
    And I own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    Then the status code received is 400


  Scenario: Edit other user's trip as an admin
    Given the application is running
    And I am logged in as an admin user
    And I do not own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "Edit Adventure",
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
            },
            {
              "destination_id" : "1155",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    Then the status code received is 200


  Scenario: Edit a another user's trip as a standard user
    Given the application is running
    And I am logged in
    And I do not own the trip with the following data
      """
        {
          "trip_name": "Edit Adventure",
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
    When I change the trip, "Edit Adventure" to contain the following data
      """
        {
          "trip_name": "Adventure",
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
    Then the status code received is 403


  Scenario: Changing ownership of public destination not owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 119 exists
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "119",
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
    Then the destination with id 119 ownership changes to the user with id 1


  Scenario: Changing ownership of private destination owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 325 exists
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "325",
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
    Then the destination with id 325 ownership changes to the user with id 2


  Scenario: Changing ownership of public destination owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 567 exists
    When the following json containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "567",
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
    Then the destination with id 567 ownership changes to the user with id 2