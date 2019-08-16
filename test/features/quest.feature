Feature: Quest API Endpoint

  Scenario: Successfully creating a quest with valid input as a regular user
    Given the application is running
    And I am logged in
    When I attempt to create a quest using the following json
      """
      {
        "id": null,
        "title": "The Journey Of The Lords",
        "startDate": "2019-08-16 15:02:00+1200 15:02:00+1200 15:02:00+1200 15:02:00+1200",
        "endDate": "2019-08-16 23:59:00+1200 23:59:00+1200 23:59:00+1200 23:59:00+1200",
        "objectives": [
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "What Rhymes with \"sniff cream file week\"?",
            "radius": 1
          },
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "bbbbb",
            "radius": 0.005
          }
        ]
      }
      """
    Then the status code received is 201


  Scenario: Successfully creating a quest with valid input as an admin
    Given the application is running
    And I am logged in as an admin user
    When I attempt to create a quest using the following json
      """
      {
        "id": null,
        "title": "The Journey Of The Lords",
        "startDate": "2019-08-16 15:02:00+1200 15:02:00+1200 15:02:00+1200 15:02:00+1200",
        "endDate": "2019-08-16 23:59:00+1200 23:59:00+1200 23:59:00+1200 23:59:00+1200",
        "objectives": [
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "What Rhymes with \"sniff cream file week\"?",
            "radius": 1
          },
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "bbbbb",
            "radius": 0.005
          }
        ]
      }
      """
    Then the status code received is 201


  Scenario: Unsuccessfully creating a quest with no title
    Given the application is running
    And I am logged in
    When I attempt to create a quest using the following json
    """
      {
        "id": null,
        "title": "",
        "startDate": "2019-08-16 15:02:00+1200 15:02:00+1200 15:02:00+1200 15:02:00+1200",
        "endDate": "2019-08-16 23:59:00+1200 23:59:00+1200 23:59:00+1200 23:59:00+1200",
        "objectives": [
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "What Rhymes with \"sniff cream file week\"?",
            "radius": 1
          },
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "bbbbb",
            "radius": 0.005
          }
        ]
      }
      """
    Then the status code received is 400
    And the following ApiErrors are returned
      | message                         |
      | A quest title must be provided. |


  Scenario: Unsuccessfully creating a quest with valid input but no owner
    Given the application is running
    And I am logged in
    When I attempt to create a quest using the following json
      """
      {
        "id": null,
        "title": "The Journey Of The Lords",
        "startDate": "2019-08-16 15:02:00+1200 15:02:00+1200 15:02:00+1200 15:02:00+1200",
        "endDate": "2019-08-16 23:59:00+1200 23:59:00+1200 23:59:00+1200 23:59:00+1200",
        "objectives": [
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "What Rhymes with \"sniff cream file week\"?",
            "radius": 1
          },
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "bbbbb",
            "radius": 0.005
          }
        ]
      }
      """
    Then the status code received is 404


  Scenario: Unsuccessfully creating a quest with as a regular user for another user
    Given the application is running
    And I am logged in
    When I attempt to create a quest using the following json
      """
      {
        "id": null,
        "title": "The Journey Of The Lords",
        "startDate": "2019-08-16 15:02:00+1200 15:02:00+1200 15:02:00+1200 15:02:00+1200",
        "endDate": "2019-08-16 23:59:00+1200 23:59:00+1200 23:59:00+1200 23:59:00+1200",
        "objectives": [
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "What Rhymes with \"sniff cream file week\"?",
            "radius": 1
          },
          {
            "id": null,
            "destination": {
              "id": 15040
            },
            "riddle": "bbbbb",
            "radius": 0.005
          }
        ]
      }
      """
    Then the status code received is 403



# TODO: Isaac - Waiting on fix for Objectives in a quest, need to add objectives to create and edit in features too.
#  Scenario: Successfully editing a quest with valid input as a regular user
#    Given I am logged in
#    And the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    Then the status code received is 200
#
#
#  Scenario: Successfully editing another user's quests as an admin
#    Given I am logged in
#    And the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And I am not logged in
#    And I am logged in as an admin user
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    Then the status code received is 200
#
#
#  Scenario: Unsuccessfully editing another user's quests as a regular user
#    Given I am logged in
#    And the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And I am not logged in
#    And I am logged in as an alternate user
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    Then the status code received is 403
#
#
#  Scenario: Unsuccessfully editing a quest when not logged in
#    Given I am logged in
#    And the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    And I am not logged in
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Epic Quest  |                    |           |
#    Then the status code received is 401
#
#
#    Scenario: Unsuccessfully editing a quest due to no quest title
#      Given I am logged in
#      And the application is running
#      And a quest already exists with the following values
#        | Title       | Start Date         | End Date  |
#        | Cool Quest  |                    |           |
#      When I attempt to edit the quest with the following values
#        | Title       | Start Date         | End Date  |
#        |             |                    |           |
#      Then the status code received is 400
#
#
#  Scenario: Unsuccessfully editing a quest due to bad dates
#    Given I am logged in
#    And the application is running
#    And a quest already exists with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  |                    |           |
#    When I attempt to edit the quest with the following values
#      | Title       | Start Date         | End Date  |
#      | Cool Quest  | XXX                |  XXX      |
#    Then the status code received is 400