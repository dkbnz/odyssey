Feature: Quest API Endpoint

  Scenario: Successfully creating a quest with valid input as a regular user
    Given the application is running
    And I am logged in
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


  Scenario: Successfully creating a quest with valid input for a regular user as an admin
    Given the application is running
    And I am logged in as an admin user
    When I start to create a quest for a regular user using the following values
      | Title       | Start Date | End Date |
      | Cool Quest  |            |          |
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


  Scenario: Unsuccessfully creating a quest without a title as a regular user
    Given the application is running
    And I am logged in
    When I start to create a quest using the following values
      | Title | Start Date | End Date |
      |       |            |          |
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
    Then the status code received is 400
    And the following ApiErrors are returned
      | A quest title must be provided. |


  Scenario: Unsuccessfully creating a quest without a title for a regular user as an admin
    Given the application is running
    And I am logged in as an admin user
    When I start to create a quest for a regular user using the following values
      | Title | Start Date | End Date |
      |       |            |          |
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
    Then the status code received is 400
    And the following ApiErrors are returned
      | A quest title must be provided. |


  Scenario: Unsuccessfully creating a quest with incorrectly ordered dates
    Given the application is running
    And I am logged in
    When I start to create a quest using the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-25 18:08:59+0000 | 2019-08-15 18:08:59+0000 |
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
    Then the status code received is 400
    And the following ApiErrors are returned
      | Start date must be before end date. |


  Scenario: Unsuccessfully creating a quest with incorrectly ordered dates for a regular user as an admin
    Given the application is running
    And I am logged in as an admin user
    When I start to create a quest for a regular user using the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-25 18:08:59+0000 | 2019-08-15 18:08:59+0000 |
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
    Then the status code received is 400
    And the following ApiErrors are returned
      | Start date must be before end date. |


  Scenario: Unsuccessfully creating a quest with no objectives
    Given the application is running
    And I am logged in
    When I start to create a quest using the following values
      | Title       | Start Date | End Date  |
      | Cool Quest  |            |           |
    And I create the quest
    Then the status code received is 400
    And the following ApiErrors are returned
      | You must provide at least one Objective for a quest. |


  Scenario: Unsuccessfully creating a quest with no objectives for a regular user as an admin
    Given the application is running
    And I am logged in as an admin user
    When I start to create a quest for a regular user using the following values
      | Title       | Start Date | End Date  |
      | Cool Quest  |            |           |
    And I create the quest
    Then the status code received is 400
    And the following ApiErrors are returned
      | You must provide at least one Objective for a quest. |


  Scenario: Unsuccessfully creating a quest for a regular user as an alternate user
    Given the application is running
    And I am logged in as an alternate user
    When I start to create a quest for a regular user using the following values
      | Title       | Start Date | End Date |
      | Not Allowed |            |          |
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
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |


  Scenario: Unsuccessfully creating a quest when not logged in
    Given the application is running
    And I am not logged in
    When I start to create a quest for a regular user using the following values
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
    Then the status code received is 401


  Scenario: Unsuccessfully creating a quest for a non-existent user as an admin
    Given the application is running
    And I am logged in as an admin user
    When I start to create a quest for a non-existent user using the following values
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
    Then the status code received is 404


  Scenario: Successfully editing a quest with valid input as a regular user
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
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
    When I attempt to edit the quest with the following values
      | Title       | Start Date | End Date  |
      | Epic Quest  |            |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 200


  Scenario: Successfully editing another user's quests as an admin
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    And I am not logged in
    And I am logged in as an admin user
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Epic Quest  |                    |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 200


  Scenario: Unsuccessfully editing another user's quests as a regular user
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    And I am not logged in
    And I am logged in as an alternate user
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Epic Quest  |                    |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 403


  Scenario: Unsuccessfully editing a quest when not logged in
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    And I am not logged in
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Epic Quest  |                    |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 401


  Scenario: Unsuccessfully editing a quest due to no title
  Given the application is running
  And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      |             |                    |           |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 400


  Scenario: Unsuccessfully editing a quest due to no objectives
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
    And the quest has the following objective
      | Destination | Riddle | Radius |
      |             |        |        |
    And I edit the quest
    Then the status code received is 400


  Scenario: Unsuccessfully editing a quest due to bad dates
    Given the application is running
    And I am logged in
    And a quest already exists with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
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
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  | XXX                |  XXX      |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I edit the quest
    Then the status code received is 400


  Scenario: Retrieve all quests
    Given I am logged in as an alternate user
    And the application is running
    When I attempt to retrieve all quests
    Then the status code received is 200
    And the response contains 4 quests


  Scenario: Retrieve all quests that are available with additional invalid quest dates
    Given I am logged in as an alternate user
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 2019-08-17 03:02:00-0720 |
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
    And the status code received is 201
    When I attempt to retrieve all quests
    Then the status code received is 200
    And the response contains 4 quests


  Scenario: Retrieve all quests that are available with additional valid quest dates
    Given I am logged in as an alternate user
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    When I attempt to retrieve all quests
    Then the status code received is 200
    And the response contains 4 quests


  Scenario: Retrieve all quests that have the title 'Cool Quest'
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with title 'Cool Quest'
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieve all quests that have exactly 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with exactly 3 objectives
    Then the status code received is 200
    And the response contains 2 quests


  Scenario: Retrieve all quests that have less than 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with less than 3 objectives
    Then the status code received is 200
    And the response contains 3 quests


  Scenario: Retrieve all quests that have greater than 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes some stuff?                | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with greater than 3 objectives
    Then the status code received is 200
    And the response contains 2 quests


  Scenario: Retrieve all quests that were made by the owner
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And I am logged in as an admin user
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests created by the user 'Dave' 'McInloch'
    Then the status code received is 200
    And the response contains 5 quests


  Scenario: Retrieve all quests that are contained the country of 'Japan'
    And I am logged in as an admin user
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                  | Radius |
      | 9000        | Where do I go to eat sushi              | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests that contain the country 'Japan'
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieve all quests that have the title 'Cool Quest'
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with title 'Cool Quest'
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieve all quests that have exactly 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with exactly 3 objectives
    Then the status code received is 200
    And the response contains 2 quests


  Scenario: Retrieve all quests that have less than 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with less than 3 objectives
    Then the status code received is 200
    And the response contains 3 quests


  Scenario: Retrieve all quests that have greater than 3 objectives
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes some stuff?                | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests with greater than 3 objectives
    Then the status code received is 200
    And the response contains 2 quests


  Scenario: Retrieve all quests that were made by the owner
    Given I am logged in
    And the application is running
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
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
    And the status code received is 201
    And I am logged in as an admin user
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests created by the user 'Dave' 'McInloch'
    Then the status code received is 200
    And the response contains 5 quests


  Scenario: Retrieve all quests that are contained the country of 'Japan'
    And I am logged in as an admin user
    And a quest already exists with the following values
      | Title       | Start Date               | End Date                 |
      | Qool Cuest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
    And the quest has the following objective
      | Destination | Riddle                                  | Radius |
      | 9000        | Where do I go to eat sushi              | 0.005  |
    And I create the quest
    And the status code received is 201
    And I am logged in as an alternate user
    When I attempt to retrieve all quests that contain the country 'Japan'
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieve all quests when I am not logged in
    Given I am not logged in
    And the application is running
    When I attempt to retrieve all quests
    Then the status code received is 401


  Scenario: Retrieve all quests I have created
    Given I am logged in
    And the application is running
    When I attempt to retrieve my quests
    Then the status code received is 200
    And the response contains 5 quests


  Scenario: Retrieve all quests when I have none created
    Given I am logged in as an alternate user
    And the application is running
    When I attempt to retrieve my quests
    Then the status code received is 200
    And the response contains 0 quests


  Scenario: Retrieve all quests for another user as an admin
    Given I am logged in as an admin user
    And the application is running
    And a user exists with id 2
    When I attempt to retrieve quests for user 2
    Then the status code received is 200
    And the response contains 5 quests


  Scenario: Retrieve all quests for another user as a regular user
    Given I am logged in as an alternate user
    And the application is running
    And a user exists with id 2
    When I attempt to retrieve quests for user 2
    Then the status code received is 403


  Scenario: Retrieve all quests when I am not logged in
    Given I am not logged in
    And the application is running
    And a user exists with id 2
    When I attempt to retrieve quests for user 2
    Then the status code received is 401


  Scenario: Delete a quest I own
    Given I am logged in
    And the application is running
    And a quest exists with id 5
    And an objective exists with id 10
    And an objective exists with id 11
    When I delete a quest with id 5
    Then the status code received is 200
    And the quest with id 5 no longer exists
    And the objective with id 10 still exists
    And the objective with id 11 still exists


  Scenario: Delete a quest I do not own
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 5
    When I delete a quest with id 5
    Then the status code received is 403


  Scenario: Delete a quest I do not own as an admin
    Given I am logged in as an admin user
    And the application is running
    And a quest exists with id 5
    And an objective exists with id 10
    And an objective exists with id 11
    When I delete a quest with id 5
    Then the status code received is 200
    And the quest with id 5 no longer exists
    And the objective with id 10 still exists
    And the objective with id 11 still exists


  Scenario: Delete a quest when I am not logged in
    Given I am not logged in
    And the application is running
    And a quest exists with id 5
    When I delete a quest with id 5
    Then the status code received is 401


  Scenario: Delete a quest that does not exist
    Given I am logged in
    And the application is running
    And a quest does not exist with id 7
    When I delete a quest with id 7
    Then the status code received is 404


  Scenario: Starting a quest as a regular user
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 3
    When I start a quest with id 3
    Then the status code received is 201
    And the new quest attempt exists


  Scenario: Starting a quest for a regular user as an admin
    Given I am logged in as an admin user
    And the application is running
    And a quest exists with id 5
    And a user exists with id 4
    When I start a quest with id 5 for user 4
    Then the status code received is 201
    And the new quest attempt exists


  Scenario: Starting a quest that does not exist
    Given I am logged in
    And the application is running
    And a quest does not exist with id 7
    When I start a quest with id 7
    Then the status code received is 404
    And the following ApiErrors are returned
    | Resource not found. |


  Scenario: Starting a quest for a user that does not exist
    Given I am logged in as an admin user
    And the application is running
    And a quest exists with id 5
    And a user does not exist with id 10
    When I start a quest with id 5 for user 10
    Then the status code received is 404
    And the following ApiErrors are returned
      | Resource not found. |


  Scenario: Starting a quest I have already started
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 5
    When I start a quest with id 5
    And I start a quest with id 5
    Then the status code received is 400
    And the following ApiErrors are returned
      | An attempt already exists for this quest. |


  Scenario: Starting a quest when I am not logged in
    Given I am not logged in
    And the application is running
    And a quest exists with id 5
    And a user exists with id 3
    When I start a quest with id 5 for user 3
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Starting a quest that I own
    Given I am logged in
    And the application is running
    And a quest exists with id 5
    When I start a quest with id 5
    Then the status code received is 403
    And the following ApiErrors are returned
      | You cannot start your own quest. |


  Scenario: Retrieving all quests I have marked as active
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 1
    And a user exists with id 3
    When I start a quest with id 1 for user 3
    When I retrieve all active quests for user 3
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieving all quests I have marked as active when not logged in
    Given I am not logged in
    And the application is running
    And a quest exists with id 5
    And a user exists with id 2
    When I start a quest with id 5 for user 2
    And I retrieve all active quests for user 2
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Retrieving all quests marked as active for another user as admin
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 1
    And a user exists with id 3
    When I start a quest with id 1 for user 3
    And I am logged in as an admin user
    And I retrieve all active quests for user 3
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieving all quests marked as active for another user as an alternative user
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 1
    And a user exists with id 3
    When I start a quest with id 1 for user 3
    And I am logged in
    And I retrieve all active quests for user 3
    Then the status code received is 200
    And the response contains 1 quests


  Scenario: Retrieving all completed quests as a regular user
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 5
    And the quest with id 5 has been completed
    When I retrieve all my complete quests
    Then the status code received is 200
    And the response contains 1 quests
    And the response has owner view


  Scenario: Retrieving all completed quests as a regular user when I have none completed
    Given I am logged in
    And the application is running
    When I retrieve all my complete quests
    Then the status code received is 200
    And the response contains 0 quests


  Scenario: Retrieving all quests another user has completed as a regular user
    Given I am logged in as an alternate user
    And the application is running
    And a quest exists with id 2
    And a user exists with id 4
    And the quest with id 2 has been completed by user 4
    When I retrieve all complete quests for user 4
    Then the status code received is 200
    And the response contains 1 quests
    And the response has public view


  Scenario: Retrieving all quests another user has completed as an admin
    Given I am logged in as an admin user
    And the application is running
    And a quest exists with id 5
    And a user exists with id 3
    And the quest with id 5 has been completed by user 3
    When I retrieve all complete quests for user 3
    Then the status code received is 200
    And the response contains 1 quests
    And the response has owner view


  Scenario: Retrieving all quests a non existent user has completed as an admin
    Given I am logged in as an admin user
    And the application is running
    And a user does not exist with id 10
    When I retrieve all complete quests for user 10
    Then the status code received is 404
    And the following ApiErrors are returned
      | Resource not found. |


  Scenario: Retrieving all completed quests when not logged in
    Given I am not logged in
    And the application is running
    And a user exists with id 4
    When I retrieve all complete quests for user 4
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Completing a quest as a regular user
    Given I am logged in
    And the application is running
    And a quest exists with id 3
    And I have started a quest with id 3
    When I check in to the quest with id 3
    Then the status code received is 200
    And the quest is completed


  Scenario: Completing a quest as a regular user
    Given I am logged in
    And the application is running
    And a quest exists with id 3
    And I have started a quest with id 3
    When I check in to the quest with id 3
    Then the status code received is 200
    And the quest is completed



#  TODO: Vinnie - Retrieval of active users
#  Scenario Successfully retrieving all active users for a quest
#    Given the application is running
#    And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date               | End Date                 |
#      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest is being attempted by a user with id 3
#    When I attempt to retrieve all active users for a quest
#    Then the status code received is 200
#    And the response contains 1 users
#
#
#  Scenario Unsuccessfully retrieving all active users for a quest as no user is logged in
#    Given the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date               | End Date                 |
#      | Cool Quest  | 2019-08-16 03:02:00-0720 | 9999-08-17 03:02:00-0720 |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest is being attempted by a user with id 3
#    When I attempt to retrieve all active users for a quest
#    Then the status code received is 401
#    And the following ApiErrors are returned
#      | You are not logged in. |
#
#
#  Scenario Successfully retrieving all active users for a quest
#    Given the application is running
#    And I am logged in
#    When I attempt to retrieve all active users for a quest
#    Then the status code received is 404
#    And the following ApiErrors are returned
#      | Resource not found. |
