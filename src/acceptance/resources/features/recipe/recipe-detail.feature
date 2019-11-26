@Functional @Recipes
Feature: Retrieve recipe details
  As a community user, I want to retrieve a specific recipe in the system so that I can follow it

  Scenario: Unknown recipe
    Given a community user
    And the following existing recipes
          | name                                   | content                                                                                                                                  |
          | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                |
          | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes |
          | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             |
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he attempts retrieving "Tomato, cucumber and mozzarella salad" recipe
    Then an error notifies that recipe has not been found

  @Smoke
  Scenario: Known recipe
    Given a community user
    And the following existing recipes
      | name                                   | content                                                                                                                                  |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             |
    When he retrieves "Tomato, cucumber and mozzarella salad" recipe
    Then the recipe details is accessible
