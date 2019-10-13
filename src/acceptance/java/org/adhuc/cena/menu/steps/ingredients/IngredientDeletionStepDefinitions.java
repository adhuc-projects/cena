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
package org.adhuc.cena.menu.steps.ingredients;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientDeletionServiceClientSteps;

/**
 * The ingredient creation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@StepDefAnnotation
public class IngredientDeletionStepDefinitions {

    @Steps
    private IngredientDeletionServiceClientSteps ingredientDeletionServiceClient;

    @When("^he attempts deleting the ingredient$")
    public void tryToDeleteIngredient() {
        ingredientDeletionServiceClient.attemptDeletingIngredient(ingredientDeletionServiceClient.storedIngredient());
    }

    @When("^he deletes the ingredient$")
    public void deleteIngredient() {
        ingredientDeletionServiceClient.deleteIngredient(ingredientDeletionServiceClient.storedIngredient());
    }

    @Then("^the ingredient is deleted$")
    public void ingredientDeleted() {
        ingredientDeletionServiceClient.assertIngredientSuccessfullyDeleted(ingredientDeletionServiceClient.storedIngredient());
    }

}
