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
    And The following profile exists within the TravelEA database:
      | username               | password    | first_name | middle_name | last_name | date_of_birth | gender |
      | guestUser@travelea.com | TestPass123 | Test       |             | Dummy     | 2000-01-01    | Other  |
    When A user attempts to create a profile with the following fields:
      | username               | password    | first_name | middle_name | last_name | date_of_birth | gender |
      | guestUser@travelea.com | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  |
    Then the status code received is BadRequest
#
#  Scenario: Attempting to sign up with a new username
#    Given I am connected to the TravelEA database
#    And The following profile does not exist within the TravelEA database:
#      | username    | password    | first_name | middle_name | last_name | date_of_birth | gender |
#      | TestUser123 | TestPass123 | Test       |             | Dummy     | 2000-01-01    | Other  |
#    When A user attempts to create a profile with the following fields:
#      | username    | password    | first_name | middle_name | last_name | date_of_birth | gender |
#      | TestUser123 | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  |
#    Then the status code received is 201
#
#  Scenario: Attempting to update an existing user
#    Given I am connected to the TravelEA database
#    And The following profile exists within the TravelEA database:
#      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender |
#      | guestUser@travelea.com | guest123    | Dave       |             | McInloch     | 1998-10-18    | Other  |
#    When The user attempts tp update their profile information within the TravelEA database:
#      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender |
#      | guestUser@travelea.com | guest123    | Dave       | Test        | McInloch     | 1998-10-18    | Other  |
#    Then the status code received is 200
#
#  Scenario: Attempting to delete an existing user
#    Given I am connected to the TravelEA database
#    And The following profile exists within the TravelEA database:
#      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender |
#      | guestUser@travelea.com | guest123    | Dave       |             | McInloch     | 1998-10-18    | Other  |
#    When The user attempts to delete their profile information within the TravelEA database:
#      | username               | password    | first_name | middle_name | last_name    | date_of_birth | gender |
#      | guestUser@travelea.com | guest123    | Dave       | Test        | McInloch     | 1998-10-18    | Other  |
#    Then the status code received is 200