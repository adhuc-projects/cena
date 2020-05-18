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
package org.adhuc.cena.menu.steps.recipes;

import static java.util.stream.Collectors.toList;

import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.buildUnknownIngredientValue;
import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.buildUnknownRecipeValue;

import java.util.List;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListAssumptionsSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

/**
 * The recipes list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeListStepDefinitions {

    private static final String NAME_ATTRIBUTE = "name";
    private static final String CONTENT_ATTRIBUTE = "content";
    private static final String SERVINGS_ATTRIBUTE = "servings";
    private static final String COURSE_TYPE_ATTRIBUTE = "courseTypes";

    @Steps
    private RecipeListSteps recipeList;
    @Steps
    private IngredientListSteps ingredientList;
    @Steps
    private RecipeListAssumptionsSteps recipeListAssumptions;
    @Steps
    private RecipeListAssertionsSteps recipeListAssertions;

    @Given("^no existing recipe$")
    public void noExistingRecipe() {
        recipeListAssumptions.assumeEmptyRecipesList();
    }

    @Given("^the following existing recipes$")
    public void existingRecipes(DataTable dataTable) {
        var recipes = dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new RecipeValue(attributes.get(NAME_ATTRIBUTE), attributes.get(CONTENT_ATTRIBUTE),
                        Integer.parseInt(attributes.get(SERVINGS_ATTRIBUTE)),
                        List.of(attributes.get(COURSE_TYPE_ATTRIBUTE).split(", ")))).collect(toList());
        recipeList.storeAssumedRecipes(recipeListAssumptions.assumeInRecipesList(recipes));
    }

    @Given("^an existing \"(.*)\" recipe$")
    public void existingRecipe(String recipeName) {
        recipeList.storeRecipe(recipeListAssumptions.assumeInRecipesList(new RecipeValue(recipeName)));
    }

    @Given("^an existing \"(.*)\" recipe authored by the authenticated user$")
    public void existingRecipeAuthoredByCurrentUser(String recipeName) {
        recipeList.storeRecipe(recipeListAssumptions.assumeInRecipesListAuthoredByCurrentUser(new RecipeValue(recipeName)));
    }

    @Given("^an existing \"(.*)\" recipe authored by another user$")
    public void existingRecipeAuthoredByAnotherUser(String recipeName) {
        recipeList.storeRecipe(recipeListAssumptions.assumeInRecipesListAuthoredByAnotherUser(new RecipeValue(recipeName)));
    }

    @Given("^a non-existent \"(.*)\" recipe$")
    public void nonExistentRecipe(String recipeName) {
        recipeList.storeRecipe(recipeListAssumptions.assumeNotInRecipesList(
                buildUnknownRecipeValue(recipeName, recipeList.recipesResourceUrl())));
    }

    @When("^he lists the recipes$")
    public void listRecipes() {
        var recipes = recipeList.getRecipes();
        recipeList.storeRecipes(recipes);
    }

    @When("^he lists the recipes composed of ingredient \"(.*)\"$")
    public void listRecipesComposedOfIngredient(String ingredientName) {
        var recipes = recipeList.getRecipesComposedOfIngredient(new IngredientValue(ingredientName));
        recipeList.storeRecipes(recipes);
    }

    @When("^he lists the recipes composed of unknown ingredient$")
    public void listRecipesComposedOfUnknownIngredient() {
        recipeList.getRecipesComposedOfIngredient(
                buildUnknownIngredientValue("unknown", ingredientList.ingredientsResourceUrl()));
    }

    @Then("^the recipes list is empty$")
    public void emptyRecipeList() {
        recipeListAssertions.assertEmptyRecipesList(recipeList.storedRecipes());
    }

    @Then("^the recipes list contains the existing recipes$")
    public void existingRecipesFoundInList() {
        recipeListAssertions.assertInRecipesList(recipeList.storedAssumedRecipes(), recipeList.storedRecipes());
    }

    @Then("^the recipes list contains the following recipes$")
    public void followingRecipesFoundInList(DataTable dataTable) {
        var recipes = dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new RecipeValue(attributes.get(NAME_ATTRIBUTE)))
                .collect(toList());
        recipeListAssertions.assertInRecipesList(recipes, recipeList.storedRecipes(), COMPARATOR);
    }

    @Then("^the recipes list does not contain the following recipes$")
    public void followingRecipesNotFoundInList(DataTable dataTable) {
        var recipes = dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new RecipeValue(attributes.get(NAME_ATTRIBUTE)))
                .collect(toList());
        recipeListAssertions.assertNotInRecipesList(recipes, recipeList.storedRecipes());
    }

    @Then("^the recipe can be found in the list$")
    public void recipeFoundInList() {
        recipeListAssertions.assertInRecipesList(recipeList.storedRecipe());
    }

    @Then("^the recipe cannot be found in the list$")
    public void recipeNotFoundInList() {
        recipeListAssertions.assertNotInRecipesList(recipeList.storedRecipe());
    }

}
