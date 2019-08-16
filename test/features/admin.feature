Feature: Admin API Endpoint

  Scenario: Attempting to create a new user from the admin panel
    Given the application is running
    And I am logged in as an admin user
    And a user does not exist with the username "adminNew@travelea.com"
    When An admin attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | adminNew@travelea.com    | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code received is 201


  Scenario: Admin successfully updating a non-admin user
    Given I am logged in as an admin user
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then the status code received is 200


  Scenario: Admin unsuccessfully updating a non-admin user
    Given I am logged in as an admin user
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user exists in the database with the id 4 and username "testuser2@email.com"
    When I change the username of the user with id 2 to "testuser2@email.com"
    Then the status code received is 400


  Scenario: Regular user unsuccessfully updating another user
    Given I am logged in
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then the status code received is 403