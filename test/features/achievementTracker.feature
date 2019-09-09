Feature: Achievement Tracker API Endpoint
  
  Scenario: Viewing current point value
    Given the application is running
    And I am logged in
    And I have some starting points
    When I try to view my points
    Then I am given my point total


  Scenario: Try to view points when not logged in
    Given the application is running
    And I am not logged in
    When I try to view another user's points value
    Then the status code received is 401


  Scenario: View another user's points
    Given the application is running
    And I am logged in
    When I try to view another user's points value
    Then I am given their total number of points


  Scenario: Solving a Quest riddle
    Given the application is running
    And I am logged in
    And I have some starting points
    When I solve the current riddle for a Quest
    Then I have gained points


  Scenario: Incorrectly solving a quest riddle
    Given the application is running
    And I am logged in
    And I have some starting points
    When I incorrectly guess the answer to a quest riddle
    Then I have not gained points


  Scenario: Checking in to a quest objective
    Given the application is running
    And I am logged in
    And I have some starting points
    When I check into a destination
    Then I have gained points


  Scenario: Checking in to an objective that hasn't been solved
    Given the application is running
    And I am logged in
    And I have some starting points
    When I check in for quest attempt 4
    Then the status code received is 403
    And I have not gained points


  Scenario: Creating a destination
    Given the application is running
    And I am logged in
    And I have some starting points
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then I have gained points


  Scenario: Creating my first destination and getting a badge
    Given the application is running
    And I am logged in
    And I currently have no destinations created
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then I gain the "Cartographer" badge with level 1


  Scenario: Creating my first trip and getting a badge
    Given the application is running
    And I am logged in
    And I currently have no trips created
    When I create a new trip with the following values
      | Name       |
      | First Trip |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 1155        |            |          |
    And the trip has a destination with the following values
      | Destination | Start Date | End Date |
      | 567         |            |          |
    Then I gain the "Planner" badge with level 1


  Scenario: Creating my first quest and getting a badge
    Given the application is running
    And I am logged in
    And I currently have no quests created
    When I start to create a quest using the following values
      | Title       | Start Date | End Date  |
      | Cool Quest  |            |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes some stuff?                | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    Then the status code received is 201
    And I gain the "Writer" badge with level 1


  Scenario: Solving my first quest and getting a badge
    Given the application is running
    And I am logged in
    And a quest exists with id 2
    When I check in for quest attempt 7
    Then the status code received is 200
    And I receive a valid quest attempt in the response
    And I have completed the quest
    And I gain the "Solver" badge with level 1


  Scenario: Gaining enough points to achieve first level Overachiever
    Given the application is running
    And I am logged in as user with username "testuser5@email.com" and id "7"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 1


  Scenario: Gaining enough points to achieve second level Overachiever
    Given the application is running
    And I am logged in as user with username "testuser6@email.com" and id "8"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 2


  Scenario: Gaining enough points to achieve third level Overachiever
    Given the application is running
    And I am logged in as user with username "testuser7@email.com" and id "9"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 3


  Scenario: Getting the first level Streaker badge
    Given the application is running
    And The following profile does not exist with the username "TestUser123@travelea.com" within the TravelEA database
    When A user attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | TestUser123@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code received is 201
    And I gain the "Streaker" badge with level 1
