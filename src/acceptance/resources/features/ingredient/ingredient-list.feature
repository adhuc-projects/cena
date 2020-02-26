@Functional @Ingredients
Feature: List ingredients
  As a community user, I want to list ingredients in the system so that I can use them in recipes

  @Edge
  Scenario: Empty ingredients list
    Given a community user
    And no existing ingredient
    When he lists the ingredients
    Then the ingredients list is empty

  @Smoke
  Scenario: List with ingredients
    Given a community user
    And the following existing ingredients
      | name       | measurementTypes |
      | Tomato     | WEIGHT, COUNT    |
      | Cucumber   | WEIGHT, COUNT    |
      | Mozzarella | WEIGHT, COUNT    |
      | Olive oil  | VOLUME           |
      | Pepper     | AT_CONVENIENCE   |
    When he lists the ingredients
    Then the ingredients list contains the existing ingredients
