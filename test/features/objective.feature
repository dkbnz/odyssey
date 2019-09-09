Feature: Objective API Endpoint

  Scenario: Successfully get all Objectives
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And the status code received is 201
    When I request to retrieve all objectives
    Then the status code received is 200
    And the response contains at least one objective


  Scenario: Successfully creating a new Objective
    Given the application is running
    And I am logged in
    When I attempt to create a objective with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    Then the status code received is 201
    And the objective is successfully created


  Scenario: Successfully creating a new Objective as an admin for another user
    Given the application is running
    And I am logged in as an admin user
    When I attempt to create a objective with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    Then the status code received is 201
    And the objective is successfully created


  Scenario: Unsuccessfully creating a new Objective with no destination
    Given the application is running
    And I am logged in
    When I attempt to create a objective with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | null        | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    Then the status code received is 400


  Scenario: Unsuccessfully creating a new Objective with no riddle
    Given the application is running
    And I am logged in
    When I attempt to create a objective with the following values
      | Destination | Riddle | Owner | Radius |
      | 119         |        | 2     | 0.005  |
    Then the status code received is 400


  Scenario: Successfully editing a Objective
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And the status code received is 201
    When I attempt to edit the objective with the following values
      | Destination | Riddle          | Owner | Radius |
      | 119         | Does this work? | 2     | 0.005  |
    Then the status code received is 200


  Scenario: Unsuccessfully editing a Objective with no destination
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And the status code received is 201
    When I attempt to edit the objective with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | null        | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    Then the status code received is 400



  Scenario: Unsuccessfully editing a Objective for another user as non-admin
    Given the application is running
    And I am logged in as an admin user
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 1     | 0.005  |
    And I am not logged in
    And I am logged in
    When I attempt to edit the objective with the following values
      | Destination | Riddle          | Owner | Radius |
      | 119         | Does this work? | 1     | 0.005  |
    Then the status code received is 403


  Scenario: Delete an already existing Objective that I own
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    When I attempt to delete the objective
    Then the status code received is 200


  Scenario: Delete an already existing Objective as an admin
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And I am logged in as an admin user
    When I attempt to delete the objective
    Then the status code received is 200


  Scenario: Unsuccessfully delete an already existing Objective that I don't own
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And I am logged in as an alternate user
    When I attempt to delete the objective
    Then the status code received is 403


  Scenario: Unsuccessfully delete an already existing Objective and I am not logged in
    Given the application is running
    And I am logged in
    And a objective already exists with the following values
      | Destination | Riddle                                 | Owner | Radius |
      | 119         | What rhymes with It's mean Kyle fleek? | 2     | 0.005  |
    And I am not logged in
    When I attempt to delete the objective
    Then the status code received is 401