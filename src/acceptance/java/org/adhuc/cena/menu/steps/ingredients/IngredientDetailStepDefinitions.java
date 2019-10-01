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

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientDetailServiceClientSteps;

/**
 * The ingredient details steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@StepDefAnnotation
public class IngredientDetailStepDefinitions {

    @Steps
    private IngredientDetailServiceClientSteps ingredientDetailServiceClient;

    @When("^he retrieves \"(.*)\" ingredient$")
    public void retrieveIngredient(String ingredientName) {
        ingredientDetailServiceClient.storeIngredient(ingredientDetailServiceClient.retrieveIngredient(ingredientName));
    }

    @When("^he attempts retrieving \"(.*)\" ingredient$")
    public void tryToRetrieveIngredient(String ingredientName) {
        ingredientDetailServiceClient.attemptRetrievingIngredient(ingredientName);
    }

    @Then("^the ingredient details is accessible$")
    public void accessibleIngredientDetails() {
        ingredientDetailServiceClient.assertIngredientInfoIsAccessible(ingredientDetailServiceClient.storedIngredient());
    }

    @Then("^an error notifies that ingredient has not been found$")
    public void errorNotFoundIngredient() {
        ingredientDetailServiceClient.assertNotFound();
    }

}
