#Feature: Destination Traveller Type API Endpoint
#
#  Scenario: Attempting to add traveller types to public destination as a regular user who owns it.
#    Given I am logged in
#    And I own destination with id 567 and it is public
#    When I set the following traveller types for destination id 567
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 200
#
#
#  Scenario: Attempting to add traveller types to public destination as a regular user who does not own it.
#    Given I am logged in
#    And I do not own destination with id 9293 and it is public
#    When I set the following traveller types for destination id 9293
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 403
#
#
#  Scenario: Suggesting traveller types to public destination as a regular user who does not own it.
#    Given I am logged in
#    And I do not own destination with id 9293 and it is public
#    When I suggest the following traveller types for destination id 9293
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 200
#
#
#  Scenario: Suggesting traveller types to public destination as a regular user who owns it.
#    Given I am logged in
#    And I own destination with id 567 and it is public
#    When I suggest the following traveller types for destination id 567
#      | 2 |
#      | 3 |
#      | 4 |
#      | 7 |
#    Then the status code received is 200
#
#
#  Scenario: Suggesting traveller types to private destination as a regular user who owns it.
#    Given I am logged in
#    And I own destination with id 325 and it is private
#    When I suggest the following traveller types for destination id 325
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 200
#
#
#  Scenario: Attempting to add traveller types to private destination as an admin user who owns it.
#    Given I am logged in as an admin user
#    And I own destination with id 9355 and it is private
#    When I set the following traveller types for destination id 9355
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 200
#
#
#  Scenario: Attempting to add traveller types to private destination as an admin user who does not own it.
#    Given I am logged in as an admin user
#    And I do not own destination with id 325 and it is private
#    When I set the following traveller types for destination id 325
#      | 1 |
#      | 3 |
#      | 4 |
#      | 5 |
#    Then the status code received is 200
#
#
#  Scenario: Admin requesting proposed destinations in the admin panel for a proposed add
#    Given the application is running
#    And I am logged in as an admin user
#    And There is a destination with one traveller type to add
#    When A request for proposed destinations is sent
#    Then the status code received is 200
#    And There is a destination to update
#
#
#  Scenario: Admin requesting proposed destinations in the admin panel for a proposed remove
#    Given the application is running
#    And I am logged in as an admin user
#    And There is a destination with one traveller type assigned
#    And There is a destination with one traveller type to remove
#    When A request for proposed destinations is sent
#    Then the status code received is 200
#    And There is a destination to update
#
#
#  Scenario: Admin requesting proposed destinations in the admin panel for a proposed add and remove
#    Given the application is running
#    And I am logged in as an admin user
#    And There is a destination with one traveller type assigned
#    And There is a destination with one traveller type to add
#    When A request for proposed destinations is sent
#    Then the status code received is 200
#    And There is a destination to update