Feature: Voting API Endpoint

  # Unsuccessful cases for upvoting
  Scenario: Upvoting a hint that does not exist
    Given the application is running
    And I am logged in
    And a hint with id 100 does not exist
    When I attempt to upvote a hint with id 100 for user with id 2
    Then the status code received is 404
    And the following ApiErrors are returned
      | The requested hint is not found. |


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


  # Successful cases for upvoting
  Scenario: Upvoting a hint for an objective I have solved
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the owner of the hint with id 1 has some starting points
    And the hint with id 1 has 10 upvotes
    When I attempt to upvote a hint with id 1 for user with id 8
    Then the status code received is 200
    And the hint with id 1 has 11 upvotes
    And the owner of the hint with id 1 has gained points


  Scenario: Removing an up vote for a hint that I have already upvoted
    Given the application is running
    And I am logged in as user with id 7
    And an objective exists with id 31
    And I do not own the objective with id 31
    And I have solved the objective with id 31
    And a hint with id 3 exists for objective with id 31
    And the owner of the hint with id 3 has some starting points
    And the hint with id 3 has 45 upvotes
    And I have already upvoted the hint with id 3
    When I attempt to upvote a hint with id 3 for user with id 7
    Then the status code received is 200
    And the hint with id 3 has 44 upvotes
    And the owner of the hint with id 3 has lost points


  # Unsuccessful cases for downvoting
  Scenario: Downvoting a hint that does not exist
    Given the application is running
    And I am logged in
    And a hint with id 100 does not exist
    When I attempt to downvote a hint with id 100 for user with id 2
    Then the status code received is 404
    And the following ApiErrors are returned
      | The requested hint is not found. |


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
    And the hint with id 1 has 3 downvotes
    When I attempt to downvote a hint with id 1 for user with id 2
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |
    And the hint with id 1 has 3 downvotes


  # Successful cases for downvoting
  Scenario: Downvoting a hint for an objective I have solved
    Given the application is running
    And I am logged in as user with id 8
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    And a hint with id 1 exists for objective with id 18
    And the hint with id 1 has 3 downvotes
    When I attempt to downvote a hint with id 1 for user with id 8
    Then the status code received is 200
    And the hint with id 1 has 4 downvotes


  Scenario: Removing a down vote for a hint that I have already downvoted
    Given the application is running
    And I am logged in as user with id 9
    And an objective exists with id 24
    And I do not own the objective with id 24
    And I have solved the objective with id 24
    And a hint with id 2 exists for objective with id 24
    And the hint with id 2 has 10 downvotes
    And I have already downvoted the hint with id 2
    When I attempt to downvote a hint with id 2 for user with id 9
    Then the status code received is 200
    And the hint with id 2 has 9 downvotes


  # Changing votes on a hint.
  Scenario: Changing my downvote to an upvote
    Given the application is running
    And I am logged in as user with id 9
    And an objective exists with id 24
    And I do not own the objective with id 24
    And I have solved the objective with id 24
    And a hint with id 2 exists for objective with id 24
    And the owner of the hint with id 2 has some starting points
    And the hint with id 2 has 10 downvotes
    And the hint with id 2 has 4 upvotes
    And I have already downvoted the hint with id 2
    When I attempt to upvote a hint with id 2 for user with id 9
    Then the status code received is 200
    And the hint with id 2 has 9 downvotes
    And the hint with id 2 has 5 upvotes
    And the owner of the hint with id 2 has gained points


  Scenario: Changing my upvote to a downvote
    Given the application is running
    And I am logged in as user with id 7
    And an objective exists with id 31
    And I do not own the objective with id 31
    And I have solved the objective with id 31
    And a hint with id 3 exists for objective with id 31
    And the owner of the hint with id 3 has some starting points
    And the hint with id 3 has 45 upvotes
    And the hint with id 3 has 38 downvotes
    And I have already upvoted the hint with id 3
    When I attempt to downvote a hint with id 3 for user with id 7
    Then the status code received is 200
    And the hint with id 3 has 44 upvotes
    And the hint with id 3 has 39 downvotes
    And the owner of the hint with id 3 has lost points