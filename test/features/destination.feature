Feature: Destination API Endpoint

  Scenario: Get all destinations
    Given I have a running application
    When I send a GET request to the destinations endpoint
    Then the status code received is OK

  Scenario:
    Given I have a running application
    When I create a new destination with the following values
    | Name    | Type | District | Latitude  | Longitude| Country    |
    | ASB     | Bank | Nelson   | 24.5      | 34.6     | New Zealand|
    Then the status code received is OK

  Scenario:
    Given I have a running application
    When I create a new destination with the following values
      | Name    | Type | District | Latitude  | Longitude| Country    |
      | ASB     | Bank | Nelson   | 24.5      | 34.6     | New Zealand|
    Then the status code received is OK