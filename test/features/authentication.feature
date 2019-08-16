#Feature: Authentication API Endpoint
#
#  Scenario: Log in using a valid profile
#    Given the application is running
#    When I log in
#    Then the status code received is 200
#
#
#  Scenario: Login using an invalid profile
#    Given the application is running
#    When I send a POST request to the login endpoint with the following credentials
#      | Username          | Password |
#      | ohno@travelea.com | badpass1 |
#    Then the status code received is 401
#
#
#  Scenario: Logging out
#    Given the application is running
#    When I log out
#    Then the status code received is 200