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
import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.COMPARATOR;

import java.util.Collection;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The recipes list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeListServiceClientSteps {

    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final RecipeListClientDelegate listClient = new RecipeListClientDelegate(recipesResourceUrl());
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Step("Assume empty recipes list")
    public void assumeEmptyRecipesList() {
        // TODO delete existing recipes
        assumeThat(fetchRecipes()).isEmpty();
    }

    @Step("Assert empty recipes list {0}")
    public void assertEmptyRecipesList(Collection<RecipeValue> recipes) {
        assertThat(recipes).isEmpty();
    }

    @Step("Assume recipe {0} is not in recipes list")
    public RecipeValue assumeNotInRecipesList(RecipeValue recipe) {
        assumeThat(fetchRecipes()).usingElementComparator(COMPARATOR).doesNotContain(recipe);
        return recipe;
    }

    @Step("Get recipes list")
    public Collection<RecipeValue> getRecipes() {
        return fetchRecipes();
    }

}