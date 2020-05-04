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

import static org.assertj.core.api.Assertions.assertThat;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.SUPER_ADMINISTRATOR;

import java.util.UUID;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipe deletion rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeDeletionSteps {

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

    @Step("Delete recipes")
    public void deleteRecipes() {
        rest().delete(recipesResourceUrl()).then();
    }

    @Step("Delete recipes as super administrator")
    public void deleteRecipesAsSuperAdministrator() {
        var response = rest(SUPER_ADMINISTRATOR).delete(recipesResourceUrl()).then();
        assertNoContent(response);
    }

    @Step("Delete recipe {0}")
    public void deleteRecipe(RecipeValue recipe) {
        rest().delete(recipe.selfLink());
    }

    @Step("Delete recipe {0} as super administrator")
    public void deleteRecipeAsSuperAdministrator(RecipeValue recipe) {
        rest(SUPER_ADMINISTRATOR).delete(recipe.selfLink());
    }

    @Step("Attempt deleting recipe {0}")
    public void attemptDeletingRecipe(RecipeValue recipe) {
        var existingRecipe = getFirstFromRecipesList(new RecipeValue(recipe.name()));
        assertThat(existingRecipe).isNotPresent();
        rest().delete(generateNotFoundRecipeUrl());
    }

    @Step("Assert recipe {0} has been successfully deleted")
    public void assertRecipeSuccessfullyDeleted(RecipeValue recipe) {
        assertNoContent();
    }

    private String generateNotFoundRecipeUrl() {
        return recipesResourceUrl() + "/" + UUID.randomUUID().toString();
    }

}
