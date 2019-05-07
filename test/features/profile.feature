#Feature: Having a profile system
#
#
#  Scenario: Get all profiles
#    Given I have a running application
#    And I have logged in
#    When I send a GET request to the profiles endpoint
#    Then the status code is OK
#
#
##  Scenario: Get all nationalities
##    Given the application is running [2]
##    When I send a GET request to the /travtypes endpoint
##    Then the received status code is ok() [2]s
##
##
##  Scenario: Attempting to sign up with an existing username
##    Given I am connected to the TravelEA database
##    And The following profile exists within the TravelEA database:
##      | username    | password    | first_name | middle_name | last_name | date_of_birth | gender |
##      | TestUser123 | TestPass123 | Test       |             | Dummy     | 2000-01-01    | other  |
##    When A user attempts to create a profile with the following fields:
##      | username    | password    | first_name | middle_name | last_name | date_of_birth | gender |
##      | TestUser123 | TestPass321 | Test       |             | Dummy     | 2000-01-01    | other  |
##    Then the status code received is 400