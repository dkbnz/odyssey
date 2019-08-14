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