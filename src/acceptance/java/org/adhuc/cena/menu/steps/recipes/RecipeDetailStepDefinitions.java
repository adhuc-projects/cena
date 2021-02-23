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

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeDetailSteps;

/**
 * The recipe details steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeDetailStepDefinitions {

    @Steps
    private RecipeDetailSteps recipeDetail;

    @When("he retrieves {string} recipe")
    public void retrieveRecipe(String recipeName) {
        recipeDetail.storeRecipe(recipeDetail.retrieveRecipe(recipeName));
    }

    @When("he attempts retrieving {string} recipe")
    public void tryToRetrieveRecipe(String recipeName) {
        recipeDetail.attemptRetrievingRecipe(recipeName);
    }

    @Then("the recipe details is accessible")
    public void accessibleRecipeDetails() {
        recipeDetail.assertRecipeInfoIsAccessible(recipeDetail.storedRecipe());
    }

    @Then("an error notifies that recipe has not been found")
    public void errorNotFoundRecipe() {
        recipeDetail.assertNotFound();
    }

}
