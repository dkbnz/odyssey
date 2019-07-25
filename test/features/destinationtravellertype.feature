Feature: Suggesting traveller types for a destination.

  Scenario: Adding traveller types to private destination as a regular user.
    Given
    When
    Then

  Scenario: Adding traveller types to private destination as an admin user.
    Given
    When
    Then

  Scenario: Suggesting traveller types to public destination as a regular user.
    Given
    When
    Then


  Scenario: Admin requesting proposed destinations in the admin panel where there is 1 destination to update
    Given The application is operational
    And The user is logged in as an admin
    And There is a destination with one traveller type to add
    When A request for proposed destinations is sent
    Then the status code received on the admin panel is OK
    And There is 1 destination to update