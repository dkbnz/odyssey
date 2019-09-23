Feature: Hint API Endpoint

  Scenario: Successfully creating a hint as the owner of the objective
    Given the application is running
    And I am logged in
    And I have some starting points
    And an objective exists with id 29
    And I own the objective with id 29
    When I attempt to create a hint with the following values for the objective with id 29
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points


  Scenario: Successfully creating a hint as a regular user that has solved the objective in a completed quest
    Given the application is running
    And I am logged in as user with id 3
    And I have some starting points
    And an objective exists with id 10
    And I do not own the objective with id 10
    And I have solved the objective with id 10
    When I attempt to create a hint with the following values for the objective with id 10
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points


  Scenario: Successfully creating a hint as a regular user that has solved the objective
    Given the application is running
    And I am logged in as user with id 8
    And I have some starting points
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have solved the objective with id 18
    When I attempt to create a hint with the following values for the objective with id 18
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points

  Scenario: Successfully creating a hint as a regular user that has solved but not checked in the objective in a current quest
    Given the application is running
    And I am logged in as user with id 7
    And I have some starting points
    And an objective exists with id 31
    And I do not own the objective with id 31
    And I have solved the objective with id 31
    When I attempt to create a hint with the following values for the objective with id 31
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points


  Scenario: Successfully creating a hint as a regular user that has solved and checked in the objective in a current quest
    Given the application is running
    And I am logged in as user with id 9
    And I have some starting points
    And an objective exists with id 24
    And I do not own the objective with id 24
    And I have solved the objective with id 24
    When I attempt to create a hint with the following values for the objective with id 24
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points


  Scenario: Unsuccessfully creating a hint as a regular user for an unsolved objective that I do not own
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have not solved the objective with id 18
    When I attempt to create a hint with the following values for the objective with id 18
      | Message |
      | WEEEEST |
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |


  Scenario: Successfully creating a hint as an admin for an unsolved objective that I do not own
    Given the application is running
    And I am logged in as an admin user
    And I have some starting points
    And an objective exists with id 29
    And I do not own the objective with id 29
    And I have not solved the objective with id 29
    When I attempt to create a hint with the following values for the objective with id 29
      | Message |
      | WEEEEST |
    Then the status code received is 201
    And I have gained points


  Scenario: Unsuccessfully creating a hint when I am not logged in
    Given the application is running
    And I am not logged in
    And a user exists with id 3
    And an objective exists with id 29
    When I attempt to create a hint for user 3 with the following values for the objective with id 29
      | Message |
      | WEEEEST |
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Unsuccessfully creating a hint for an objective that does not exist
    Given the application is running
    And I am logged in
    And I have some starting points
    And an objective does not exist with id 40
    When I attempt to create a hint with the following values for the objective with id 40
      | Message |
      | WEEEEST |
    Then the status code received is 404
    And the following ApiErrors are returned
      | Requested objective not found. |


  Scenario: Successfully retrieving all hints for an objective I have completed
    Given the application is running
    And I am logged in as user with id 7
    And an objective exists with id 29
    And I have solved the objective with id 29
    When I attempt to retrieve all hints for the objective with id 29
    Then the status code received is 200
    And the response contains 2 hints


  Scenario: Successfully retrieving all hints for an objective I have completed with no hints
    Given the application is running
    And I am logged in as user with id 7
    And an objective exists with id 29
    And I have solved the objective with id 29
    When I attempt to retrieve all hints for the objective with id 29
    Then the status code received is 200
    And the response contains 0 hints


  Scenario: Successfully retrieving all hints for an objective I have not completed as an admin
    Given the application is running
    And I am logged in as an admin user
    And an objective exists with id 29
    And I have not solved the objective with id 29
    When I attempt to retrieve all hints for the objective with id 29
    Then the status code received is 200
    And the response contains 2 hints


  Scenario: Unsuccessfully retrieving all hints for an objective I have not completed
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I have not solved the objective with id 18
    When I attempt to retrieve all hints for the objective with id 18
    Then the status code received is 403
    And the following ApiErrors are returned
      | You are not authorized to access this resource. |


  Scenario: Unsuccessfully retrieving all hints for an objective that does not exist
    Given the application is running
    And I am logged in
    And an objective does not exist with id 40
    When I attempt to retrieve all hints for the objective with id 40
    Then the status code received is 404
    And the following ApiErrors are returned
      | Requested objective not found. |


  Scenario: Unsuccessfully retrieving all hints for an objective when not logged in
    Given the application is running
    And I am not logged in
    And an objective exists with id 29
    When I attempt to retrieve all hints for the objective with id 29
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Successfully retrieving a single hint for an objective
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have not solved the objective with id 18
    And a hint with id 4 exists for objective with id 18
    And a hint with id 5 exists for objective with id 18
    And a hint with id 6 exists for objective with id 18
    When I requests a new hint for objective with id 18
    Then the status code received is 200
    And I receive a hint with id 4


  Scenario: Successfully requesting two hints for an objective
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And I do not own the objective with id 18
    And I have not solved the objective with id 18
    And a hint with id 4 exists for objective with id 18
    And a hint with id 5 exists for objective with id 18
    And a hint with id 6 exists for objective with id 18
    When I requests a new hint for objective with id 18
    When I requests a new hint for objective with id 18
    Then the status code received is 200
    And I receive a hint with id 5
    
    
  Scenario: Successfully requesting all seen hints for an objective as a regular user
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And a hint with id 4 exists for objective with id 18
    And a hint with id 5 exists for objective with id 18
    And a hint with id 6 exists for objective with id 18
    When I requests a new hint for objective with id 18
    And I requests a new hint for objective with id 18
    And I request all the hints that I have seen for objective with id 18
    Then the status code received is 200
    And the response contains 2 hints


  Scenario: Successfully requesting all seen hints for an objective as a regular user when I have seen no hints
    Given the application is running
    And I am logged in
    And an objective exists with id 18
    And a hint with id 4 exists for objective with id 18
    And a hint with id 5 exists for objective with id 18
    And a hint with id 6 exists for objective with id 18
    When I request all the hints that I have seen for objective with id 18
    Then the status code received is 200
    And the response contains 0 hints


  Scenario: Successfully requesting all seen hints for an objective as an admin
    Given the application is running
    And I am logged in as an admin user
    And an objective exists with id 18
    And a hint with id 4 exists for objective with id 18
    And a hint with id 5 exists for objective with id 18
    And a hint with id 6 exists for objective with id 18
    When I requests a new hint for objective with id 18
    And I requests a new hint for objective with id 18
    And I request all the hints that I have seen for objective with id 18
    Then the status code received is 200
    And the response contains 2 hints


  Scenario: Unsuccessfully requesting all seen hints when I am not logged in
    Given the application is running
    And I am not logged in
    And a user exists with id 2
    And an objective exists with id 18
    When I request all the hints for user 2 that I have seen for objective with id 18
    Then the status code received is 401
    And the following ApiErrors are returned
      | You are not logged in. |


  Scenario: Unsuccessfully requesting all seen hints for an objective that doesn't exist
    Given the application is running
    And I am logged in
    And an objective does not exist with id 50
    When I request all the hints that I have seen for objective with id 50
    Then the status code received is 404
    And the following ApiErrors are returned
      | Requested objective not found. |


  Scenario: Unsuccessfully requesting all seen hints for a user that doesn't exist
    Given the application is running
    And I am logged in
    And a user does not exist with id 50
    And an objective exists with id 18
    When I request all the hints for user 50 that I have seen for objective with id 18
    Then the status code received is 404
    And the following ApiErrors are returned
      | Requested profile not found. |