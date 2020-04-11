@Functional @Menus
Feature: Create a menu for a meal
  As an authenticated user, I want to create a menu for a meal so that I can plan my meals and shopping list

  @Smoke @Security
  Scenario: Create a menu successfully
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he creates a menu from the recipe for 2 covers for today's lunch
    Then the menu is created
    And the menu can be found in the menus list starting from today
