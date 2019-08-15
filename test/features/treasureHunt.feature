# TODO: As TreasureHunt model is changing for Quests, features will need to be changed.
#
#Feature: TreasureHunt API Endpoint
#
#  Scenario: Successfully get all Treasure Hunts
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    And the status code received is 201
#    When I request to retrieve all treasure hunts
#    Then the status code received is 200
#    And the response contains at least one treasure hunt
#
#
#  Scenario: Successfully getting no Treasure Hunts because of dates out of range
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date               | End Date                 | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | 1998-07-29 12:12:12-0720 | 1998-07-30 12:12:12-0720 | 2     |
#    And the status code received is 201
#    When I request to retrieve all treasure hunts
#    Then the status code received is 200
#    And the response contains no treasure hunts
#
#
#  Scenario: Successfully creating a new Treasure Hunt
#    Given the application is running
#    And I am logged in
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    Then the status code received is 201
#
#
#  Scenario: Successfully creating a new Treasure Hunt as an admin for another user
#    Given the application is running
#    And I am logged in as an admin user
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    Then the status code received is 201
#
#
#  Scenario: Unsuccessfully creating a new Treasure Hunt with no destination
#    Given the application is running
#    And I am logged in
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | null        | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    Then the status code received is 400
#
#
#  Scenario: Unsuccessfully creating a new Treasure Hunt with no riddle
#    Given the application is running
#    And I am logged in
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle | Start Date | End Date | Owner |
#      | 119         |        | null       | null     | 2     |
#    Then the status code received is 400
#
#
#  Scenario: Unsuccessfully creating a new Treasure Hunt with a start date in the future
#    Given the application is running
#    And I am logged in
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | 8096-12-12 | null     | 2     |
#    Then the status code received is 400
#
#
#  Scenario: Unsuccessfully creating a new Treasure Hunt with an end date in the past
#    Given the application is running
#    And I am logged in
#    When I attempt to create a treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date   | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | 2000-12-12 | 2     |
#    Then the status code received is 400
#
#
#  Scenario: Successfully editing a Treasure Hunt
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    And the status code received is 201
#    When I attempt to edit the treasure hunt with the following values
#      | Destination | Riddle          | Start Date | End Date | Owner |
#      | 119         | Does this work? | null       | null     | 2     |
#    Then the status code received is 200
#
#
#  Scenario: Unsuccessfully editing a Treasure Hunt with no destination
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    And the status code received is 201
#    When I attempt to edit the treasure hunt with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | null        | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    Then the status code received is 400
#
#
#
#  Scenario: Unsuccessfully editing a Treasure Hunt for another user as non-admin
#    Given the application is running
#    And I am logged in as an admin user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 1     |
#    And I am not logged in
#    And I am logged in
#    When I attempt to edit the treasure hunt with the following values
#      | Destination | Riddle | Start Date | End Date | Owner |
#      | 119         | COOL?  | null       | null     | 1     |
#    Then the status code received is 403
#
#
#  Scenario: Delete an already existing Treasure Hunt that I own
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    When I attempt to delete the treasure Hunt
#    Then the status code received is 200
#
#
#  Scenario: Delete an already existing Treasure Hunt as an admin
#    Given the application is running
#    And I am logged in as an admin user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    When I attempt to delete the treasure Hunt
#    Then the status code received is 200
#
#
#  Scenario: Delete an already existing Treasure Hunt that I don't own
#    Given the application is running
#    And I am logged in as an admin user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 1     |
#    And I am logged in
#    When I attempt to delete the treasure Hunt
#    Then the status code received is 403
#
#
#  Scenario: Delete an already existing Treasure Hunt and I am not logged in
#    Given the application is running
#    And I am logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date | Owner |
#      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
#    And I am not logged in
#    When I attempt to delete the treasure Hunt
#    Then the status code received is 401
#
