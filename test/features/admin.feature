Feature: Having a admin system

  Scenario: Attempting to create a new user from the admin panel
    Given I have a running application
    And An admin is logged in
    And The following profile does not exist with username "adminNew@travelea.com" within the TravelEA database
    When An admin attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | adminNew@travelea.com    | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then I receive a status code of 201

  Scenario: Admin updating a non-admin user successfully
    Given I am logged in as an admin
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then I receive a status code of 200

  Scenario: Admin updating a non-admin user unsuccessfully
    Given I am logged in as an admin
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user exists in the database with the id 4 and username "testuser2@email.com"
    When I change the username of the user with id 2 to "testuser2@email.com"
    Then I receive a status code of 400

  Scenario: Regular user updating another user unsuccessfully
    Given I am logged in as a regular user
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then I receive a status code of 403