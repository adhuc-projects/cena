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
package org.adhuc.cena.menu.steps.serenity.recipes.ingredients;

import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.Optional;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipe's ingredients addition rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeIngredientListServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Step("Assume ingredient {0} is not in recipe {1} ingredients list")
    public void assumeIngredientNotRelatedToRecipe(IngredientValue ingredient, RecipeValue recipe) {
        var response = rest().get(recipe.getIngredients()).then();
        var recipeIngredient = Optional.ofNullable(statusAssertionDelegate.assertOk(response)
                .extract().jsonPath().param("id", ingredient.id())
                .getObject("_embedded.data.find { ingredient->ingredient.id == id }", RecipeIngredientValue.class));
        assumeThat(recipeIngredient).isEmpty();
    }

}
