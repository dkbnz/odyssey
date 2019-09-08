Feature: Profile API Endpoint

  Scenario: Get all profiles
    Given the application is running
    And I am logged in
    When I send a GET request to the profiles endpoint
    Then the status code received is 200


  Scenario: Get all nationalities
    Given the application is running
    When I send a GET request to the /nationalities endpoint
    Then the status code received is 200


  Scenario: Get all traveller types
    Given the application is running
    When I send a GET request to the /travtypes endpoint
    Then the status code received is 200


  Scenario: Attempting to sign up with an existing username
    Given the application is running
    And I am logged in
    And The following profile exists with username "guestUser@travelea.com" within the TravelEA database:
    When A user attempts to create a profile with the following fields:
      | username               | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | guestUser@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code received is 400


  Scenario: Attempting to sign up with a new username
    Given the application is running
    And The following profile does not exist with the username "TestUser123@travelea.com" within the TravelEA database
    When A user attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | TestUser123@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code received is 201


  Scenario: Attempting to sign up with a formatted email
    Given the application is running
    And The following profile does not exist with the username "Test.User.123@travelea.com" within the TravelEA database
    When A user attempts to create a profile with the following fields:
      | username                    | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | Test.User.123@travelea.com  | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code received is 201


  Scenario: Attempting to update an existing user
    Given the application is running
    And The following profile exists with username "guestUser@travelea.com" within the TravelEA database:
    When The user attempts to update their profile information within the TravelEA database:
      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender | nationality | traveller_type | passport_country |
      | guestUser@travelea.com | guest123    | Dave       | Test        | McInloch     | 1998-10-18    | Other  | 1           | 1              | 1                |
    Then the status code received is 200


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


  Scenario: Searching for all profiles by nationality
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "nationalities" with value "New Zealand"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by first name
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "name" with value "Default"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by middle name
    Given I am logged in
    And a user exists in the database with the id 2 and username "guestUser@travelea.com"
    When I search for profiles by "name" with value "John"
    Then the status code received is 200
    And the response contains the profile with username "guestUser@travelea.com"


  Scenario: Searching for all profiles by last name
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "name" with value "Admin"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by traveller type
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "travellerTypes" with value "Functional/Business"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by gender
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "gender" with value "Male"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by min age
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "min_age" with value "1"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by max age
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "max_age" with value "120"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by max age which is too high
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "max_age" with value "200"
    Then the status code received is 400


  Scenario: Searching for profiles by min number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "4800"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching unsuccessfully for profiles by min number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "7000"
    Then the status code received is 200
    And the response does not contain the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by max number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "max_points" with value "5001"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching unsuccessfully for profiles by max number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "max_points" with value "4999"
    Then the status code received is 200
    And the response does not contain the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by min and max number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "3000" and by "max_points" with value "7000"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by exact number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "5000" and by "max_points" with value "5000"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching successfully for all profiles by number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user has 5000 points
    When I search for profiles with "5000" points
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching unsuccessfully for all profiles by number of points
    Given I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user has 5 points
    When I search for profiles with "5000" points
    Then the status code received is 200
    And the response is empty