Feature: Having a profile system


  Scenario: Get all profiles
    Given the application is running
    When I send a GET request to the /profiles endpoint
    Then the received status code is ok()


  Scenario: Get all nationalities
    Given the application is running [2]
    When I send a GET request to the /travtypes endpoint
    Then the received status code is ok() [2]


  Scenario: Attempting to sign up with an existing username
    Given I am connected to the TravelEA database
    And The username "GimmeErrors" exists within the TravelEA database
    When A new username, "GimmeErrors", is passed through the ProfileController
    Then the status code received is 400

