@Functional @Ingredients
Feature: Delete ingredients
  As a super administrator, I want to delete all ingredients in the system so that I can clean-up the system

  @Edge
  Scenario: Empty ingredients list
    Given an authenticated super administrator
    And no existing ingredient
    When he deletes the ingredients
    Then the ingredients have been deleted
    And no ingredient is left in the ingredients list

  @Security
  Scenario: List with ingredients
    Given an authenticated super administrator
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    When he deletes the ingredients
    Then the ingredients have been deleted
    And no ingredient is left in the ingredients list

  @Security
  Scenario: Delete ingredients as community user
    Given a community user
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    When he deletes the ingredients
    Then an error notifies that user is not authenticated
    And no existing ingredient has been deleted

  @Security
  Scenario: Delete ingredients as community user
    Given an authenticated user
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    When he deletes the ingredients
    Then an error notifies that user is not authorized
    And no existing ingredient has been deleted

  @Security
  Scenario: Delete ingredients as ingredient manager
    Given an authenticated ingredient manager
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    When he deletes the ingredients
    Then an error notifies that user is not authorized
    And no existing ingredient has been deleted
