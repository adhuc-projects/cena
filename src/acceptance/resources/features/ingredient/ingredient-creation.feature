Feature: Create an ingredient in the system
  As an ingredient manager, I want to create an ingredient in the system so that recipes can be composed of ingredients

Scenario: Create an ingredient successfully
  Given an authenticated ingredient manager
    And a non-existent "Tomato" ingredient
  When he creates the ingredient
  Then the ingredient is created
    And the ingredient can be found in the list
