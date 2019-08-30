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
    Then I have gained points.

  Scenario: Incorrectly solving a quest riddle
    Given the application is running
    And I am logged in
    And I have some starting points
    When I incorrectly guess the answer to a quest riddle
    Then I have not gained points
