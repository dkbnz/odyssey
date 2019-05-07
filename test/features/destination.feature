Feature: Destination API Endpoint

  Scenario: Get all destinations
    Given I have a running application
    And I am logged in
    When I send a GET request to the destinations endpoint
    Then the status code received is OK

  Scenario: Create a new destination with valid input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following valid values
    | Name    | Type | District | Latitude  | Longitude| Country     |
    | ASB     | 2    | Nelson   | 24.5      | 34.6     | New Zealand |
    Then the status code received is OK
#
#  Scenario: Create a new destination with invalid input
#    Given I have a running application
#    And I am logged in
#    When I create a new destination with the following invalid values
#      | Name    | Type | District | Latitude  | Longitude| Country    |
#      |         | BANK | Nelson   | 24.5      | 34.6     | New Zealand|
#    Then the status code received is BadRequest
#
#
#  Scenario: Create a destination with duplicated input
#    Given I have a running application
#    And I am logged in
#    And a destination already exists with the name "Duplicate" and district "Nelson"
#    When I create a new destination with the following duplicated values
#      | Name      | Type | District | Latitude  | Longitude| Country    |
#      | Duplicate | BANK | Nelson   | 24.5      | 34.6     | New Zealand|
#    Then the status code received is BadRequest