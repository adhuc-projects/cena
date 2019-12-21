@Functional @Recipes
Feature: Delete a recipe from the system
  As a super administrator, I want to delete a recipe in the system so that recipe list is not polluted with misconfigured recipes

  @Edge
  Scenario: Delete an unknown recipe
    Given an authenticated super administrator
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he attempts deleting the recipe
    Then an error notifies that recipe has not been found

  @Smoke @Security
  Scenario: Delete a recipe successfully
    Given an authenticated super administrator
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he deletes the recipe
    Then the recipe is deleted
    And the recipe cannot be found in the list

  @Security
  Scenario: Delete a recipe as community user
    Given a community user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he deletes the recipe
    Then an error notifies that user is not authenticated
    And the recipe can be found in the list

  @Security
  Scenario: Delete an ingredient as authenticated user
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he deletes the recipe
    Then an error notifies that user is not authorized
    And the recipe can be found in the list

  @Security
  Scenario: Delete an ingredient as ingredient manager
    Given an authenticated ingredient manager
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he deletes the recipe
    Then an error notifies that user is not authorized
    And the recipe can be found in the list
