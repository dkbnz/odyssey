Feature: Achievement Tracker API Endpoint

#Points
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


#Awarding Points
  Scenario: Creating a destination awards points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then I have gained points


  Scenario: Creating a quest awards points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I start to create a quest using the following values
      | Title      | Start Date | End Date |
      | Cool Quest |            |          |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                  | Radius |
      | 119         | What rhymes some stuff? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    Then I have gained points


  Scenario: Solving a quest objective awards points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I solve the current riddle for a Quest
    Then I have gained points


  Scenario: Incorrectly solving a quest objective does not award points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I incorrectly guess the answer to a quest riddle
    Then I have not gained points


  Scenario: Checking in to a quest objective awards points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I check into a destination
    Then I have gained points


  Scenario: Checking in to an objective that hasn't been solved does not award points
    Given the application is running
    And I am logged in
    And I have some starting points
    When I check in for quest attempt 4
    Then the status code received is 403
    And I have not gained points


  Scenario: Completing a quest awards points
    Given the application is running
    And I am logged in as user with id "7"
    And a quest exists with id 6
    When I check in for quest attempt 7
    Then I have gained points


  Scenario: Creating a destination
    Given the application is running
    And I am logged in
    And I have some starting points
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then I have gained points

#Badges
  Scenario: Successfully requesting all badges
    Given the application is running
    And I am logged in
    When I request to retrieve all badges
    Then the status code received is 200
    And the response contains 9 badges


  Scenario: Unsuccessfully requesting all badges when not logged in
    Given the application is running
    And I am not logged in
    When I request to retrieve all badges
    Then the status code received is 401

#Awarding Badges
  Scenario: Creating my first destination and getting a bronze level Cartographer badge
    Given the application is running
    And I am logged in as user with id "7"
    And my current progress towards the "Cartographer" badge is 0
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Cartographer" badge with level 1


  Scenario: Creating enough destinations to achieve a silver level Cartographer badge
    Given the application is running
    And I am logged in as user with id "8"
    And my current progress towards the "Cartographer" badge is 9
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Cartographer" badge with level 2


  Scenario: Creating enough destinations to achieve a gold level Cartographer badge
    Given the application is running
    And I am logged in as user with id "9"
    And my current progress towards the "Cartographer" badge is 49
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Cartographer" badge with level 3


  Scenario: Creating my first trip and getting a bronze level Planner badge
    Given the application is running
    And I am logged in as user with id "7"
    And my current progress towards the "Planner" badge is 0
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
    And I gain the "Planner" badge with level 1


  Scenario: Creating enough trips to achieve a silver level Planner badge
    Given the application is running
    And I am logged in as user with id "8"
    And my current progress towards the "Planner" badge is 9
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
    And I gain the "Planner" badge with level 2


  Scenario: Creating enough trips to achieve a gold level Planner badge
    Given the application is running
    And I am logged in as user with id "9"
    And my current progress towards the "Planner" badge is 29
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
    And I gain the "Planner" badge with level 3


  Scenario: Creating my first quest and getting a bronze level Writer badge
    Given the application is running
    And I am logged in as user with id "7"
    And I currently have no "quests" created
    When I start to create a quest using the following values
      | Title      | Start Date | End Date |
      | Cool Quest |            |          |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                  | Radius |
      | 119         | What rhymes some stuff? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    Then the status code received is 201
    And I gain the "Writer" badge with level 1


  Scenario: Creating enough quests to achieve a silver level Writer badge
    Given the application is running
    And I am logged in as user with id "8"
    And I currently have no "quests" created
    When I start to create a quest using the following values
      | Title      | Start Date | End Date |
      | Cool Quest |            |          |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                  | Radius |
      | 119         | What rhymes some stuff? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    Then the status code received is 201
    And I gain the "Writer" badge with level 2


  Scenario: Creating enough quests to achieve a gold level Writer badge
    Given the application is running
    And I am logged in as user with id "9"
    And I currently have no "quests" created
    When I start to create a quest using the following values
      | Title      | Start Date | End Date |
      | Cool Quest |            |          |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                  | Radius |
      | 119         | What rhymes some stuff? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    Then the status code received is 201
    And I gain the "Writer" badge with level 3


  Scenario: Solving enough quests to achieve bronze level Solver badge
    Given the application is running
    And I am logged in as user with id "7"
    And a quest exists with id 6
    When I check in for quest attempt 7
    And I retrieve all my complete quests
    Then the status code received is 200
    And the response contains quest 6
    And I gain the "Solver" badge with level 1


  Scenario: Solving enough quests to achieve silver level Solver badge
    Given the application is running
    And I am logged in as user with id "8"
    And a quest exists with id 6
    When I check in for quest attempt 17
    And I retrieve all my complete quests
    Then the status code received is 200
    And the response contains quest 6
    And I gain the "Solver" badge with level 2


  Scenario: Solving enough quests to achieve gold level Solver badge
    Given the application is running
    And I am logged in as user with id "9"
    And a quest exists with id 6
    When I check in for quest attempt 18
    And I retrieve all my complete quests
    Then the status code received is 200
    And the response contains quest 6
    And I gain the "Solver" badge with level 3


  Scenario: Gaining enough points to achieve bronze level Overachiever badge
    Given the application is running
    And I am logged in as user with id "7"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 1


  Scenario: Gaining enough points to achieve silver level Overachiever badge
    Given the application is running
    And I am logged in as user with id "8"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 2


  Scenario: Gaining enough points to achieve gold level Overachiever badge
    Given the application is running
    And I am logged in as user with id "9"
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201
    And I gain the "Overachiever" badge with level 3


