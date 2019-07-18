Feature: Destination API Endpoint

  Scenario: Get all destinations
    Given I have a running application
    And I am logged in
    When I send a GET request to the destinations endpoint
    Then the status code received is OK

  Scenario: Create a new destination with valid input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is Created

  Scenario: Create a new destination with invalid name input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      |      | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is Bad Request

  Scenario: Create a new destination with invalid country input
    Given I have a running application
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New 1?! |
    Then the status code received is Bad Request

  Scenario: Create a destination that already exists in my private destinations
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | Duplicate | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    When I create a new destination with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | Duplicate | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Bad Request

  Scenario: Create a destination that already exists as a public destination
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I create a new destination with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is Bad Request

  Scenario: Create a destination that already exists as a private destination for another user
    Given I have a running application
    And I am logged in
    And a destination already exists for user 3 with the following values
      | Name          | Type | District | Latitude | Longitude | Country     | is_public |
      | DuplicatePriv | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    When I create a new destination with the following values
      | Name          | Type | District | Latitude | Longitude | Country     | is_public |
      | DuplicatePriv | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Created

  Scenario: Create a new destination with valid input for another user
    Given I have a running application
    And I am logged in as an admin user
    When I create a new destination with the following values for another user
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is Created

  Scenario: Create a destination for another user that already exists as a public destination
    Given I have a running application
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I create a new destination with the following values for another user
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is Bad Request

  Scenario: Search for a destination by name that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with name
      | Name |
      | ASB  |
    Then the status code received is OK
    And the response contains at least one destination with name
      | Name |
      | ASB  |

  Scenario: Search for a destination by district that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with district
      | District |
      | Nelson   |
    Then the status code received is OK
    And the response contains at least one destination with district
      | District |
      | Nelson   |

  Scenario: Search for a destination by latitude that exists
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with latitude
      | Latitude |
      | 24.5     |
    Then the status code received is OK
    And the response contains at least one destination with latitude
      | Latitude |
      | 24.5     |

  Scenario: Search for a private destinations
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     | is_public |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    When I search for a destination with name
      | Name |
      | ASB  |
    Then the status code received is OK
    And the response is empty

  Scenario: Search for all destinations
    Given I have a running application
    And I am logged in
    And a destination already exists with the following values
      | Name     | Type | District | Latitude | Longitude | Country     | is_public |
      | ASB      | 3    | Nelson   | 24.5     | 34.6      | New Zealand | true      |
      | Big      | 4    | Gore     | 24.5     | 34.6      | New Zealand | true      |
      | Phloomis | 5    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
      | Styles   | 3    | Bally    | 24.5     | 34.6      | New Zealand | true      |
    When I search for all destinations
    Then the status code received is OK
    And the response contains only public destinations

  Scenario: Search for destinations by owner
    Given I have a running application
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name     | Type | District | Latitude | Longitude | Country     |
      | ASB      | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
      | Phloomis | 5    | Nelson   | 24.5     | 34.6      | New Zealand |
      | Styles   | 3    | Bally    | 24.5     | 34.6      | New Zealand |
    And a destination already exists for user 2 with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | Big  | 4    | Gore     | 24.5     | 34.6      | New Zealand |
    When I search for all destinations by user 2
    Then the status code received is OK
    And the response contains only destinations owned by the user with id 2

  Scenario: Attempt to edit a private destination while not logged in
    Given I have a running application
    And I am not logged in
    And a destination has been created with the following values
      | Name       |
      | Canterbury |
    When I attempt to edit the destination using the following values
      | Type | District | Latitude  | Longitude  | Country     |
      | 4    | Sydney   | 33.838306 | 151.002007 | Australia |
    Then the status code received is Unauthorised

  Scenario: Attempt to edit a private destination as the owner
    Given I have a running application
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Type | District | Latitude  | Longitude  | Country   |
      | 3    | Sydney   | 33.838306 | 151.002007 | Australia |
    Then the status code received is OK

  Scenario: Attempt to edit a private destination as another user
    Given I am running the application
    And I am logged in
    And a destination already exists for user 1 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | District | Country   |
      | Sydney   | Australia |
    Then the status code received is Forbidden

  Scenario: Attempt to edit a destination that does not exist
    Given I am running the application
    And I am logged in
    When I attempt to edit the destination using the following values
      | District | Country |
      | Sydney | Australia |
    Then the status code received is Not Found

  Scenario: Attempt to edit a destination using an incorrect latitude value
    Given I am running the application
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name      | Type | District     | Latitude  | Longitude| Country     | is_public |
      | University| 4    | Christchurch | 24.5      | 34.6     | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Latitude  |
      | 100       |
    Then the status code is BadRequest

  Scenario: Attempt to edit a destination using an incorrect longitude value
    Given I am running the application
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name      | Type | District     | Latitude  | Longitude| Country     | is_public |
      | University| 4    | Christchurch | 24.5      | 34.6     | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Longitude |
      | 200       |
    Then the status code is BadRequest

  Scenario: Attempt to edit a destination using an incorrect field name
    Given I am running the application
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name      | Type | District     | Latitude  | Longitude| Country     | is_public |
      | University| 4    | Christchurch | 24.5      | 34.6     | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Typ | Ditsrict     |
      | 5   | Christchurch |
    Then the status code received is BadRequest

  Scenario: Attempt to delete a private destination as the owner when it is not used
    Given I am running the application
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is OK

  Scenario: Attempt to delete a private destination as the owner when it is used
    Given I am running the application
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And a destination with the following values is used
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Bad Request

  Scenario: Attempt to delete a public destination as the owner when it is not used
    Given I am running the application
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    Then the status code received is OK

  Scenario: Attempt to delete a public destination as the owner when it is used
    Given I am running the application
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And a destination with the following values is used
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    Then the status code received is Bad Request

  Scenario: Attempt to delete a destination that does not exist
    Given I am running the application
    And I am logged in
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Not Found

  Scenario: Attempt to delete a private destination as another user
    Given I am running the application
    And I am logged in
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Forbidden

  Scenario: Attempt to delete a public destination as another user
    Given I am running the application
    And I am logged in
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    Then the status code received is Forbidden

  Scenario: Attempt to delete a private destination as an admin
    Given I am running the application
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is OK

  Scenario: Attempt to delete a destination when not logged in
    Given I am running the application
    And I am not logged in
    When I attempt to delete the destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is Unauthorised
