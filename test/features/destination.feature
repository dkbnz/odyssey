Feature: Destination API Endpoint

  Scenario: Get all destinations
    Given the application is running
    And I am logged in
    When I send a GET request to the destinations endpoint
    Then the status code received is 200


  Scenario: Create a new destination with valid input
    Given the application is running
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201


  Scenario: Create a new destination with invalid name input
    Given the application is running
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      |      | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 400


  Scenario: Create a new destination with invalid country input
    Given the application is running
    And I am logged in
    When I create a new destination with the following values
      | Name | Type | District | Latitude | Longitude | Country |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New 1?! |
    Then the status code received is 400


  Scenario: Create a destination that already exists in my private destinations
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | Duplicate | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    When I create a new destination with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | Duplicate | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is 400


  Scenario: Create a destination that already exists as a public destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I create a new destination with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 400


  Scenario: Create a destination that already exists as a private destination for another user
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name          | Type | District | Latitude | Longitude | Country     | is_public |
      | DuplicatePriv | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    When I create a new destination with the following values
      | Name          | Type | District | Latitude | Longitude | Country     | is_public |
      | DuplicatePriv | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    Then the status code received is 201


  Scenario: Create a new destination with valid input for another user
    Given the application is running
    And I am logged in as an admin user
    When I create a new destination with the following values for another user
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 201


  Scenario: Create a destination for another user that already exists as a public destination
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I create a new destination with the following values for another user
      | Name       | Type | District | Latitude | Longitude | Country     |
      | DuplicateP | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    Then the status code received is 400


  Scenario: Search for a destination by name that exists
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with name
      | Name |
      | ASB  |
    Then the status code received is 200
    And the response contains at least one destination with name
      | Name |
      | ASB  |


  Scenario: Search for a destination by district that exists
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with district
      | District |
      | Nelson   |
    Then the status code received is 200
    And the response contains at least one destination with district
      | District |
      | Nelson   |


  Scenario: Search for a destination by latitude that exists
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand |
    When I search for a destination with latitude
      | Latitude |
      | 24.5     |
    Then the status code received is 200
    And the response contains at least one destination with latitude
      | Latitude |
      | 24.5     |


  Scenario: Search for a private destinations
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name | Type | District | Latitude | Longitude | Country     | is_public |
      | ASB  | 3    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
    And I am not logged in
    And I am logged in as an alternate user
    When I search for a destination with name
      | Name |
      | ASB  |
    Then the status code received is 200
    And the response is empty


  Scenario: Search for all destinations
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name     | Type | District | Latitude | Longitude | Country     | is_public |
      | ASB      | 3    | Nelson   | 24.5     | 34.6      | New Zealand | true      |
      | Big      | 4    | Gore     | 24.5     | 34.6      | New Zealand | true      |
      | Phloomis | 5    | Nelson   | 24.5     | 34.6      | New Zealand | false     |
      | Styles   | 3    | Bally    | 24.5     | 34.6      | New Zealand | true      |
    When I search for all destinations
    Then the status code received is 200
    And the response contains only own or public destinations


  Scenario: Search for destinations by owner
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | ASB       | 3    | Nelson   | 24.5     | 34.6      | New Zealand | true      |
      | Phloomis  | 5    | Nelson   | 24.5     | 34.6      | New Zealand | true      |
      | Styles    | 3    | Bally    | 24.5     | 34.6      | New Zealand | true      |
    And a destination already exists for user 2 with the following values
      | Name      | Type | District | Latitude | Longitude | Country     | is_public |
      | Big       | 4    | Gore     | 24.5     | 34.6      | New Zealand | true      |
    When I search for all destinations by user 2
    Then the status code received is 200
    And the response contains only destinations owned by the user with id 2


  Scenario: Attempt to edit a private destination while not logged in
    Given the application is running
    And I am not logged in
    When I attempt to edit destination 10000 using the following values
      | Type | District | Latitude  | Longitude  | Country      |
      | 4    | Sydney   | 33.838306 | 151.002007 | Australia    |
    Then the status code received is 401


  Scenario: Attempt to edit a private destination as the owner
    Given the application is running
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Type | District | Latitude  | Longitude  | Country   |
      | 3    | Sydney   | 33.838306 | 151.002007 | Australia |
    Then the status code received is 200


  Scenario: Attempt to edit another user's private destination as Admin
    Given the application is running
    And I am logged in as an admin user
    When I attempt to edit destination 10000 using the following values
      | District | Country   |
      | Sydney   | Australia |
    Then the status code received is 200


  Scenario: Attempt to edit a private destination as another user
    Given the application is running
    And I am logged in
    When I attempt to edit destination 10000 using the following values
      | District | Country   |
      | Sydney   | Australia |
    Then the status code received is 403


  Scenario: Attempt to edit a destination that does not exist
    Given the application is running
    And I am logged in
    When I attempt to edit destination -4 using the following values
      | District | Country |
      | Sydney | Australia |
    Then the status code received is 404


  Scenario: Attempt to edit a destination using an incorrect latitude value
    Given the application is running
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name      | Type | District     | Latitude  | Longitude| Country     | is_public |
      | University| 4    | Christchurch | 24.5      | 34.6     | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Latitude  |
      | 100       |
    Then the status code received is 400


  Scenario: Attempt to edit a destination using an incorrect longitude value
    Given the application is running
    And I am logged in
    And a destination already exists for user 2 with the following values
      | Name      | Type | District     | Latitude  | Longitude| Country     | is_public |
      | University| 4    | Christchurch | 24.5      | 34.6     | New Zealand | false     |
    When I attempt to edit the destination using the following values
      | Longitude |
      | 200       |
    Then the status code received is 400


  Scenario: Attempt to delete a private destination as the owner when it is not used
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a private destination as the owner when it is only used by the owner
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 2
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a public destination as the owner when it is not used
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a public destination as the owner when it is only used by the owner
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And the destination has a photo with id 2
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a public destination as the owner when it is used by another user
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And I am not logged in
    And I am logged in as an alternate user
    And the destination has a photo with id 6
    And I am not logged in
    And I am logged in
    When I attempt to delete the destination
    Then the status code received is 403


  Scenario: Attempt to delete a destination that does not exist
    Given the application is running
    And I am logged in
    When I attempt to delete the destination with id 100
    Then the status code received is 404


  Scenario: Attempt to delete a private destination as another user
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And I am not logged in
    And I am logged in
    When I attempt to delete the destination
    Then the status code received is 403


  Scenario: Attempt to delete a public destination as another user
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And I am not logged in
    And I am logged in
    When I attempt to delete the destination
    Then the status code received is 403


  Scenario: Attempt to delete a private destination as an admin
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a used public destination as an admin
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists for user 3 with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a public destination as an admin when it is used by another user
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And the destination has a photo with id 2
    When I attempt to delete the destination
    Then the status code received is 200


  Scenario: Attempt to delete a destination when not logged in
    Given the application is running
    And I am not logged in
    When I attempt to delete the destination with id 119
    Then the status code received is 401


  Scenario: Deleting a destination as the owner deletes the trip
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination is used in trip "Trip 1"
    When I attempt to delete the destination
    Then the status code received is 200
    And the trip with name "Trip 1" is deleted


  Scenario: Previous owner uses a private destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 2
    Then the owner is user 2


  Scenario: Previous owner uses a public destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And the destination has a photo with id 2
    Then the owner is user 2


  Scenario: Another user uses a public destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And I am not logged in
    And I am logged in as an alternate user
    When I add a photo with id 6 to the destination
    Then the owner is user 1


  Scenario: Merging two destinations which have photos
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 2
    And I am not logged in
    And I am logged in as an alternate user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 6
    When I attempt to edit the destination using the following values
      | is_public |
      | true      |
    Then the destination will have photos with the following ids
      | id    |
      | 2     |
      | 6     |


  Scenario: Merging three destinations which have photos
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 2
    And I am not logged in
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 1
    And I am not logged in
    And I am logged in as an alternate user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a photo with id 6
    When I attempt to edit the destination using the following values
      | is_public |
      | true      |
    Then the destination will have photos with the following ids
      | id    |
      | 1     |
      | 2     |
      | 6     |


  Scenario: Merging two destinations are used in trips
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination is used in trip "Trip 1"
    And I am not logged in
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination is used in trip "Trip 2"
    When I attempt to edit the destination using the following values
      | is_public |
      | true      |
    Then the destination will be used in the following trips
      | Trip name   |
      | Trip 1      |
      | Trip 2      |


  Scenario: Merging two destinations that have traveller types
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a set traveller type with id 1
    And I am not logged in
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a set traveller type with id 2
    When I attempt to edit the destination using the following values
      | is_public |
      | true      |
    Then the destination will have the following traveller types
      |Traveller Type |
      | 1             |
      | 2             |


  Scenario: Merging two destinations that have proposed traveller types to add
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a proposed traveller type with id 1
    And I am not logged in
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And the destination has a set traveller type with id 1
    And the destination has a proposed traveller type with id 2
    When I attempt to edit the destination using the following values
      | is_public |
      | true      |
    Then the destination will have the following traveller types
      |Traveller Type |
      | 1             |
    And the destination will have the following proposed traveller types to add
      |Traveller Type |
      | 2             |


  Scenario: Merging two public destinations upon editing one to be identical to the other
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | chch         | 24.5     | 34.6      | New Zealand | true      |
    When I attempt to edit the destination using the following values
      | District     |
      | Christchurch |
    Then the status code received is 200
    And there is only one destination with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |


