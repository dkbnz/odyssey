Feature: Authentication API Endpoint

  Scenario: Log in using a valid profile
    Given I am running the application
    When I send a POST request to the login endpoint with the admin credentials
    Then I receive an OK status code


  Scenario: Login using an invalid profile
    Given I am running the application
    When I send a POST request to the login endpoint with the following credentials
      | Username          | Password |
      | ohno@travelea.com | badpass1 |
    Then the received status code is BadRequest


  Scenario: Logging out
    Given I am running the application
    When I send a POST request to the logout endpoint
    Then I receive an OK status code