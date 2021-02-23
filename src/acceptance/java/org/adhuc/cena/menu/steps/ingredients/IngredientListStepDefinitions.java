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

import static java.util.stream.Collectors.toList;

import java.util.List;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListAssumptionsSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;

/**
 * The ingredients list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public class IngredientListStepDefinitions {

    private static final String NAME_ATTRIBUTE = "name";
    private static final String MEASUREMENT_TYPE_ATTRIBUTE = "measurementTypes";

    @Steps
    private IngredientListSteps ingredientList;
    @Steps
    private IngredientListAssumptionsSteps ingredientListAssumptions;
    @Steps
    private IngredientListAssertionsSteps ingredientListAssertions;

    @Given("no existing ingredient")
    public void noExistingIngredient() {
        ingredientListAssumptions.assumeEmptyIngredientsList();
    }

    @Given("the following existing ingredients")
    public void existingIngredients(DataTable dataTable) {
        var ingredients = dataTable.asMaps().stream()
                .map(attributes -> new IngredientValue(
                        attributes.get(NAME_ATTRIBUTE),
                        List.of(attributes.get(MEASUREMENT_TYPE_ATTRIBUTE).split(", "))
                )).collect(toList());
        ingredientList.storeAssumedIngredients(ingredientListAssumptions.assumeInIngredientsList(ingredients));
    }

    @Given("an existing {string} ingredient")
    public void existingIngredient(String ingredientName) {
        ingredientList.storeIngredient(ingredientListAssumptions.assumeInIngredientsList(new IngredientValue(ingredientName)));
    }

    @Given("an existing {string} ingredient with measurement types {string}")
    public void existingIngredient(String ingredientName, String measurementTypes) {
        var ingredient = new IngredientValue(ingredientName, List.of(measurementTypes.split(", ")));
        ingredientList.storeIngredient(ingredientListAssumptions.assumeInIngredientsList(ingredient));
    }

    @Given("a non-existent {string} ingredient")
    public void nonExistentIngredient(String ingredientName) {
        ingredientList.storeIngredient(ingredientListAssumptions.assumeNotInIngredientsList(
                IngredientValue.buildUnknownIngredientValue(ingredientName, ingredientList.ingredientsResourceUrl())));
    }

    @When("he lists the ingredients")
    public void listIngredients() {
        var ingredients = ingredientList.getIngredients();
        ingredientList.storeIngredients(ingredients);
    }

    @Then("the ingredients list is empty")
    public void emptyIngredientList() {
        ingredientListAssertions.assertEmptyIngredientsList(ingredientList.storedIngredients());
    }

    @Then("the ingredients list contains the existing ingredients")
    public void existingIngredientsFoundInList() {
        ingredientListAssertions.assertInIngredientsList(ingredientList.storedAssumedIngredients(), ingredientList.storedIngredients());
    }

    @Then("the ingredient can be found in the list")
    public void ingredientFoundInList() {
        ingredientListAssertions.assertInIngredientsList(ingredientList.storedIngredient());
    }

    @Then("the ingredient cannot be found in the list")
    public void ingredientNotFoundInList() {
        ingredientListAssertions.assertNotInIngredientsList(ingredientList.storedIngredient());
    }

}
