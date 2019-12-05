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
package org.adhuc.cena.menu.steps.recipes.ingredients;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientListServiceClientSteps;

/**
 * The recipe ingredients list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@StepDefAnnotation
public class RecipeIngredientListStepDefinitions {

    @Steps
    private RecipeIngredientListServiceClientSteps recipeIngredientListServiceClient;

    @Given("^no relation between ingredient and recipe$")
    public void noRelationToIngredient() {
        recipeIngredientListServiceClient.assumeIngredientNotRelatedToRecipe(
                recipeIngredientListServiceClient.storedIngredient(),
                recipeIngredientListServiceClient.storedRecipe());
    }

}
