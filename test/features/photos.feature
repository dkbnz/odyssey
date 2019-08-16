#Feature: Photos API Endpoint
#
#  Scenario: Successfully uploading a photo to my own profile
#    Given the application is running
#    And I am logged in
#    When I upload a valid jpeg photo to a profile with id 2
#    Then the status code received is 201
#
#
#  Scenario: Successfully uploading a photo to another user's profile as Admin
#    Given the application is running
#    And I am logged in as an admin user
#    And a user exists in the database with the username "testuser1@email.com" and id number 3
#    When I upload a valid jpeg photo to a profile with id 2
#    Then the status code received is 201
#
#
#  Scenario: Failing to upload a photo to another user's profile as a regular user
#    Given the application is running
#    And I am logged in
#    And a user exists in the database with the username "testuser1@email.com" and id number 3
#    When I upload a valid jpeg photo to a profile with id 1
#    Then the status code received is 403
#
#
#  Scenario: Updating my own Personal Photo from public to private
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 1
#    When I change the privacy of the photo with id 2 to public "false"
#    Then the status code received is 200
#
#
#  Scenario: Updating my own Personal Photo from private to public
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 1
#    When I change the privacy of the photo with id 2 to public "true"
#    Then the status code received is 200
#
#
#  Scenario: Admin updating another user's photo
#    Given the application is running
#    And I am logged in as an admin user
#    And a photo exists with id 2
#    When I change the privacy of the photo with id 2 to public "true"
#    Then the status code received is 200
#
#
#  Scenario: Failing to change the privacy of another user's photo as a regular user
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 1
#    And a user exists in the database with the username "admin@travelea.com" and id number 1
#    When I change the privacy of the photo with id 1 to public "true"
#    Then the status code received is 403
#
#
#  Scenario: Deleting my own photo successfully
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 2
#    When I delete the photo with id 2
#    Then the status code received is 200
#
#
#  Scenario: Admin deleting another users photo successfully
#    Given the application is running
#    And I am logged in as an admin user
#    And a photo exists with id 2
#    When I delete the photo with id 2
#    Then the status code received is 200
#
#
#  Scenario: Regular user deleting another users photos unsuccessfully
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 1
#    When I delete the photo with id 1
#    Then the status code received is 403
#
#
#  Scenario: Successfully uploading a profile photo to my own profile from my gallery
#    Given the application is running
#    And I am logged in
#    When I set a profile photo from their photo Gallery with id 2
#    Then the status code received is 200
#
#
#  Scenario: Uploading a profile photo to another persons profile from their gallery as a regular user unsuccessfully
#    Given the application is running
#    And I am logged in
#    When I set a profile photo from their photo Gallery with id 1
#    Then the status code received is 403
#
#
#  Scenario: Successfully uploading a profile photo to another persons profile from their gallery as an admin user
#    Given the application is running
#    And I am logged in as an admin user
#    When I set a profile photo from their photo Gallery with id 2
#    Then the status code received is 200
#
#
#  Scenario: Successfully deleting my own profile picture
#    Given the application is running
#    And I am logged in
#    When I delete a profile picture of profile 2
#    Then the status code received is 200
#
#
#  Scenario: Admin successfully deleting another user's profile
#    Given the application is running
#    And I am logged in as an admin user
#    When I delete a profile picture of profile 2
#    Then the status code received is 200
#
#
#  Scenario: User deleting another users profile picture unsuccessfully
#    Given the application is running
#    And I am logged in
#    When I delete a profile picture of profile 1
#    Then the status code received is 403
#
#
#  Scenario: User is adding one of their own Personal Photos to a destination
#    Given the application is running
#    And I am logged in
#    And the destination with id 119 exists
#    And a photo exists with id 2
#    When I add a photo with id 2 to a destination with id 119
#    Then the status code received is 201
#
#
#  Scenario: User is adding someone else's Personal Photo to a destination
#    Given the application is running
#    And I am logged in
#    And the destination with id 119 exists
#    And a photo exists with id 1
#    When I add a photo with id 1 to a destination with id 119
#    Then the status code received is 403
#
#
#  Scenario: Admin is adding another user's Personal Photo to a destination
#    Given the application is running
#    And I am logged in as an admin user
#    And the destination with id 119 exists
#    And a photo exists with id 2
#    When I add a photo with id 2 to a destination with id 119
#    Then the status code received is 201
#
#
#  Scenario: User adding a photo to a destination that doesn't exist
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 2
#    When I add a photo with id 2 to a destination with id 9999
#    Then the status code received is 404
#
#
#  Scenario: User adding a photo that doesn't exist to a destination
#    Given the application is running
#    And I am logged in
#    And the destination with id 119 exists
#    When I add a photo with id 50 to a destination with id 119
#    Then the status code received is 404
#
#
#  Scenario: User is removing one of their own Personal Photos from a destination
#    Given the application is running
#    And I am logged in
#    And the destination with id 119 exists
#    And the destination with id 119 has a photo with id 3
#    When I remove a photo with id 3 from a destination with id 119
#    Then the status code received is 200
#
#
#  Scenario: User is removing another user's Personal Photos from a destination
#    Given the application is running
#    And I am logged in
#    And the destination with id 119 exists
#    And the destination with id 119 has a photo with id 4
#    When I remove a photo with id 4 from a destination with id 119
#    Then the status code received is 403
#
#
#  Scenario: Admin is removing another user's Personal Photo from a destination
#    Given the application is running
#    And I am logged in as an admin user
#    And the destination with id 119 exists
#    And the destination with id 119 has a photo with id 3
#    When I remove a photo with id 3 from a destination with id 119
#    Then the status code received is 200
#
#
#  Scenario: Deleting my own photo successfully when it is associated with a destination
#    Given the application is running
#    And I am logged in
#    And a photo exists with id 5
#    And the destination with id 119 exists
#    And the destination with id 119 has a photo with id 5
#    When I delete the photo with id 5
#    Then the status code received is 200