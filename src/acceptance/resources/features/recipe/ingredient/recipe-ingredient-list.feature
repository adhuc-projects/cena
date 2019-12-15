@Functional @Recipes
Feature: List recipe's ingredients
  As a community user, I want to list recipe's ingredients so that I can buy ingredients before following the recipe

  Scenario: Empty recipe's ingredients list
    Given a community user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And the recipe contains no ingredient
    When he lists the recipe's ingredients
    Then the recipe's ingredients list is empty

  @Smoke
  Scenario: Recipe's ingredients list with ingredients
    Given a community user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
    And the following ingredients in the recipe
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
    When he lists the recipe's ingredients
    Then the recipe's ingredients list contains the ingredients
