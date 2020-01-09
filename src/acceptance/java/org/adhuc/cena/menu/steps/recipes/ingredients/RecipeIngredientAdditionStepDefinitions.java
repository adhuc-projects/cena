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

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientAdditionServiceClientSteps;

/**
 * The recipe's ingredients addition steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeIngredientAdditionStepDefinitions {

    @Steps
    private RecipeIngredientAdditionServiceClientSteps recipeIngredientAdditionServiceClient;

    @When("^he adds the ingredient to the recipe specifying quantity as (\\d+) \"(.*)\"$")
    public void addIngredientToRecipe(int quantity, String measurementUnit) {
        var recipeIngredient = recipeIngredientAdditionServiceClient.addIngredientToRecipe(
                recipeIngredientAdditionServiceClient.storedIngredient(),
                recipeIngredientAdditionServiceClient.storedRecipe(), quantity, measurementUnit);
        recipeIngredientAdditionServiceClient.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he adds the ingredient to the recipe without specifying any quantity$")
    public void addIngredientToRecipeWithoutQuantity() {
        var recipeIngredient = recipeIngredientAdditionServiceClient.addIngredientToRecipeWithoutQuantity(
                recipeIngredientAdditionServiceClient.storedIngredient(),
                recipeIngredientAdditionServiceClient.storedRecipe());
        recipeIngredientAdditionServiceClient.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he add an ingredient without identity to the recipe$")
    public void addIngredientWithoutIdToRecipe() {
        recipeIngredientAdditionServiceClient.addIngredientWithoutIdToRecipe(
                recipeIngredientAdditionServiceClient.storedRecipe());
    }

    @Then("^the ingredient is added to the recipe$")
    public void recipeIngredientCreated() {
        recipeIngredientAdditionServiceClient.assertIngredientSuccessfullyAddedToRecipe(
                recipeIngredientAdditionServiceClient.storedIngredient(),
                recipeIngredientAdditionServiceClient.storedRecipe());
    }

    @Then("^an error notifies that recipe ingredient must have an identity$")
    public void errorOnRecipeIngredientCreationWithoutId() {
        recipeIngredientAdditionServiceClient.assertInvalidRequest();
        // TODO assert response indicates field in error
    }

}
