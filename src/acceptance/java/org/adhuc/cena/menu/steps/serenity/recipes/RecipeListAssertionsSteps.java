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

import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.NAME_AND_CONTENT_COMPARATOR;

import java.util.Collection;
import java.util.Comparator;

import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The recipes list rest-service client steps definition dedicated to assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeListAssertionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final RecipeListClientDelegate listClient = new RecipeListClientDelegate(
            resourceUrlResolverDelegate.recipesResourceUrl());

    @Step("Assert empty recipes list")
    public void assertEmptyRecipesList() {
        assertEmptyRecipesList(listClient.fetchRecipes());
    }

    @Step("Assert empty recipes list {0}")
    public void assertEmptyRecipesList(Collection<RecipeValue> recipes) {
        assertThat(recipes).isEmpty();
    }

    @Step("Assert recipe {0} is in recipes list")
    public void assertInRecipesList(RecipeValue recipe) {
        assertThat(listClient.getFromRecipesList(recipe)).isPresent().get().usingComparator(NAME_AND_CONTENT_COMPARATOR).isEqualTo(recipe);
    }

    @Step("Assert recipes {0} are in recipes list {1}")
    public void assertInRecipesList(Collection<RecipeValue> expected, Collection<RecipeValue> actual) {
        assertInRecipesList(expected, actual, NAME_AND_CONTENT_COMPARATOR);
    }

    @Step("Assert recipes {0} are in recipes list {1}")
    public void assertInRecipesList(Collection<RecipeValue> expected, Collection<RecipeValue> actual, Comparator<RecipeValue> comparator) {
        assertThat(actual).usingElementComparator(comparator).containsAll(expected);
    }

    @Step("Assert recipe {0} is not in recipes list")
    public void assertNotInRecipesList(RecipeValue recipe) {
        assertThat(listClient.getFromRecipesList(recipe)).isNotPresent();
    }

    @Step("Assert recipes {0} are not in recipes list {1}")
    public void assertNotInRecipesList(Collection<RecipeValue> expected, Collection<RecipeValue> actual) {
        assertThat(actual).usingElementComparator(COMPARATOR).doesNotContainAnyElementsOf(expected);
    }

}
