@Functional @Menus
Feature: Delete a menu from the system
  As an authenticated user, I want to delete a menu so that I can cancel scheduled menu

  Background:
    Given the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Pizza Regina                           | Pizza Regina made easy                                                                                                                   | 2        |

  @Edge
  Scenario: Delete an unknown menu
    Given an authenticated user
    And the following existing menus
      | owner | date      | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today     | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow  | dinner   | 4      | Tomato and cantal pie                  |
    And no existing menu for today's lunch
    When he attempts deleting the menu scheduled for tomorrow's lunch
    Then an error notifies that menu has not been found

  @Edge
  Scenario: Delete other user menu
    Given an authenticated ingredient manager
    And the following existing menus
      | owner | date  | mealType | covers | mainCourseRecipes |
      | user  | today | lunch    | 4      | Pizza Regina      |
    And no existing menu for today's lunch
    When he attempts deleting the menu scheduled for today's lunch
    Then an error notifies that menu has not been found

  @Smoke @Security
  Scenario: Delete a menu successfully
    Given an authenticated user
    And the following existing menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today              | lunch    | 4      | Pizza Regina                           |
      | user  | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
    When he deletes the menu scheduled for today's lunch
    Then the menu is deleted
    And the menu cannot be found in the menus list

  @Security
  Scenario: Delete a menu as community user
    Given a community user
    When he attempts deleting the menu scheduled for today's lunch
    Then an error notifies that user is not authenticated
