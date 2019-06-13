Feature: List ingredients
  As a community user, I want to list ingredients in the system so that I can use them in recipes

  Scenario: Empty ingredients list
    Given a community user
    And no existing ingredient
    When he lists the ingredients
    Then the ingredients list is empty
