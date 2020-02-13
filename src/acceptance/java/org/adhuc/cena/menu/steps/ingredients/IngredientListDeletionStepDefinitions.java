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

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientDeletionSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListSteps;

/**
 * The ingredients list deletion steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@StepDefAnnotation
public class IngredientListDeletionStepDefinitions {

    @Steps
    private IngredientDeletionSteps ingredientDeletion;
    @Steps
    private IngredientListSteps ingredientList;
    @Steps
    private IngredientListAssertionsSteps ingredientListAssertions;

    @When("^he deletes the ingredients$")
    public void deleteIngredients() {
        ingredientDeletion.deleteIngredients();
    }

    @Then("^the ingredients have been deleted$")
    public void ingredientDeleted() {
        ingredientDeletion.assertNoContent();
    }

    @Then("^no ingredient is left in the ingredients list$")
    public void noIngredientsLeftInList() {
        ingredientListAssertions.assertEmptyIngredientsList();
    }

    @Then("^no existing ingredient has been deleted$")
    public void noExistingIngredientDeleted() {
        ingredientListAssertions.assertInIngredientsList(ingredientList.storedAssumedIngredients(), ingredientList.getIngredients());
    }

}
