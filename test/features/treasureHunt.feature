Feature: TreasureHunt API Endpoint

  Scenario: Successfully get all Treasure Hunts
    Given I have the application running
    And I am logged in as a normal user
    And a treasure hunt already exists with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    And the status code I receive is 201
    When I request to retrieve all treasure hunts
    Then the status code I receive is 200
    And the response contains at least one treasure hunt


  Scenario: Successfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged in as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the status code I receive is 201


  Scenario: Unsuccessfully creating a new Treasure Hunt with no destination
    Given I have a application running
    And I am logged in as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | null        | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the status code I receive is 400


  Scenario: Unsuccessfully creating a new Treasure Hunt with no riddle
    Given I have a application running
    And I am logged in as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle | Start Date | End Date | Owner |
      | 119         | null   | null       | null     | 2     |
    Then the status code I receive is 400

#TODO: Vinnie Jamieson - can you please add a more detailed description to the scenario.
  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged in as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 119         | What rhymes with It's mean Kyle fleek? | 8096-12-12 | null     | 2     |
    Then the status code I receive is 400

#TODO: Vinnie Jamieson - can you please add a more detailed description to the scenario.
  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged in as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date   | Owner |
      | 119         | What rhymes with It's mean Kyle fleek? | null       | 1993-12-12 | 2     |
    Then the status code I receive is 400

  Scenario: Successfully editing a Treasure Hunt
    Given I have the application running
    And I am logged in as a normal user
    And a treasure hunt already exists with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 119         | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    And the status code I receive is 201
    When I attempt to edit the treasure hunt with the following values
      | Riddle              |
      | How does this work? |
    Then the status code I receive is 200


# TODO: Joel Ridden
#  Scenario: Delete an already existing Treasure Hunt that I own
#    Given I have a application running
#    And I am logged in as a normal user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date   | Owner |
#      | 15040       | Delete me please                       | null       | 1993-12-12 | 2     |
#    When I attempt to delete the treasure Hunt
#    Then the status code I receive is 200
#
#
#  Scenario: Delete an already existing Treasure Hunt as an admin
#    Given I have a application running
#    And I am logged in as an admin user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date   | Owner |
#      | 15040       | Delete me please                       | null       | 1993-12-12 | 2     |
#    When I attempt to delete the treasure Hunt
#    Then the status code I receive is 200
#
#
#  Scenario: Delete an already existing Treasure Hunt that I don't own
#    Given I have a application running
#    And I am logged in as a normal user
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date   | Owner |
#      | 15040       | Delete me please                       | null       | 1993-12-12 | 1     |
#    When I attempt to delete the treasure Hunt
#    Then the status code I receive is 403
#
#
#  Scenario: Delete an already existing Treasure Hunt and I am not logged in
#    Given I have a application running
#    And The user is not logged in
#    And a treasure hunt already exists with the following values
#      | Destination | Riddle                                 | Start Date | End Date   | Owner |
#      | 15040       | Delete me please                       | null       | 1993-12-12 | 2     |
#    When I attempt to delete the treasure Hunt
#    Then the status code I receive is 401
