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
package org.adhuc.cena.menu.steps.ingredients;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientDeletionSteps;

/**
 * The ingredient deletion steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public class IngredientDeletionStepDefinitions {

    @Steps
    private IngredientDeletionSteps ingredientDeletion;

    @When("he attempts deleting the ingredient")
    public void tryToDeleteIngredient() {
        ingredientDeletion.attemptDeletingIngredient(ingredientDeletion.storedIngredient());
    }

    @When("he deletes the ingredient")
    public void deleteIngredient() {
        ingredientDeletion.deleteIngredient(ingredientDeletion.storedIngredient());
    }

    @Then("the ingredient is deleted")
    public void ingredientDeleted() {
        ingredientDeletion.assertIngredientSuccessfullyDeleted(ingredientDeletion.storedIngredient());
    }

    @Then("an error notifies that ingredient used in a recipe cannot be deleted")
    public void errorOnIngredientDeletionUsedInRecipe() {
        ingredientDeletion.assertIngredientNotDeletableUsedInRecipe(ingredientDeletion.storedIngredient());
    }

}
