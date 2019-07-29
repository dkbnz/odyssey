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