#TODO: Joel - Waiting on backend implementation.
#  Scenario: Getting the bronze level Streaker badge
#    Given the application is running
#    And The following profile does not exist with the username "TestUser123@travelea.com" within the TravelEA database
#    When A user attempts to create a profile with the following fields:
#      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
#      | TestUser123@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
#    Then the status code received is 201
#    And I gain the "Streaker" badge with level 1
#
#
#  Scenario: Losing a current streak
#    Given the application is running
#    And the user with id "7" has a current streak of 3
#    And the user with id "7" current progress towards the "Streaker" badge is 3
#    And the user with id "7" last logged in 2 day ago
#    When I login to the application with id "7" and username "testuser5@email.com"
#    Then the status code received is 200
#    And my current streak is 0
#    And my last login was 0 days ago
#    And the current progress towards the "Streaker" badge is still 3
#
#
#  Scenario: Getting the silver level Streaker badge
#    Given the application is running
#    And the user with id "8" has a current streak of 6
#    And the user with id "8" current progress towards the "Streaker" badge is 6
#    And the user with id "8" last logged in 1 day ago
#    When I login to the application with id "8" and username "testuser6@email.com"
#    Then the status code received is 200
#    And I gain the "Streaker" badge with level 2
#    And my current streak is 7
#    And my last login was 0 days ago
#
#
#  Scenario: Getting the gold level Streaker badge
#    Given the application is running
#    And the user with id "9" has a current streak of 30
#    And the user with id "9" current progress towards the "Streaker" badge is 30
#    And the user with id "9" last logged in 1 day ago
#    When I login to the application with id "9" and username "testuser7@email.com"
#    Then the status code received is 200
#    And I gain the "Streaker" badge with level 3
#    And my current streak is 31
#    And my last login was 0 days ago


  Scenario: Gaining enough points to achieve bronze level Wayfarer badge
    Given the application is running
    And I am logged in as user with id "7"
    And my current progress towards the "Wayfarer" badge is 99999
    When I check in for quest attempt 8
    Then the status code received is 200
    And I gain the "Wayfarer" badge with level 1


  Scenario: Gaining enough points to achieve silver level Wayfarer badge
    Given the application is running
    And I am logged in as user with id "8"
    And my current progress towards the "Wayfarer" badge is 499999
    When I check in for quest attempt 9
    Then the status code received is 200
    And I gain the "Wayfarer" badge with level 2


  Scenario: Gaining enough points to achieve gold level Wayfarer badge
    Given the application is running
    And I am logged in as user with id "9"
    And my current progress towards the "Wayfarer" badge is 999999
    When I check in for quest attempt 10
    Then the status code received is 200
    And I gain the "Wayfarer" badge with level 3


  Scenario: Completing an international quest to earn a bronze level Explorer badge
    Given the application is running
    And I am logged in as user with id "7"
    And my current progress towards the "Explorer" badge is 0
    When I check in for quest attempt 11
    Then the status code received is 200
    And I gain the "Explorer" badge with level 1


  Scenario: Completing enough international quests to earn a silver level Explorer badge
    Given the application is running
    And I am logged in as user with id "8"
    And my current progress towards the "Explorer" badge is 4
    When I check in for quest attempt 12
    Then the status code received is 200
    And I gain the "Explorer" badge with level 2


  Scenario: Completing enough international quests to earn a gold level Explorer badge
    Given the application is running
    And I am logged in as user with id "9"
    And my current progress towards the "Explorer" badge is 9
    When I check in for quest attempt 13
    Then the status code received is 200
    And I gain the "Explorer" badge with level 3


  Scenario: Gaining enough points to achieve bronze level Adventurer badge
    Given the application is running
    And I am logged in as user with id "7"
    And my current progress towards the "Adventurer" badge is 0
    When I check in for quest attempt 14
    Then the status code received is 200
    And I gain the "Adventurer" badge with level 1


  Scenario: Gaining enough points to achieve silver level Adventurer badge
    Given the application is running
    And I am logged in as user with id "8"
    And my current progress towards the "Adventurer" badge is 9
    When I check in for quest attempt 15
    Then the status code received is 200
    And I gain the "Adventurer" badge with level 2


  Scenario: Gaining enough points to achieve gold level Adventurer badge
    Given the application is running
    And I am logged in as user with id "9"
    And my current progress towards the "Adventurer" badge is 29
    When I check in for quest attempt 16
    Then the status code received is 200
    And I gain the "Adventurer" badge with level 3