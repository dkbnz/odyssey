Feature: Profile API Endpoint

  Scenario: Get all profiles
    Given the application is running
    And I am logged in
    When I send a GET request to the profiles endpoint
    Then the status code received is 200


  Scenario: Get all nationalities
    Given the application is running
    When I send a GET request to the nationalities endpoint
    Then the status code received is 200


  Scenario: Get all traveller types
    Given the application is running
    When I send a GET request to the travtypes endpoint
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
    Given the application is running
    Given I am logged in as an admin user
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then the status code received is 200


  Scenario: Admin unsuccessfully updating a non-admin user
    Given the application is running
    And I am logged in as an admin user
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user exists in the database with the id 4 and username "testuser2@email.com"
    When I change the username of the user with id 2 to "testuser2@email.com"
    Then the status code received is 400


  Scenario: Regular user unsuccessfully updating another user
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 3 and username "testuser1@email.com"
    And a user does not exist with the username "testuser10@email.com"
    When I change the username of the user with id 3 to "testuser10@email.com"
    Then the status code received is 403


  Scenario: Searching for all profiles by nationality
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "nationalities" with value "New Zealand"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by first name
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "name" with value "Default"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by middle name
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 2 and username "guestUser@travelea.com"
    When I search for profiles by "name" with value "John"
    Then the status code received is 200
    And the response contains the profile with username "guestUser@travelea.com"


  Scenario: Searching for all profiles by last name
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "name" with value "Admin"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by traveller type
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "travellerTypes" with value "Functional/Business"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by gender
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "gender" with value "Male"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by min age
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "min_age" with value "1"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by max age
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "max_age" with value "120"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for all profiles by max age which is too high
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    When I search for profiles by "max_age" with value "200"
    Then the status code received is 400


  Scenario: Searching for profiles by min number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "4800"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching unsuccessfully for profiles by min number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "7000"
    Then the status code received is 200
    And the response does not contain the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by max number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "max_points" with value "5001"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching unsuccessfully for profiles by max number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "max_points" with value "4999"
    Then the status code received is 200
    And the response does not contain the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by min and max number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "3000" and by "max_points" with value "7000"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by exact number of points
    Given the application is running
    And I am logged in
    And a user exists in the database with the id 1 and username "admin@travelea.com"
    And the user 1 has 5000 points
    When I search for profiles by "min_points" with value "5000" and by "max_points" with value "5000"
    Then the status code received is 200
    And the response contains the profile with username "admin@travelea.com"


  Scenario: Searching for profiles by rank
    Given the application is running
    And I am logged in
    And the following users exist in the database:
      | id  | username                |
      | 1   | admin@travelea.com      |
      | 2   | guestUser@travelea.com  |
      | 3   | testuser1@email.com     |
      | 4   | testuser2@email.com     |
      | 5   | testuser3@email.com     |
      | 6   | testuser4@email.com     |
      | 7   | testuser5@email.com     |
      | 8   | testuser6@email.com     |
      | 9   | testuser7@email.com     |
      | 10  | testuser8@email.com     |
      | 11  | testuser9@email.com     |
    And the users have the following points
      | id  | points  |
      | 1   | 10      |
      | 2   | 20      |
      | 3   | 30      |
      | 4   | 40      |
      | 5   | 50      |
      | 6   | 60      |
    When I search for profiles by "rank" with value "4"
    Then the status code received is 200
    And the response contains the following profiles:
      | username                |
      | testuser1@email.com     |
      | testuser2@email.com     |
      | testuser3@email.com     |
      | testuser4@email.com     |
      | testuser5@email.com     |

    And the response does not contain the following profiles:
      | username                |
      | admin@travelea.com      |
      | guestUser@travelea.com  |


  Scenario: Searching for profiles by rank with tied profiles
    Given the application is running
    And I am logged in
    And the following users exist in the database:
      | id  | username                |
      | 1   | admin@travelea.com      |
      | 2   | guestUser@travelea.com  |
      | 3   | testuser1@email.com     |
      | 4   | testuser2@email.com     |
      | 5   | testuser3@email.com     |
      | 6   | testuser4@email.com     |
    And the users have the following points
      | id  | points  |
      | 1   | 10      |
      | 2   | 10      |
      | 3   | 10      |
      | 4   | 40      |
      | 5   | 40      |
      | 6   | 40      |
    When I search for profiles by "rank" with value "3"
    Then the status code received is 200
    And the response contains the following profiles:
      | username                |
      | testuser2@email.com     |
      | testuser3@email.com     |
      | testuser4@email.com     |
      | testuser5@email.com     |
      | testuser6@email.com     |
    And the response does not contain the following profiles:
      | username                |
      | admin@travelea.com      |
      | testuser1@email.com     |
      | guestUser@travelea.com  |


  Scenario: Searching for profiles by rank where no profiles have points
    Given the application is running
    And I am logged in
    And the following users exist in the database:
      | id  | username                |
      | 1   | admin@travelea.com      |
      | 2   | guestUser@travelea.com  |
      | 3   | testuser1@email.com     |
      | 4   | testuser2@email.com     |
      | 5   | testuser3@email.com     |
      | 6   | testuser4@email.com     |
    And the users have the following points
      | id  | points  |
      | 1   | 0       |
      | 2   | 0       |
      | 3   | 0       |
      | 4   | 0       |
      | 5   | 0       |
      | 6   | 0       |
    When I search for profiles by "rank" with value "1"
    Then the status code received is 200
    And the response contains the following profiles:
      | username                |
      | testuser5@email.com     |
      | testuser6@email.com     |
      | testuser7@email.com     |


  Scenario: Searching for all profiles with a valid page size
    Given the application is running
    And I am logged in
    And at least 7 profiles exist
    When I search for profiles by "pageSize" with value "7"
    Then the status code received is 200
    And the response contains 7 profiles


  Scenario: Searching for all profiles with a page size that is too large
    Given the application is running
    And I am logged in
    And at least 11 profiles exist
    When I search for profiles by "pageSize" with value "106"
    Then the status code received is 200
    And the response contains 11 profiles


  Scenario: Searching for all profiles with an invalid page size
    Given the application is running
    And I am logged in
    When I search for profiles by "pageSize" with value "Infinity"
    Then the status code received is 400
    And the following ApiErrors are returned
    | Invalid page size provided. |