Feature: Voting on hints

  # Unsuccessful cases for upvoting
  Scenario: Upvoting a hint that does not exist
    Given the application is running
    And I am logged in
    And a hint with id 100 does not exist
    When I attempt to upvote a hint with id 100 for user with id 2
    Then the status code received is 404
    And the following ApiErrors are returned
      | Resource not found. |


  Scenario: Upvoting a hint belonging to an objective I have not solved
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have not solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    When I attempt to upvote a hint with id 1 for user with id 2
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |


  Scenario: Upvoting a hint when I am not logged in
    Given the application is running
    And I am not logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 upvotes
    When I attempt to upvote a hint with id 1 for user with id 2
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |
    And the hint with id 1 has 10 upvotes


  Scenario: Upvoting a hint that I have already upvoted
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 upvotes
    And I have already upvoted the hint with id 1
    When I attempt to upvote a hint with id 1 for user with id 2
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |
    And the hint with id 1 has 11 upvotes


  # Successful cases for upvoting
  Scenario: Upvoting a hint for an objective I have solved
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 downvotes
    When I attempt to downvote a hint with id 1 for user with id 8
    Then the status code received is 200
    And the hint with id 1 has 11 downvotes


  # Unsuccessful cases for downvoting
  Scenario: Downvoting a hint that does not exist
    Given the application is running
    And I am logged in
    And a hint with id 100 does not exist
    When I attempt to downvote a hint with id 100 for user with id 2
    Then the status code received is 404
    And the following ApiErrors are returned
      | Resource not found. |


  Scenario: Downvoting a hint belonging to an objective I have not solved
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have not solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    When I attempt to downvote a hint with id 1 for user with id 2
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |


  Scenario: Downvoting a hint when I am not logged in
    Given the application is running
    And I am not logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 downvotes
    When I attempt to downvote a hint with id 1 for user with id 2
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |
    And the hint with id 1 has 10 downvotes


  Scenario: Downvoting a hint that I have already downvoted
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 downvotes
    And I have already downvoted the hint with id 1
    When I attempt to downvote a hint with id 1 for user with id 8
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |
    And the hint with id 1 has 11 downvotes


  # Successful cases for downvoting
  Scenario: Downvoting a hint for an objective I have solved
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 10 downvotes
    When I attempt to downvote a hint with id 1 for user with id 8
    Then the status code received is 200
    And the hint with id 1 has 11 downvotes