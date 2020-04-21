@Functional @Menus
Feature: Create a menu for a meal
  As an authenticated user, I want to create a menu for a meal so that I can plan my meals and shopping list

  @Edge
  Scenario: Create a menu without date
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he creates a menu from the recipe without date
    Then an error notifies that menu must have a date
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario: Create a menu without meal type
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he creates a menu from the recipe without meal type
    Then an error notifies that menu must have a meal type
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario: Create a menu with invalid meal type
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he creates a menu from the recipe with unknown "UNKNOWN" meal type
    Then an error notifies that menu cannot be created with unknown "UNKNOWN" meal type
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario: Create a menu without covers
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And no existing menu for today's lunch
    When he creates a menu from the recipe without covers for today's lunch
    Then an error notifies that menu must have covers
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario Outline: Create a menu with invalid <covers> covers
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And no existing menu for today's lunch
    When he creates a menu from the recipe for <covers> covers for today's lunch
    Then an error notifies that menu cannot be created with invalid <covers> covers
    And the menu cannot be found in the menus list starting from today

    Examples:
      | covers |
      | -1     |
      | 0      |

  @Edge
  Scenario: Create a menu without main course recipe
    Given an authenticated user
    And no existing menu for today's lunch
    When he creates a menu without main course recipe for today's lunch
    Then an error notifies that menu must have main course recipes
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario: Create a menu with unknown main course recipe
    Given an authenticated user
    And no existing menu for today's lunch
    When he creates a menu from unknown recipe for today's lunch
    Then an error notifies that menu cannot be linked to unknown recipe
    And the menu cannot be found in the menus list starting from today

  @Edge
  Scenario: Create an already existing menu
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And an existing menu from the recipe for today's lunch
    When he creates a menu from the recipe for 2 covers for today's lunch
    Then an error notifies that menu already exists

  @Smoke @Security
  Scenario: Create a menu successfully
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And no existing menu for today's lunch
    When he creates a menu from the recipe for 2 covers for today's lunch
    Then the menu is created
    And the menu can be found in the menus list starting from today

  @Edge
  Scenario: Create a menu with many main course recipes
    Given an authenticated user
    And the following existing recipes
      | name                                   | content                                                   | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it               | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it | 6        |
    And no existing menu for today's lunch
    When he creates a menu from the recipes for 2 covers for today's lunch
    Then the menu is created
    And the menu can be found in the menus list starting from today

  @Security
  Scenario: Create a menu as community user
    Given a community user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he tries to create a menu from the recipe for 2 covers for today's lunch
    Then an error notifies that user is not authenticated
