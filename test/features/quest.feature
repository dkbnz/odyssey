Feature: Quest API Endpoint

#  Scenario: Successfully editing a quest with valid input as a regular user
#    Given the application is running
#    And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date | End Date  |
#      | Cool Quest  |            |           |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date | End Date  |
#      | Epic Quest  |            |           |
#    And with the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    Then the status code received is 200
#
#
#  Scenario: Successfully editing another user's quests as an admin
#    Given the application is running
#    And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And I am not logged in
#    And I am logged in as an admin user
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    And with the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    Then the status code received is 200
#
#
#  Scenario: Unsuccessfully editing another user's quests as a regular user
#    Given the application is running
#    And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And I am not logged in
#    And I am logged in as an alternate user
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    And with the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    Then the status code received is 403
#
#
#  Scenario: Unsuccessfully editing a quest when not logged in
#    Given the application is running
#    And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And I am not logged in
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    And with the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    Then the status code received is 401
#
#
#  Scenario: Unsuccessfully editing a quest due to no title
#  Given the application is running
#  And I am logged in
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes some stuff?                | 0.005  |
#    And the quest has the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      |             |                    |           |
#    And with the following objective
#      | Destination | Riddle                                 | Radius |
#      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
#    Then the status code received is 400


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
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  |                    |           |
    And with the following objective
      | Destination | Riddle      | Radius |
      |             |             |        |
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
    When I attempt to edit the quest with the following values
      | Title       | Start Date         | End Date  |
      | Cool Quest  | XXX                |  XXX      |
    And with the following objective
      | Destination | Riddle                                 | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 0.005  |
    Then the status code received is 400