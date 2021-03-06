/*
 * Copyright (C) 2019-2020 Alexandre Carbenay
 *
 * This file is part of Cena Project.
 *
 * Cena Project is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Cena Project is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Cena Project. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.adhuc.cena.menu.steps.recipes.ingredients;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientListAssumptionsSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientListSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue;

/**
 * The recipe ingredients list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeIngredientListStepDefinitions {

    private static final String NAME_ATTRIBUTE = "name";
    private static final String RECIPE_ATTRIBUTE = "recipe";
    private static final String INGREDIENT_ATTRIBUTE = "ingredient";
    private static final String MAIN_INGREDIENT_ATTRIBUTE = "isMainIngredient";
    private static final String QUANTITY_ATTRIBUTE = "quantity";
    private static final String MEASUREMENT_UNIT_ATTRIBUTE = "measurementUnit";

    @Steps
    private RecipeListSteps recipeList;
    @Steps
    private RecipeIngredientListSteps recipeIngredientList;
    @Steps
    private RecipeIngredientListAssumptionsSteps recipeIngredientListAssumptions;
    @Steps
    private RecipeIngredientListAssertionsSteps recipeIngredientListAssertions;

    @Given("the recipe contains no ingredient")
    public void recipeContainsNoIngredient() {
        recipeIngredientListAssumptions.assumeRecipeContainsNoIngredient(recipeIngredientList.storedRecipe());
    }

    @Given("the following ingredients in the recipe")
    public void recipeContainsIngredients(DataTable dataTable) {
        var ingredients = dataTable.asMaps().stream()
                .map(attributes -> new RecipeIngredientValue(
                        new IngredientValue(attributes.get(NAME_ATTRIBUTE)),
                        Boolean.parseBoolean(attributes.get(MAIN_INGREDIENT_ATTRIBUTE)),
                        Integer.parseInt(attributes.get(QUANTITY_ATTRIBUTE)),
                        attributes.get(MEASUREMENT_UNIT_ATTRIBUTE))
                ).collect(toList());
        var assumedIngredients = recipeIngredientListAssumptions.assumeIngredientsRelatedToRecipe(ingredients,
                recipeIngredientList.storedRecipe());
        recipeIngredientList.storeAssumedRecipeIngredients(assumedIngredients);
    }

    @Given("the following ingredients in recipes")
    public void recipesContainIngredients(DataTable dataTable) {
        var recipesIngredients = new HashMap<String, List<RecipeIngredientValue>>();
        dataTable.asMaps()
                .forEach(attributes -> {
                    var recipeName = attributes.get(RECIPE_ATTRIBUTE);
                    var ingredient = new RecipeIngredientValue(
                            new IngredientValue(attributes.get(INGREDIENT_ATTRIBUTE)),
                            Boolean.parseBoolean(attributes.get(MAIN_INGREDIENT_ATTRIBUTE)),
                            Integer.parseInt(attributes.get(QUANTITY_ATTRIBUTE)),
                            attributes.get(MEASUREMENT_UNIT_ATTRIBUTE)
                    );
                    recipesIngredients.putIfAbsent(recipeName, new ArrayList<>());
                    recipesIngredients.get(recipeName).add(ingredient);
                });
        recipesIngredients.forEach((key, value) -> {
            var recipe = recipeList.getCorrespondingRecipe(new RecipeValue(key))
                    .orElseThrow(() -> new AssertionError(String.format("Recipe %s does not exist", key)));
            recipeIngredientListAssumptions.assumeIngredientsRelatedToRecipe(value, recipe);
        });
    }

    @Given("recipe contains ingredient")
    public void recipeContainsIngredient() {
        var recipeIngredient = recipeIngredientListAssumptions.assumeIngredientRelatedToRecipe(
                recipeIngredientList.storedIngredient(),
                recipeIngredientList.storedRecipe());
        recipeIngredientList.storeRecipeIngredient(recipeIngredient);
    }

    @Given("recipe does not contain ingredient")
    public void recipeDoesNotContainIngredient() {
        recipeIngredientListAssumptions.assumeIngredientNotRelatedToRecipe(
                recipeIngredientList.storedIngredient(),
                recipeIngredientList.storedRecipe());
    }

    @When("he lists the recipe's ingredients")
    public void listRecipeIngredients() {
        var ingredients = recipeIngredientList.getRecipeIngredients(recipeIngredientList.storedRecipe());
        recipeIngredientList.storeRecipeIngredients(ingredients);
    }

    @Then("no ingredient is related to the recipe")
    public void noIngredientRelatedToRecipe() {
        recipeIngredientListAssertions.assertEmptyRecipeIngredientList(recipeIngredientList.storedRecipe());
    }

    @Then("the recipe's ingredients list is empty")
    public void emptyRecipeIngredientList() {
        recipeIngredientListAssertions.assertEmptyRecipeIngredientList(recipeIngredientList.storedRecipeIngredients());
    }

    @Then("the recipe's ingredients list contains the ingredients")
    public void existingRecipeIngredientsFoundInList() {
        recipeIngredientListAssertions.assertInRecipeIngredientsList(
                recipeIngredientList.storedAssumedRecipeIngredients(),
                recipeIngredientList.storedRecipeIngredients());
    }

    @Then("the ingredient can be found in the recipe's ingredients list")
    public void ingredientInRecipeIngredientsList() {
        recipeIngredientListAssertions.assertIngredientRelatedToRecipe(
                recipeIngredientList.storedRecipeIngredient(),
                recipeIngredientList.storedRecipe());
    }

    @Then("the ingredient cannot be found in the recipe's ingredients list")
    public void ingredientNotInRecipeIngredientsList() {
        recipeIngredientListAssertions.assertIngredientNotRelatedToRecipe(
                recipeIngredientList.storedRecipeIngredient(),
                recipeIngredientList.storedRecipe());
    }

}
