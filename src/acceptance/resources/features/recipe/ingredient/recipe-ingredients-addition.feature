@Functional @Recipes
Feature: Add ingredients to recipe
  As an authenticated user, I want to add ingredients to create a recipe in the system so that I can have an exhaustive
  list of ingredients required to follow the recipe

  @Smoke @Security
  Scenario: Add a single existing ingredient to an existing recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And no relation between ingredient and recipe
    When he adds the ingredient to the recipe
    Then the ingredient is added to the recipe
    And the ingredient can be found in the recipe's ingredients list