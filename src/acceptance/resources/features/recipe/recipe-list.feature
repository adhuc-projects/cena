@Functional @Recipes
Feature: List recipes
  As a community user, I want to list recipes in the system so that I can select a recipe I would like to follow

  Scenario: Empty recipes list
    Given a community user
    And no existing recipe
    When he lists the recipes
    Then the recipes list is empty
