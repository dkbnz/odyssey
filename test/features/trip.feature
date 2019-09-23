Feature: Trip API Endpoint

  Scenario: Successfully add a new trip with two destinations
    Given the application is running
    And I am logged in as an admin user
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the status code received is 201


  Scenario: Unsuccessfully creating a trip for a user that non-existent user
    Given the application is running
    And I am logged in as an admin user
    When I create the trip with the following values for user with id 500
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the status code received is 400


  Scenario: Unsuccessfully creating a trip for another user when not admin
    Given the application is running
    And I am logged in
    When I create the trip with the following values for user with id 1
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the status code received is 403


  Scenario: Successfully delete a trip as the trip's owner
    Given the application is running
    And I am logged in
    And the trip exists with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    When I delete the trip
    Then the status code received is 200


  Scenario: Successfully delete other user's trip as an admin
    Given the application is running
    And I am logged in as an admin user
    And the trip exists with the following values for user with id 3
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And the trip exists
    When I delete the trip
    Then the status code received is 200


  Scenario: Unsuccessfully delete a trip when not logged in
    Given the application is running
    And I am logged in
    And the trip exists with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    And I am not logged in
    When I delete the trip
    Then the status code received is 401


  Scenario: Unsuccessfully delete a another user's trip as a standard user
    Given the application is running
    And I am logged in
    And the trip exists with the following values for user with id 3
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And the trip exists
    When I delete the trip
    Then the status code received is 403


  Scenario: Unsuccessfully attempt to add a trip with one destination
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And I create the trip
    Then the status code received is 400


  Scenario: Unsuccessfully attempt to add a trip with no name
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the status code received is 400


  Scenario: Unsuccessfully attempt to add a trip with duplicate destinations in series
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And I create the trip
    Then the status code received is 400


  Scenario: Unsuccessfully attempt to add a trip with inappropriately ordered dates
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        | 2019-08-25 | 2019-08-23 |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the status code received is 400


  Scenario: Edit a trip as the trip's owner with valid name
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    When I change the trip to the following trip
      | Name       |
      | Adventure  |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I update the trip
    Then the status code received is 200


  Scenario: Edit a trip as the trip's owner with valid destinations
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    When I change the trip to the following trip
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 567         |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And I update the trip
    Then the status code received is 200


  Scenario: Unsuccessfully edit a trip when not logged in
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    And I am not logged in
    When I change the trip to the following trip
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 567         |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And I update the trip
    Then the status code received is 401


  Scenario: Edit a trip as the trip's owner with invalid name
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    When I change the trip to the following trip
      | Name       |
      |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 567         |            |            |
    And I update the trip
    Then the status code received is 400


  Scenario: Edit a trip as the trip's owner with invalid destinations
    Given the application is running
    And I am logged in
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    When I change the trip to the following trip
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And I update the trip
    Then the status code received is 400


  Scenario: Edit other user's trip as an admin
    Given the application is running
    And I am logged in as an admin user
    When the trip exists with the following values for user with id 3
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And the trip exists
    When I change the trip to the following trip
      | Name       |
      | Adventure  |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 567         |            |            |
    And I update the trip
    Then the status code received is 200


  Scenario: Unsuccessfully edit a another user's trip as a standard user
    Given the application is running
    And I am logged in
    When the trip exists with the following values for user with id 3
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And the trip exists
    When I change the trip to the following trip
      | Name       |
      | Adventure  |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 1155        |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 567         |            |            |
    And I update the trip
    Then the status code received is 403


  Scenario: Changing ownership of public destination not owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 119 exists
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 119         |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the destination with id 119 ownership changes to the user with id 1


  Scenario: Changing ownership of private destination owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 325 exists
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 325         |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the destination with id 325 ownership changes to the user with id 2


  Scenario: Changing ownership of public destination owned by me when used in a trip
    Given the application is running
    And I am logged in
    And the destination with id 567 exists
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date   |
      | 119         |            |            |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    And I create the trip
    Then the destination with id 567 ownership changes to the user with id 2


  Scenario: Successfully fetching all trips for a specified user
    Given the application is running
    And I am logged in
    When I request all trips for user with id 3
    Then the status code received is 200


  Scenario: Unsuccessfully fetching all trips when not logged in
    Given the application is running
    When I request all trips for user with id 3
    Then the status code received is 401


  Scenario: Successfully fetching the number of trips for a specified user
    Given the application is running
    And I am logged in
    When I request the number of trips for user with id 3
    Then the status code received is 200


  Scenario: Unsuccessfully fetching the number of trips for a specified user when not logged in
    Given the application is running
    When I request the number of trips for user with id 3
    Then the status code received is 401
