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

import static net.serenitybdd.rest.SerenityRest.rest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.Collection;
import java.util.List;

import lombok.experimental.Delegate;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipes list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeListServiceClientSteps {

    static final String RECIPES_SESSION_KEY = "recipes";

    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @Step("Assume empty recipes list")
    public void assumeEmptyRecipesList() {
        // TODO delete existing recipes
        assumeThat(fetchRecipes()).isEmpty();
    }

    @Step("Assert empty recipes list {0}")
    public void assertEmptyRecipesList(Collection<RecipeValue> recipes) {
        assertThat(recipes).isEmpty();
    }

    @Step("Get recipes list")
    public Collection<RecipeValue> getRecipes() {
        return fetchRecipes();
    }

    public Collection<RecipeValue> storeRecipes(Collection<RecipeValue> recipes) {
        Serenity.setSessionVariable(RECIPES_SESSION_KEY).to(recipes);
        return recipes;
    }

    public Collection<RecipeValue> storedRecipes() {
        assertThat(Serenity.hasASessionVariableCalled(RECIPES_SESSION_KEY))
                .as("Recipes in session (%s) must have been set previously", RECIPES_SESSION_KEY).isTrue();
        return Serenity.sessionVariableCalled(RECIPES_SESSION_KEY);
    }

    private List<RecipeValue> fetchRecipes() {
        var response = rest().get(recipesResourceUrl()).then();
        return assertOk(response).extract().jsonPath().getList("_embedded.data", RecipeValue.class);
    }

}
