@Functional @Recipes
Feature: Add ingredients to recipe
  As an authenticated user, I want to add ingredients to create a recipe in the system so that I can have an exhaustive
  list of ingredients required to follow the recipe

  @Edge
  Scenario: Add an ingredient to recipe without ingredient identity
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he add an ingredient without identity to the recipe
    Then an error notifies that recipe ingredient must have an identity

  @Edge
  Scenario: Add an unknown ingredient to an existing recipe
    Given an authenticated user
    And a non-existent "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he adds the ingredient to the recipe
    Then an error notifies that ingredient has not been found

  @Edge
  Scenario: Add an existing ingredient to an unknown recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he adds the ingredient to the recipe
    Then an error notifies that recipe has not been found

  @Smoke @Security
  Scenario: Add an existing ingredient to an existing recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe authored by the authenticated user
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list

  @Edge
  Scenario: Add an already contained ingredient to a recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe contains ingredient
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list

  @Security
  Scenario: Add an ingredient to a recipe as community user
    Given a community user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then an error notifies that user is not authenticated
    And the ingredient cannot be found in the recipe's ingredients list

  @Security
  Scenario: Add an ingredient to a recipe authored by another user
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe authored by another user
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then an error notifies that user is not authorized
    And the ingredient cannot be found in the recipe's ingredients list

  @Security
  Scenario: Add an ingredient to a recipe as ingredient manager
    Given an authenticated ingredient manager
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe authored by the authenticated user
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list

  @Security
  Scenario: Add an ingredient to a recipe as super administrator
    Given an authenticated super administrator
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe authored by the authenticated user
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list

  @Security
  Scenario: Add an ingredient to a recipe authored by another user as super administrator
    Given an authenticated super administrator
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe authored by another user
    And recipe does not contain ingredient
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list
