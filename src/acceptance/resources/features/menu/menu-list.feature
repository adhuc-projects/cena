@Functional @Menus
Feature: List menus for a period of time
  As an authenticated user, I want to list menus so that I can retrieve the menus I planned for a period of time

  Background:
    Given the following existing recipes
      | name                                   | content                                                                                                                                  | servings | courseTypes          |
      | Tomato, cucumber and mozzarella salad  | Cut everything into dices, mix it, dress it                                                                                              | 2        | STARTER, MAIN_COURSE |
      | Tomato, cucumber, olive and feta salad | Stone olives, cut everything into dices, mix it, dress it                                                                                | 6        | STARTER, MAIN_COURSE |
      | Tomato and cantal pie                  | Spread the shortcrust in a pie plate, wrap it with mustard, tomato slices and cantal slices in this order, and bake it during 20 minutes | 4        | MAIN_COURSE          |
      | Quiche lorraine                        | Spread the shortcrust in a pie plate, beat the eggs with milk, add lardons, pour on the pastry and bake it during 30 minutes             | 4        | MAIN_COURSE          |
      | Gazpacho                               | Gazpacho made easy                                                                                                                       | 4        | STARTER, MAIN_COURSE |
      | Chili con carne                        | Chili con carne made easy                                                                                                                | 8        | MAIN_COURSE          |
      | Moussaka                               | Moussaka made easy                                                                                                                       | 4        | MAIN_COURSE          |
      | Lasagna                                | Lasagna made easy                                                                                                                        | 8        | MAIN_COURSE          |
      | Pizza Regina                           | Pizza Regina made easy                                                                                                                   | 2        | MAIN_COURSE          |
      | Watercress soup                        | Watercress soup made easy                                                                                                                | 4        | STARTER, MAIN_COURSE |
      | Stuffed tomatoes                       | Stuffed tomatoes made easy                                                                                                               | 4        | MAIN_COURSE          |
      | Vegetarian fajitas                     | Vegetarian fajitas made easy                                                                                                             | 2        | MAIN_COURSE          |
      | Norwegian salad                        | Norwegian salad made easy                                                                                                                | 2        | MAIN_COURSE          |
      | Caesar salad                           | Caesar salad made easy                                                                                                                   | 4        | MAIN_COURSE          |

  @Edge
  Scenario: Empty menus list
    Given an authenticated user
    And no existing menu
    When he lists the menus
    Then the menus list is empty

  @Edge
  Scenario: Empty menus list when menus are not in specified period of time
    Given an authenticated user
    And the following existing menus
      | owner | date      | mealType | covers | mainCourseRecipes                      |
      | user  | yesterday | lunch    | 4      | Moussaka                               |
      | user  | yesterday | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | user  | today     | lunch    | 4      | Pizza Regina                           |
      | user  | today     | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user  | today + 5 | dinner   | 4      | Watercress soup                        |
    And no existing menu between tomorrow and today + 4
    When he lists the menus between tomorrow and today + 4
    Then the menus list is empty

  @Smoke
  Scenario: List with menus for default period of time
    Given an authenticated user
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
      | date               | mealType | covers | mainCourseRecipes                      |
      | today              | lunch    | 4      | Pizza Regina                           |
      | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
      | today + 3          | dinner   | 4      | Gazpacho                               |
      | today + 4          | dinner   | 4      | Caesar salad                           |
      | today + 5          | dinner   | 4      | Watercress soup                        |
      | today + 6          | lunch    | 4      | Lasagna                                |
      | today + 6          | dinner   | 4      | Norwegian salad                        |
    And the menus list does not contain the following menus
      | date      | mealType | covers | mainCourseRecipes                     |
      | yesterday | lunch    | 4      | Moussaka                              |
      | yesterday | dinner   | 4      | Tomato, cucumber and mozzarella salad |
      | today + 7 | lunch    | 4      | Chili con carne                       |
      | today + 7 | dinner   | 4      | Stuffed tomatoes                      |

  @Smoke
  Scenario: List with menus for specified period of time
    Given an authenticated user
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
    When he lists the menus between yesterday and day after tomorrow
    Then the menus list contains the following menus
      | date               | mealType | covers | mainCourseRecipes                      |
      | yesterday          | lunch    | 4      | Moussaka                               |
      | yesterday          | dinner   | 4      | Tomato, cucumber and mozzarella salad  |
      | today              | lunch    | 4      | Pizza Regina                           |
      | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
    And the menus list does not contain the following menus
      | date      | mealType | covers | mainCourseRecipes |
      | today + 3 | dinner   | 4      | Gazpacho          |
      | today + 4 | dinner   | 4      | Caesar salad      |
      | today + 5 | dinner   | 4      | Watercress soup   |
      | today + 6 | lunch    | 4      | Lasagna           |
      | today + 6 | dinner   | 4      | Norwegian salad   |
      | today + 7 | lunch    | 4      | Chili con carne   |
      | today + 7 | dinner   | 4      | Stuffed tomatoes  |

  @Security
  Scenario: List with menus contains current user menus only
    Given an authenticated user
    And the following existing menus
      | owner              | date               | mealType | covers | mainCourseRecipes                      |
      | user               | today              | lunch    | 4      | Pizza Regina                           |
      | user               | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user               | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | user               | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
      | ingredient manager | today              | lunch    | 4      | Chili con carne                        |
      | ingredient manager | today              | dinner   | 4      | Watercress soup                        |
      | ingredient manager | tomorrow           | dinner   | 4      | Stuffed tomatoes                       |
      | ingredient manager | day after tomorrow | dinner   | 4      | Pizza Regina                           |
    When he lists the menus between today and day after tomorrow
    Then the menus list contains the following menus
      | date               | mealType | covers | mainCourseRecipes                      |
      | today              | lunch    | 4      | Pizza Regina                           |
      | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
    And the menus list does not contain the following menus
      | date               | mealType | covers | mainCourseRecipes |
      | today              | lunch    | 4      | Chili con carne   |
      | today              | dinner   | 4      | Watercress soup   |
      | tomorrow           | dinner   | 4      | Stuffed tomatoes  |
      | day after tomorrow | dinner   | 4      | Pizza Regina      |

  @Security
  Scenario: List with menus as community user
    Given a community user
    When he attempts retrieving list of menus
    Then an error notifies that user is not authenticated

  @Security
  Scenario: List with menus as super administrator
    Given an authenticated super administrator
    And the following existing menus
      | owner               | date               | mealType | covers | mainCourseRecipes                      |
      | user                | today              | lunch    | 4      | Pizza Regina                           |
      | user                | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | user                | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | user                | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
      | super administrator | today              | lunch    | 4      | Chili con carne                        |
      | super administrator | today              | dinner   | 4      | Watercress soup                        |
      | super administrator | tomorrow           | dinner   | 4      | Stuffed tomatoes                       |
      | super administrator | day after tomorrow | dinner   | 4      | Pizza Regina                           |
    When he lists the menus between today and day after tomorrow
    Then the menus list contains the following menus
      | date               | mealType | covers | mainCourseRecipes |
      | today              | lunch    | 4      | Chili con carne   |
      | today              | dinner   | 4      | Watercress soup   |
      | tomorrow           | dinner   | 4      | Stuffed tomatoes  |
      | day after tomorrow | dinner   | 4      | Pizza Regina      |
    And the menus list does not contain the following menus
      | date               | mealType | covers | mainCourseRecipes                      |
      | today              | lunch    | 4      | Pizza Regina                           |
      | today              | dinner   | 4      | Tomato, cucumber, olive and feta salad |
      | tomorrow           | dinner   | 4      | Tomato and cantal pie                  |
      | day after tomorrow | dinner   | 4      | Quiche lorraine                        |
