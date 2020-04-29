@Functional @Menus
Feature: List menus for a period of time
  As an authenticated user, I want to list menus so that I can retrieve the menus I planned for a period of time

  @Edge
  Scenario: Empty menus list
    Given an authenticated user
    And no existing menu
    When he lists the menus
    Then the menus list is empty

  @Smoke
  Scenario: List with menus for default period of time
    Given an authenticated user
    And the following existing recipes
      | name                                   | content                                                                                                                                  | servings |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        |
      | Gazpacho                               | Gazpacho made easy                                                                                                                       | 4        |
      | Chili con carne                        | Chili con carne made easy                                                                                                                | 8        |
      | Moussaka                               | Moussaka made easy                                                                                                                       | 4        |
      | Lasagna                                | Lasagna made easy                                                                                                                        | 8        |
      | Pizza Regina                           | Pizza Regina made easy                                                                                                                   | 2        |
      | Watercress soup                        | Watercress soup made easy                                                                                                                | 4        |
      | Stuffed tomatoes                       | Stuffed tomatoes made easy                                                                                                               | 4        |
      | Vegetarian fajitas                     | Vegetarian fajitas made easy                                                                                                             | 2        |
      | Norwegian salad                        | Norwegian salad made easy                                                                                                                | 2        |
      | Caesar salad                           | Caesar salad made easy                                                                                                                   | 4        |
    And the following existing menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday          | lunch    | 4      | Moussaka                               |
      | user  | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today              | lunch    | 4      | Pizza Regina                           |
      | user  | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | user  | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
      | user  | today + 3          | dinner   | 4      | Gazpacho                               |
      | user  | today + 4          | dinner   | 4      | Caesar salad                           |
      | user  | today + 5          | dinner   | 4      | Watercress soup                        |
      | user  | today + 6          | lunch    | 4      | Lasagna                                |
      | user  | today + 6          | dinner   | 4      | Norwegian salad                        |
      | user  | today + 7          | lunch    | 4      | Chili con carne                        |
      | user  | today + 7          | dinner   | 4      | Stuffed tomatoes                       |
    When he lists the menus
    Then the menus list contains the following menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | today              | lunch    | 4      | Pizza Regina                           |
      | user  | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | user  | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
      | user  | today + 3          | dinner   | 4      | Gazpacho                               |
      | user  | today + 4          | dinner   | 4      | Caesar salad                           |
      | user  | today + 5          | dinner   | 4      | Watercress soup                        |
      | user  | today + 6          | lunch    | 4      | Lasagna                                |
      | user  | today + 6          | dinner   | 4      | Norwegian salad                        |
    And the menus list does not contain the following menus
      | owner | date               | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday          | lunch    | 4      | Moussaka                               |
      | user  | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today + 7          | lunch    | 4      | Chili con carne                        |
      | user  | today + 7          | dinner   | 4      | Stuffed tomatoes                       |
