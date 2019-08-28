Feature: Achievement Tracker API Endpoint

  Scenario: Solving a Quest riddle
    Given the application is running
    And I am logged in
    And I have some starting points
    When I solve the current riddle for a Quest
    Then I have gained points.
