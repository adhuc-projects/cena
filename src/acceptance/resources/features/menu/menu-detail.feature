@Functional @Menus
Feature: Retrieve menu details
  As an authenticated user, I want to retrieve a specific menu so that I can follow its recipes and list needed ingredients

  Background:
    Given the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Pizza Regina                           | Pizza Regina made easy                                                                                                                   | 2        |

  @Edge
  Scenario: Unknown menu
    Given an authenticated user
    And the following existing menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
    And no existing menu for today's lunch
    When he attempts retrieving the menu scheduled for tomorrow's lunch
    Then an error notifies that menu has not been found

  @Edge
  Scenario: Other user menu
    Given an authenticated ingredient manager
    And the following existing menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | today              | lunch    | 4      | Pizza Regina                           |
    And no existing menu for today's lunch
    When he attempts retrieving the menu scheduled for today's lunch
    Then an error notifies that menu has not been found

  @Smoke
  Scenario: Known recipe
    Given an authenticated user
    And the following existing menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today              | lunch    | 4      | Pizza Regina                           |
      | user  | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
    When he retrieves the menu scheduled for today's lunch
    Then the menu details are accessible

  @Edge
  Scenario: Get menu detail as community user
    Given a community user
    When he attempts retrieving the menu scheduled for today's lunch
    Then an error notifies that user is not authenticated
