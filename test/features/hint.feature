Feature: Hint API Endpoint.

  Scenario: Successfully creating a hint as the owner of the objective
    Given the application is running
    And I am logged in
    And an objective exists with id 29
    And I own the objective with id 29
    When I attempt to create a hint with the following values for the objective with id 29
      | Message |
      | WEEEEST |
    Then the status code received is 201


  Scenario: Successfully creating a hint as a regular user that has solved the objective
    Given the application is running
    And I am logged in
    And an objective exists with id 30
    And I do not own the objective with id 30
    And I have solved the objective with id 30
    When I attempt to create a hint with the following values for the objective with id 30
      | Message |
      | WEEEEST |
    Then the status code received is 201

#  TODO: Matilda and Vinnie
#  Scenario: Unsuccessfully creating a hint as a regular user for an unsolved objective that I do not own
#    Given the application is running
#    And I am logged in
#    And an objective exists with id 18
#    And I do not own the objective with id 18
#    And I have not solved the objective with id 18
#    When I attempt to create a hint with the following values for the objective with id 18
#      | Message |
#      | WEEEEST |
#    Then the status code received is 403
#    And the following ApiErrors are returned
#     | You are not authorized to access this resource. |


  Scenario: Successfully creating a hint as an admin for an unsolved objective that I do not own
    Given the application is running
    And I am logged in as an admin user
    And an objective exists with id 29
    And I do not own the objective with id 29
    And I have not solved the objective with id 29
    When I attempt to create a hint with the following values for the objective with id 29
      | Message |
      | WEEEEST |
    Then the status code received is 201


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
    And an objective does not exist with id 40
    When I attempt to create a hint with the following values for the objective with id 40
      | Message |
      | WEEEEST |
    Then the status code received is 404
    And the following ApiErrors are returned
      | Requested objective not found. |