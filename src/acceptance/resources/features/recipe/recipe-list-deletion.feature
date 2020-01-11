@Functional @Recipes
Feature: Delete recipes
  As a super administrator, I want to delete all recipes in the system so that I can clean-up the system

  @Edge
  Scenario: Empty recipes list
    Given an authenticated super administrator
    And no existing recipe
    When he deletes the recipes
    Then the recipes have been deleted
    And no recipe is left in the recipes list

  @Security
  Scenario: List with recipes
    Given an authenticated super administrator
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
    When he deletes the recipes
    Then the recipes have been deleted
    And no recipe is left in the recipes list

  @Security
  Scenario: Delete ingredients as community user
    Given a community user
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
    When he deletes the recipes
    Then an error notifies that user is not authenticated
    And no existing recipe has been deleted

  @Security
  Scenario: Delete ingredients as authenticated user
    Given an authenticated user
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
    When he deletes the recipes
    Then an error notifies that user is not authorized
    And no existing recipe has been deleted

  @Security
  Scenario: Delete ingredients as ingredient manager
    Given an authenticated ingredient manager
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
    When he deletes the recipes
    Then an error notifies that user is not authorized
    And no existing recipe has been deleted
