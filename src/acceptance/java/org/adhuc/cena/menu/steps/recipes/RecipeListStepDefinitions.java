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
package org.adhuc.cena.menu.steps.recipes;

import static java.util.stream.Collectors.toList;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListServiceClientSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

/**
 * The recipes list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeListStepDefinitions {

    private static final String NAME_ATTRIBUTE = "name";
    private static final String CONTENT_ATTRIBUTE = "content";
    private static final String SERVINGS_ATTRIBUTE = "servings";

    @Steps
    private RecipeListServiceClientSteps recipeListServiceClient;

    @Given("^no existing recipe$")
    public void noExistingRecipe() {
        recipeListServiceClient.assumeEmptyRecipesList();
    }

    @Given("^the following existing recipes$")
    public void existingRecipes(DataTable dataTable) {
        var recipes = dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new RecipeValue(attributes.get(NAME_ATTRIBUTE), attributes.get(CONTENT_ATTRIBUTE),
                        Integer.parseInt(attributes.get(SERVINGS_ATTRIBUTE)))).collect(toList());
        recipeListServiceClient.storeAssumedRecipes(recipeListServiceClient.assumeInRecipesList(recipes));
    }

    @Given("^an existing \"(.*)\" recipe$")
    public void existingRecipe(String recipeName) {
        recipeListServiceClient.storeRecipe(recipeListServiceClient.assumeInRecipesList(new RecipeValue(recipeName)));
    }

    @Given("^an existing \"(.*)\" recipe authored by the authenticated user$")
    public void existingRecipeAuthoredByCurrentUser(String recipeName) {
        recipeListServiceClient.storeRecipe(recipeListServiceClient.assumeInRecipesListAuthoredByCurrentUser(new RecipeValue(recipeName)));
    }

    @Given("^an existing \"(.*)\" recipe authored by another user$")
    public void existingRecipeAuthoredByAnotherUser(String recipeName) {
        recipeListServiceClient.storeRecipe(recipeListServiceClient.assumeInRecipesListAuthoredByAnotherUser(new RecipeValue(recipeName)));
    }

    @Given("^a non-existent \"(.*)\" recipe$")
    public void nonExistentRecipe(String recipeName) {
        recipeListServiceClient.storeRecipe(recipeListServiceClient.assumeNotInRecipesList(
                RecipeValue.buildUnknownRecipeValue(recipeName, recipeListServiceClient.recipesResourceUrl())));
    }

    @When("^he lists the recipes$")
    public void listRecipes() {
        var recipes = recipeListServiceClient.getRecipes();
        recipeListServiceClient.storeRecipes(recipes);
    }

    @Then("^the recipes list is empty$")
    public void emptyRecipeList() {
        recipeListServiceClient.assertEmptyRecipesList(recipeListServiceClient.storedRecipes());
    }

    @Then("^the recipes list contains the existing recipes$")
    public void existingRecipesFoundInList() {
        recipeListServiceClient.assertInRecipesList(
                recipeListServiceClient.storedAssumedRecipes(),
                recipeListServiceClient.storedRecipes());
    }

    @Then("^the recipe can be found in the list$")
    public void recipeFoundInList() {
        recipeListServiceClient.assertInRecipesList(recipeListServiceClient.storedRecipe());
    }

    @Then("^the recipe cannot be found in the list$")
    public void recipeNotFoundInList() {
        recipeListServiceClient.assertNotInRecipesList(recipeListServiceClient.storedRecipe());
    }

}
