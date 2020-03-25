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

import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientAdditionSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue;

/**
 * The recipe's ingredients addition steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeIngredientAdditionStepDefinitions {

    @Steps
    private RecipeIngredientAdditionSteps recipeIngredientAddition;

    @When("^he adds the ingredient to the recipe specifying quantity as (\\d+) \"(.*)\"$")
    public void addIngredientToRecipe(int quantity, String measurementUnit) {
        var recipeIngredient = recipeIngredientAddition.addIngredientToRecipe(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe(), quantity, measurementUnit);
        recipeIngredientAddition.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he adds the ingredient as main ingredient to the recipe specifying quantity as (\\d+) \"(.*)\"$")
    public void addMainIngredientToRecipe(int quantity, String measurementUnit) {
        var recipeIngredient = recipeIngredientAddition.addMainIngredientToRecipe(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe(), quantity, measurementUnit);
        recipeIngredientAddition.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he adds the ingredient to the recipe without specifying whether ingredient is a main ingredient$")
    public void addIngredientToRecipeWithoutMain() {
        var recipeIngredient = recipeIngredientAddition.addIngredientToRecipeWithoutMain(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe());
        recipeIngredientAddition.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he adds the ingredient to the recipe without specifying any quantity$")
    public void addIngredientToRecipeWithoutQuantity() {
        var recipeIngredient = recipeIngredientAddition.addIngredientToRecipeWithoutQuantity(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe());
        recipeIngredientAddition.storeRecipeIngredient(recipeIngredient);
    }

    @When("^he add an ingredient without identity to the recipe$")
    public void addIngredientWithoutIdToRecipe() {
        recipeIngredientAddition.addIngredientWithoutIdToRecipe(recipeIngredientAddition.storedRecipe());
    }

    @Then("^the ingredient is added to the recipe$")
    public void recipeIngredientCreated() {
        var recipeIngredient = recipeIngredientAddition.assertIngredientSuccessfullyAddedToRecipe(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe());
        recipeIngredientAddition.storeRecipeIngredient(recipeIngredient);
    }

    @Then("^an error notifies that recipe ingredient must have an identity$")
    public void errorOnRecipeIngredientCreationWithoutId() {
        recipeIngredientAddition.assertInvalidRequestConcerningMissingBodyField(RecipeIngredientValue.ID_FIELD);
    }

    @Then("^an error notifies that measurement unit does not correspond to ingredient measurement type$")
    public void errorOnNonCorrespondingMeasurementUnit() {
        recipeIngredientAddition.assertNonCorrespondingMeasurementUnit(
                recipeIngredientAddition.storedIngredient(),
                recipeIngredientAddition.storedRecipe(),
                recipeIngredientAddition.storedRecipeIngredient());
    }

}
