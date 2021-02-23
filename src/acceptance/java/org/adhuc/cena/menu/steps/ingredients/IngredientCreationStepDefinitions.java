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

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientCreationSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;

import java.util.List;

/**
 * The ingredient creation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public class IngredientCreationStepDefinitions {

    @Steps
    private IngredientCreationSteps ingredientCreation;

    @When("he creates the ingredient")
    public void createIngredient() {
        ingredientCreation.createIngredient(ingredientCreation.storedIngredient());
    }

    @When("he creates the ingredient with {string} name")
    public void createIngredientWithName(String ingredientName) {
        ingredientCreation.createIngredient(new IngredientValue(ingredientName));
    }

    @When("he creates the ingredient with the following measurement types")
    public void createIngredientWithMeasurementTypes(DataTable dataTable) {
        var measurementTypes = dataTable.asList();
        var ingredient = ingredientCreation.storeIngredient(ingredientCreation.storedIngredient().withMeasurementTypes(measurementTypes));
        ingredientCreation.createIngredient(ingredient);
    }

    @When("he creates an ingredient without name")
    public void createIngredientWithoutName() {
        ingredientCreation.createIngredientWithoutName();
    }

    @When("he creates the ingredient without measurement type")
    public void createIngredientWithoutMeasurementType() {
        var ingredient = ingredientCreation.storeIngredient(ingredientCreation.storedIngredient().withoutMeasurementType());
        ingredientCreation.createIngredient(ingredient);
    }

    @Then("the ingredient is created")
    public void ingredientCreated() {
        ingredientCreation.assertIngredientSuccessfullyCreated(ingredientCreation.storedIngredient());
    }

    @Then("an error notifies that ingredient must have a name")
    public void errorOnIngredientCreationWithoutName() {
        ingredientCreation.assertInvalidRequestConcerningMissingBodyField(IngredientValue.NAME_FIELD);
    }

    @Then("an error notifies that ingredient name already exists")
    public void errorOnIngredientCreationDuplicatedName() {
        ingredientCreation.assertIngredientNameAlreadyExists(ingredientCreation.storedIngredient().name());
    }

    @Then("an error notifies that ingredient cannot be created with unknown {string} measurement type")
    public void errorOnIngredientCreationWithUnknownMeasurementType(String unknownMeasurementType) {
        int position = ingredientCreation.storedIngredient().measurementTypes().indexOf(unknownMeasurementType);
        ingredientCreation.assertInvalidRequestConcerningUnknownMeasurementUnit(position, unknownMeasurementType);
    }

}
