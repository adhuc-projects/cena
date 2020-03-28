@Functional @Recipes
Feature: List recipe's ingredients
  As a community user, I want to list recipe's ingredients so that I can buy ingredients before following the recipe

  @Edge
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
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
      | Chives     | AT_CONVENIENCE   |
    And the following ingredients in the recipe
      | name       | isMainIngredient | quantity | measurementUnit |
      | Tomato     | true             | 3        | UNIT            |
      | Cucumber   | true             | 1        | UNIT            |
      | Mozzarella | true             | 200      | GRAM            |
      | Chives     | false            | 1        | PINCH           |
    When he lists the recipe's ingredients
    Then the recipe's ingredients list contains the ingredients
