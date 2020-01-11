@Functional @Recipes
Feature: List recipes
  As a community user, I want to list recipes in the system so that I can select a recipe I would like to follow

  @Edge
  Scenario: Empty recipes list
    Given a community user
    And no existing recipe
    When he lists the recipes
    Then the recipes list is empty

  @Smoke
  Scenario: List with recipes
    Given a community user
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
    When he lists the recipes
    Then the recipes list contains the existing recipes
