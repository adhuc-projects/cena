@Functional @Recipes
Feature: List recipes filtered by ingredient
  As a community user, I want to list recipes in the system composed of an ingredient so that I can select a recipe based on an ingredient I want to use

  @Edge
  Scenario: List recipes filtered by unknown ingredient
    Given a community user
    And the following existing ingredients
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings | courseTypes          |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        | STARTER, MAIN_COURSE |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        | STARTER, MAIN_COURSE |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        | MAIN_COURSE          |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        | STARTER, MAIN_COURSE |
    When he lists the recipes composed of unknown ingredient
    Then an error notifies that ingredient has not been found

  @Edge
  Scenario: Empty recipes list filtered by ingredient
    Given a community user
    And the following existing ingredients
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
    And no existing recipe
    When he lists the recipes composed of ingredient "Cucumber"
    Then the recipes list is empty

  @Edge
  Scenario: Empty recipes list filtered by non used ingredient
    Given a community user
    And the following existing ingredients
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
      | Olive oil  | VOLUME           |
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings | courseTypes          |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        | STARTER, MAIN_COURSE |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        | STARTER, MAIN_COURSE |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        | MAIN_COURSE          |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        | STARTER, MAIN_COURSE |
    And the following ingredients in recipes
      | recipe                                 | ingredient | isMainIngredient | quantity | measurementUnit |
      | Tomato, cucumber and mozzarella salad  | Tomato     | true             | 3        | UNIT            |
      | Tomato, cucumber and mozzarella salad  | Cucumber   | true             | 1        | UNIT            |
      | Tomato, cucumber and mozzarella salad  | Mozzarella | true             | 200      | GRAM            |
      | Tomato, cucumber, olive and feta salad | Tomato     | true             | 10       | UNIT            |
      | Tomato, cucumber, olive and feta salad | Cucumber   | true             | 3        | UNIT            |
      | Tomato and cantal pie                  | Tomato     | true             | 3        | UNIT            |
    When he lists the recipes composed of ingredient "Olive oil"
    Then the recipes list is empty

  @Smoke
  Scenario: List recipes filtered by ingredient
    Given a community user
    And the following existing ingredients
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings | courseTypes          |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        | STARTER, MAIN_COURSE |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        | STARTER, MAIN_COURSE |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        | MAIN_COURSE          |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        | STARTER, MAIN_COURSE |
    And the following ingredients in recipes
      | recipe                                 | ingredient | isMainIngredient | quantity | measurementUnit |
      | Tomato, cucumber and mozzarella salad  | Tomato     | true             | 3        | UNIT            |
      | Tomato, cucumber and mozzarella salad  | Cucumber   | true             | 1        | UNIT            |
      | Tomato, cucumber and mozzarella salad  | Mozzarella | true             | 200      | GRAM            |
      | Tomato, cucumber, olive and feta salad | Tomato     | true             | 10       | UNIT            |
      | Tomato, cucumber, olive and feta salad | Cucumber   | true             | 3        | UNIT            |
      | Tomato and cantal pie                  | Tomato     | true             | 3        | UNIT            |
    When he lists the recipes composed of ingredient "Cucumber"
    Then the recipes list contains the following recipes
      | name                                   |
      | Tomato, cucumber and mozzarella salad  |
      | Tomato, cucumber, olive and feta salad |
    And the recipes list does not contain the following recipes
      | name                  |
      | Tomato and cantal pie |
      | Quiche lorraine       |
