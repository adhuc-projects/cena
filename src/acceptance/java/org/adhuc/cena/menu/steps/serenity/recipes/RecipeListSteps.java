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
package org.adhuc.cena.menu.steps.serenity.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The recipes list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeListSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final RecipeListClientDelegate listClient = new RecipeListClientDelegate(
            resourceUrlResolverDelegate.recipesResourceUrl());
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Steps
    private IngredientListSteps ingredientList;

    @Step("Get recipes list")
    public Collection<RecipeValue> getRecipes() {
        return listClient.fetchRecipes();
    }

    @Step("Get recipes composed of ingredient {0}")
    public Collection<RecipeValue> getRecipesComposedOfIngredient(IngredientValue ingredient) {
        var actualIngredient = ingredientList.getCorrespondingIngredient(ingredient);
        if (actualIngredient.isPresent()) {
            return listClient.fetchRecipesComposedOfIngredient(actualIngredient.get());
        } else {
            listClient.attemptFetchingRecipesComposedOfUnknownIngredient(ingredient);
            return Collections.emptyList();
        }
    }

    @Step("Get recipe corresponding to recipe {0}")
    public Optional<RecipeValue> getCorrespondingRecipe(RecipeValue recipe) {
        return listClient.getFirstFromRecipesList(recipe);
    }

    public String recipesResourceUrl() {
        return resourceUrlResolverDelegate.recipesResourceUrl();
    }

}
