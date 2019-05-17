Feature: Having a admin system

  Scenario: Attempting to create a new user from the admin panel
    Given I have a running application
    And An admin is logged in
    And The following profile does not exist with username "adminNew@travelea.com" within the TravelEA database
    When An admin attempts to create a profile with the following fields:
      | username                 | password    | first_name | middle_name | last_name | date_of_birth | gender | nationality | traveller_type | passport_country |
      | adminNew@travelea.com    | TestPass321 | Test       |             | Dummy     | 2000-01-01    | Other  | 1           | 1              | 1                |
    Then the status code is Created
