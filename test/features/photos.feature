Feature: CRUD of photos

  Scenario: Successfully uploading a photo to my own profile
    Given I have a application running
    And I am logged in as a non-admin
    When I upload a valid jpeg photo to my own profile
    Then the status code I get is Created


  Scenario: Successfully uploading a photo to another user's profile as Admin
    Given I have a application running
    And I am logged in as an admin
    And a user exists in the database with the username "testuser1@email.com" and id 3
    When I upload a valid jpeg photo to another user's profile
    Then the status code I get is Created

  Scenario: Failing to upload a photo to another user's profile as a regular user
    Given I have a application running
    And I am logged in as a non-admin
    And a user exists in the database with the username "testuser1@email.com" and id 3
    When I upload a valid jpeg photo to another user's profile
    Then the status code I get is Forbidden