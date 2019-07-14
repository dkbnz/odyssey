Feature: Having a profile system


  Scenario: Get all profiles
    Given I have a running application
    And I have logged in
    When I send a GET request to the profiles endpoint
    Then the status code is OK

  Scenario: Get all nationalities
    Given the application is running
    When I send a GET request to the /nationalities endpoint
    Then the status code is OK

  Scenario: Get all traveller types
    Given the application is running
    When I send a GET request to the /travtypes endpoint
    Then the status code is OK

  Scenario: Attempting to sign up with an existing username
    Given I have a running application
    And I have logged in
    And The following profile exists with username "guestUser@travelea.com" within the TravelEA database:
    When A user attempts to create a profile with the following fields:
      | username               | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | guestUser@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code is BadRequest

  Scenario: Attempting to sign up with a new username
    Given I have a running application
    And The following profile does not exist with the username "TestUser123@travelea.com" within the TravelEA database
    When A user attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | TestUser123@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code is Created

  Scenario: Attempting to update an existing user
    Given I have a running application
    And The following profile exists with username "guestUser@travelea.com" within the TravelEA database:
    When The user attempts to update their profile information within the TravelEA database:
      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender | nationality | traveller_type | passport_country |
      | guestUser@travelea.com | guest123    | Dave       | Test        | McInloch     | 1998-10-18    | Other  | 1           | 1              | 1                |
    Then the status code is OK