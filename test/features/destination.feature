Feature: Destination API Endpoint

  Scenario: Get all destinations
    Given I have a running application
    When I send a GET request to the destinations endpoint
    Then the status code received is OK

  Scenario: Create a new destination with valid input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following valid values
      | Name    | Type | District | Latitude  | Longitude| Country     |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New Zealand |
    Then the received status code is Created

  Scenario: Create a new destination with invalid name input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following invalid values
      | Name    | Type | District | Latitude  | Longitude| Country    |
      |         | 3    | Nelson   | 24.5      | 34.6     | New Zealand|
    Then the status code received is BadRequest

  Scenario: Create a new destination with invalid country input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following invalid values
      | Name    | Type | District | Latitude  | Longitude| Country    |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New 1?!    |
    Then the status code received is BadRequest

  Scenario: Create a destination with duplicated input
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name      | Type | District | Latitude  | Longitude| Country    |
      | Duplicate | 3    | Nelson   | 24.5      | 34.6     | New Zealand|
    When I create a new destination with the following duplicated values
      | Name      | Type | District | Latitude  | Longitude| Country    |
      | Duplicate | 3    | Nelson   | 24.5      | 34.6     | New Zealand|
    Then the status code received is BadRequest

  Scenario: Search for a destination by name that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name    | Type | District | Latitude  | Longitude| Country     |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New Zealand |
    When I search for a destination with name
      | Name    |
      | ASB     |
    Then the status code received is OK
    And the response contains at least one destination with name
      | Name    |
      | ASB     |

  Scenario: Search for a destination by district that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name    | Type | District | Latitude  | Longitude| Country     |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New Zealand |
    When I search for a destination with district
      | District    |
      | Nelson      |
    Then the status code received is OK
    And the response contains at least one destination with district
      | District    |
      | Nelson      |

  Scenario: Search for a destination by latitude that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name    | Type | District | Latitude  | Longitude| Country     |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New Zealand |
    When I search for a destination with latitude
      | Latitude    |
      | 24.5        |
    Then the status code received is OK
    And the response contains at least one destination with latitude
      | Latitude    |
      | 24.5        |

  Scenario: Search for a private destinations
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name    | Type | District | Latitude  | Longitude| Country     | is_public  |
      | ASB     | 3    | Nelson   | 24.5      | 34.6     | New Zealand | false      |
    When I search for a destination with name
      | Name    |
      | ASB     |
    Then the status code received is OK
    And the response is empty