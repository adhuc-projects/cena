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

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientDetailSteps;

/**
 * The ingredient details steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public class IngredientDetailStepDefinitions {

    @Steps
    private IngredientDetailSteps ingredientDetail;

    @When("he retrieves {string} ingredient")
    public void retrieveIngredient(String ingredientName) {
        ingredientDetail.storeIngredient(ingredientDetail.retrieveIngredient(ingredientName));
    }

    @When("he attempts retrieving {string} ingredient")
    public void tryToRetrieveIngredient(String ingredientName) {
        ingredientDetail.attemptRetrievingIngredient(ingredientName);
    }

    @Then("the ingredient details is accessible")
    public void accessibleIngredientDetails() {
        ingredientDetail.assertIngredientInfoIsAccessible(ingredientDetail.storedIngredient());
    }

    @Then("an error notifies that ingredient has not been found")
    public void errorNotFoundIngredient() {
        ingredientDetail.assertNotFound();
    }

}
