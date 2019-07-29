Feature: TreasureHunt API Endpoint

  Scenario: Successfully get all Treasure Hunts
    Given I have the application running
    And I am logged in as a normal user
    And a treasure hunt already exists with the following values
      | Destination       | Riddle             | Start Date     | End Date | Owner |
      | 119               | This is great      | null           | null     | 2     |
    When I request to retrieve all treasure hunts
    Then the response code is OK
    And the response contains at least one treasure hunt


  Scenario: Successfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 15040       | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the response status code is Created


  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | null        | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the response status code is BadRequest


  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle | Start Date | End Date | Owner |
      | 15040       | null   | null       | null     | 2     |
    Then the response status code is BadRequest


  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 15040       | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the response status code is BadRequest


  Scenario: Unsuccessfully creating a new Treasure Hunt
    Given I have a application running
    And I am logged as a normal user
    When I attempt to create a treasure hunt with the following values
      | Destination | Riddle                                 | Start Date | End Date | Owner |
      | 15040       | What rhymes with It's mean Kyle fleek? | null       | null     | 2     |
    Then the response status code is BadRequest