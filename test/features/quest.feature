Feature: Quest API Endpoint
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

  # TODO: Matthew and Matilda
#  Scenario: Retrieve all quests I have created
#    Given I am logged in
#    And the application is running
#    When I attempt to retrieve my quests
#
#
#  Scenario: Retrieve quests when I have none created
#    Given I am logged in as an alternate user
#    And the application is running
#
#  Scenario: Retrieve quests when I am not logged in
#    Given I am not logged in
#    And the application is running
#    When I attempt to retrieve my quests