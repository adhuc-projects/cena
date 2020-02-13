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
package org.adhuc.cena.menu.steps.serenity.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipe detail rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeDetailSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final RecipeListClientDelegate listClient = new RecipeListClientDelegate(recipesResourceUrl());
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Step("Get recipe from {0}")
    public RecipeValue getRecipeFromUrl(String recipeDetailUrl) {
        fetchRecipe(recipeDetailUrl);
        return assertOk().extract().as(RecipeValue.class);
    }

    @Step("Retrieve recipe with name {0}")
    public RecipeValue retrieveRecipe(String recipeName) {
        var recipe = getFromRecipesList(new RecipeValue(recipeName));
        return recipe.orElseGet(() -> fail("Unable to retrieve recipe with name " + recipeName));
    }

    @Step("Attempt retrieving recipe with name {0}")
    public void attemptRetrievingRecipe(String recipeName) {
        var original = RecipeValue.buildUnknownRecipeValue(recipeName, recipesResourceUrl());
        var recipe = getFromRecipesList(original);
        assertThat(recipe).isNotPresent();
        fetchRecipe(original.selfLink());
    }

    @Step("Assert recipe {0} is accessible")
    public void assertRecipeInfoIsAccessible(RecipeValue expected) {
        var actual = getRecipeFromUrl(expected.selfLink());
        actual.assertEqualTo(expected);
    }

    private void fetchRecipe(String recipeDetailUrl) {
        rest().accept(HAL_JSON_VALUE).get(recipeDetailUrl).andReturn();
    }

}
