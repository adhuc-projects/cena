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

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientRemovalSteps;

/**
 * The recipe's ingredients removal steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeIngredientRemovalStepDefinitions {

    @Steps
    private RecipeIngredientRemovalSteps recipeIngredientRemoval;

    @When("he removes all the ingredients from the recipe")
    public void removeIngredientsFromRecipe() {
        recipeIngredientRemoval.removeIngredientsFromRecipe(recipeIngredientRemoval.storedRecipe());
    }

    @When("he removes the ingredient from the recipe")
    public void removeIngredientFromRecipe() {
        recipeIngredientRemoval.removeIngredientFromRecipe(
                recipeIngredientRemoval.storedIngredient(),
                recipeIngredientRemoval.storedRecipe());
    }

    @When("he attempts removing the ingredient from the recipe")
    public void attemptRemovingIngredientFromRecipe() {
        recipeIngredientRemoval.attemptRemoveIngredientFromRecipe(
                recipeIngredientRemoval.storedIngredient(),
                recipeIngredientRemoval.storedRecipe());
    }

    @Then("the ingredients are removed from the recipe")
    public void recipeIngredientsRemoved() {
        recipeIngredientRemoval.assertIngredientsSuccessfullyRemovedFromRecipe(recipeIngredientRemoval.storedRecipe());
    }

    @Then("the ingredient is removed from the recipe")
    public void recipeIngredientRemoved() {
        recipeIngredientRemoval.assertIngredientSuccessfullyRemovedFromRecipe(
                recipeIngredientRemoval.storedIngredient(),
                recipeIngredientRemoval.storedRecipe());
    }

    @Then("an error notifies that recipe does not contain ingredient")
    public void errorOnRecipeIngredientDeletionNotFound() {
        recipeIngredientRemoval.assertIngredientNotRemovableFromRecipe(
                recipeIngredientRemoval.storedIngredient(),
                recipeIngredientRemoval.storedRecipe());
    }

}
