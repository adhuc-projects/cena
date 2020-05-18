@Functional @Recipes
Feature: Create a recipe in the system
  As an authenticated user, I want to create a recipe in the system so that I can retrieve it later to follow it

  @Edge
  Scenario: Create a recipe without name
    Given an authenticated user
    When he creates a recipe without name
    Then an error notifies that recipe must have a name
    And the recipe cannot be found in the list

  @Edge
  Scenario: Create a recipe without content
    Given an authenticated user
    When he creates a recipe without content
    Then an error notifies that recipe must have a content
    And the recipe cannot be found in the list

  @Smoke @Security
  Scenario: Create a recipe successfully
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then the recipe is created
    And the recipe can be found in the list

  @Edge
  Scenario: Create a recipe without number of servings
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe without number of servings
    Then the recipe is created
    And the recipe can be found in the list

  @Edge
  Scenario: Create a recipe without course types
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe without course types
    Then the recipe is created
    And the recipe can be found in the list

  @Edge
  Scenario: Create a recipe with unknown course type
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe with the following course types
      | STARTER     |
      | MAIN_COURSE |
      | UNKNOWN     |
    Then an error notifies that recipe cannot be created with unknown "UNKNOWN" course type
    And the recipe cannot be found in the list

  @Security
  Scenario: Create a recipe as community user
    Given a community user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then an error notifies that user is not authenticated
    And the recipe cannot be found in the list

  @Security
  Scenario: Create a recipe as ingredient manager
    Given an authenticated ingredient manager
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then the recipe is created
    And the recipe can be found in the list

  @Security
  Scenario: Create a recipe as super administrator
    Given an authenticated super administrator
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then the recipe is created
    And the recipe can be found in the list