#  Scenario: Merging two destinations that have treasure hunts
#    Given the application is running
#    And I am logged in
#    And a destination already exists with the following values
#      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
#      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
#    And I am not logged in
#    And I am logged in as an admin user
#    And a destination already exists with the following values
#      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
#      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
#    And the destination exists in a treasure hunt with the following values
#      | Riddle                                 | Start Date | End Date | Owner |
#      | What rhymes with It's mean Kyle fleek? |            |          | 2     |
#    When I attempt to edit the destination using the following values
#      | is_public |
#      | true      |
#    Then the destination will have the following number of treasure hunts 1
#
#
#  Scenario: Merging two destinations that both have treasure hunts
#    Given the application is running
#    And I am logged in
#    And a destination already exists with the following values
#      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
#      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
#    And the destination exists in a treasure hunt with the following values
#      | Riddle                                | Start Date | End Date | Owner |
#      | What rhymes with It's mean Kyle Reek? |            |          | 2     |
#    And I am not logged in
#    And I am logged in as an admin user
#    And a destination already exists with the following values
#      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
#      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
#    And the destination exists in a treasure hunt with the following values
#      | Riddle                                 | Start Date | End Date | Owner |
#      | What rhymes with It's mean Kyle fleek? |            |          | 2     |
#    When I attempt to edit the destination using the following values
#      | is_public |
#      | true      |
#    Then the destination will have the following number of treasure hunts 2


  Scenario: Retrieving destination usage for 1 trip
    Given the application is running
    And I am logged in as an admin user
    And the following json body containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : "1990-12-12",
              "end_date" : "1991-12-12"
            },
            {
              "destination_id" : "567",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    When I request the destination usage for destination with id 1155
    Then the status code received is 200
    And the trip count is 1
    And the number of trips received is 1
    And the photo count is 0


  Scenario: Retrieving destination usage for 1 photo
    Given the application is running
    And I am logged in as an admin user
    And a photo exists with id 1
    When I add a photo with id 1 to an existing destination with id 1155
    And I request the destination usage for destination with id 1155
    Then the status code received is 200
    And the trip count is 0
    And the number of trips received is 0
    And the photo count is 1


  Scenario: Retrieving destination usage for 0 photos and 0 trips
    Given the application is running
    And I am logged in as an admin user
    When I request the destination usage for destination with id 1155
    Then the status code received is 200
    And the trip count is 0
    And the number of trips received is 0
    And the photo count is 0


  Scenario: Retrieving destination usage for 1 photo and 1 trip
    Given the application is running
    And I am logged in as an admin user
    And the following json body containing a trip is sent:
      """
        {
          "trip_name": "A Holiday Away",
          "trip_destinations" : [
            {
              "destination_id" : "1155",
              "start_date" : "1990-12-12",
              "end_date" : "1991-12-12"
            },
            {
              "destination_id" : "567",
              "start_date" : null,
              "end_date" : null
            }
          ]
        }
      """
    When I add a photo with id 1 to an existing destination with id 1155
    And I request the destination usage for destination with id 1155
    Then the status code received is 200
    And the trip count is 1
    And the number of trips received is 1
    And the photo count is 1


  Scenario: Getting the destination usage of the changed value for a destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | New Place  | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | New Dup    | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    When I change the value of the destination name to 'New Place' and I request the destination usage for edited destination
    Then the status code received is 200
    And the number of destinations received is at least 1


  Scenario: Getting the destination usage of the changed value for a destination as non-owner
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | New Place  | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And I am logged in as an alternate user
    When I change the value of the destination name to 'New Place' and I request the destination usage for edited destination
    Then the status code received is 403


  Scenario: Getting the destination usage of the changed value for a destination when not logged in
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | New Place  | 4    | Christchurch | 24.5     | 34.6      | New Zealand | false     |
    And I am not logged in
    When I change the value of the destination name to 'New Place' and I request the destination usage for edited destination
    Then the status code received is 401


  Scenario: Transferring the ownership of a public destination to default admin when photo is added by another user
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And I am not logged in
    And I am logged in as an alternate user
    And the destination has a photo with id 6
    Then the owner is user 1


  Scenario: The ownership is not changed when the owner adds a photo to their own un-used public destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | University | 4    | Christchurch | 24.5     | 34.6      | New Zealand | true      |
    And the destination has a photo with id 3
    Then the owner is user 2


  Scenario: Retrieving destination usage when not logged in
    Given the application is running
    And I am not logged in
    And a destination already exists with the following values
      | Name       | Type | District     | Latitude | Longitude | Country     | is_public |
      | Angus Flat | 31   | Canterbury   | -43.65598| 170.48378 | New Zealand | true      |
    When I request the destination usage for destination with id 119
    Then the status code received is 401


  Scenario: Retrieving destination usage for a destination that doesn't exist
    Given the application is running
    And I am logged in
    When I request the destination usage for destination with id 1
    Then the status code received is 404


  Scenario: Retrieving destination usage for my own private destination
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name                      | Type  | District         | Latitude   | Longitude     | Country     |
      | Baylys Beach Post Office  | 10    | North Auckland   | -35.953527 | 173.74573     | New Zealand |
    And I request the destination usage for destination with id 325
    Then the status code received is 200


  Scenario: Retrieving destination usage for a private destination as another user
    Given the application is running
    And I am logged in
    And a destination already exists with the following values
      | Name           | Type  | District         | Latitude   | Longitude     | Country     |
      | Private Glade  | 39    | Canterbury       | -44.1625   | 170.993056    | New Zealand |
    When I request the destination usage for destination with id 9001
    Then the status code received is 403


  Scenario: Retrieving destination usage for a private destination an admin
    Given the application is running
    And I am logged in as an admin user
    And a destination already exists with the following values
      | Name                      | Type  | District         | Latitude   | Longitude     | Country     |
      | Baylys Beach Post Office  | 10    | North Auckland   | -35.953527 | 173.74573     | New Zealand |
    And I request the destination usage for destination with id 325
    Then the status code received is 200