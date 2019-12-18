/*
 * Copyright (C) 2019 Alexandre Carbenay
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

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientListServiceClientSteps;

/**
 * The recipe ingredients list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeIngredientListStepDefinitions {

    private static final String NAME_ATTRIBUTE = "name";

    @Steps
    private RecipeIngredientListServiceClientSteps recipeIngredientListServiceClient;

    @Given("^the recipe contains no ingredient$")
    public void recipeContainsNoIngredient() {
        recipeIngredientListServiceClient.assumeRecipeContainsNoIngredient(
                recipeIngredientListServiceClient.storedRecipe());
    }

    @Given("^the following ingredients in the recipe$")
    public void recipeContainsIngredients(DataTable dataTable) {
        var ingredients = dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new IngredientValue(attributes.get(NAME_ATTRIBUTE))).collect(toList());
        var assumedIngredients = recipeIngredientListServiceClient.assumeIngredientsRelatedToRecipe(ingredients,
                recipeIngredientListServiceClient.storedRecipe());
        recipeIngredientListServiceClient.storeAssumedRecipeIngredients(assumedIngredients);
    }

    @Given("^recipe contains ingredient$")
    public void recipeContainsIngredient() {
        recipeIngredientListServiceClient.assumeIngredientRelatedToRecipe(
                recipeIngredientListServiceClient.storedIngredient(),
                recipeIngredientListServiceClient.storedRecipe());
    }

    @Given("^recipe does not contain ingredient$")
    public void recipeDoesNotContainIngredient() {
        recipeIngredientListServiceClient.assumeIngredientNotRelatedToRecipe(
                recipeIngredientListServiceClient.storedIngredient(),
                recipeIngredientListServiceClient.storedRecipe());
    }

    @When("^he lists the recipe's ingredients$")
    public void listRecipeIngredients() {
        var ingredients = recipeIngredientListServiceClient.getRecipeIngredients(
                recipeIngredientListServiceClient.storedRecipe());
        recipeIngredientListServiceClient.storeRecipeIngredients(ingredients);
    }

    @Then("^no ingredient is related to the recipe$")
    public void noIngredientRelatedToRecipe() {
        recipeIngredientListServiceClient.assertEmptyRecipeIngredientList(
                recipeIngredientListServiceClient.storedRecipe());
    }

    @Then("^the recipe's ingredients list is empty$")
    public void emptyRecipeIngredientList() {
        recipeIngredientListServiceClient.assertEmptyRecipeIngredientList(
                recipeIngredientListServiceClient.storedRecipeIngredients());
    }

    @Then("^the recipe's ingredients list contains the ingredients$")
    public void existingRecipeIngredientsFoundInList() {
        recipeIngredientListServiceClient.assertInRecipeIngredientsList(
                recipeIngredientListServiceClient.storedAssumedRecipeIngredients(),
                recipeIngredientListServiceClient.storedRecipeIngredients());
    }

    @Then("^the ingredient can be found in the recipe's ingredients list$")
    public void ingredientInRecipeIngredientsList() {
        recipeIngredientListServiceClient.assertIngredientRelatedToRecipe(
                recipeIngredientListServiceClient.storedIngredient(),
                recipeIngredientListServiceClient.storedRecipe());
    }

    @Then("^the ingredient cannot be found in the recipe's ingredients list$")
    public void ingredientNotInRecipeIngredientsList() {
        recipeIngredientListServiceClient.assertIngredientNotRelatedToRecipe(
                recipeIngredientListServiceClient.storedIngredient(),
                recipeIngredientListServiceClient.storedRecipe());
    }

}
