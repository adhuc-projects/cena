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

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientCreationServiceClientSteps;

/**
 * The ingredient creation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@StepDefAnnotation
public class IngredientCreationStepDefinitions {

    @Steps
    private IngredientCreationServiceClientSteps ingredientCreationServiceClient;

    @When("^he creates the ingredient$")
    public void createIngredient() {
        ingredientCreationServiceClient.createIngredient(ingredientCreationServiceClient.storedIngredient());
    }

    @When("^he creates the ingredient with the following measurement types$")
    public void createIngredientWithMeasurementTypes(DataTable dataTable) {
        var measurementTypes = dataTable.asList(String.class);
        ingredientCreationServiceClient.createIngredient(
                ingredientCreationServiceClient.storedIngredient().withMeasurementTypes(measurementTypes));
    }

    @When("^he creates an ingredient without name$")
    public void createIngredientWithoutName() {
        ingredientCreationServiceClient.createIngredientWithoutName();
    }

    @When("^he creates the ingredient without measurement type$")
    public void createIngredientWithoutMeasurementType() {
        var ingredient = ingredientCreationServiceClient.storeIngredient(
                ingredientCreationServiceClient.storedIngredient().withoutMeasurementType());
        ingredientCreationServiceClient.createIngredient(ingredient);
    }

    @Then("^the ingredient is created$")
    public void ingredientCreated() {
        ingredientCreationServiceClient.assertIngredientSuccessfullyCreated(ingredientCreationServiceClient.storedIngredient());
    }

    @Then("^an error notifies that ingredient must have a name$")
    public void errorOnIngredientCreationWithoutName() {
        ingredientCreationServiceClient.assertInvalidRequest();
        // TODO assert response indicates field in error
    }

    @Then("^an error notifies that ingredient name already exists$")
    public void errorOnIngredientCreationDuplicatedName() {
        ingredientCreationServiceClient.assertIngredientNameAlreadyExists(ingredientCreationServiceClient.storedIngredient().name());
    }

    @Then("^an error notifies that ingredient cannot be created with unknown measurement type$")
    public void errorOnIngredientCreationWithUnknownMeasurementType() {
        ingredientCreationServiceClient.assertInvalidRequest();
        // TODO assert response indicates field in error
    }

}
