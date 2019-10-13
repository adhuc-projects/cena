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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import static org.assertj.core.api.Assertions.assertThat;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.INGREDIENT_MANAGER;
import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.SUPER_ADMINISTRATOR;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The ingredient deletion rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientDeletionServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(ingredientsResourceUrl());
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();

    @Step("Delete ingredients")
    public void deleteIngredients() {
        rest().delete(ingredientsResourceUrl()).then();
    }

    @Step("Delete ingredients as super administrator")
    public void deleteIngredientsAsSuperAdministrator() {
        var response = rest(SUPER_ADMINISTRATOR).delete(ingredientsResourceUrl()).then();
        assertNoContent(response);
    }

    @Step("Delete ingredient {0}")
    public void deleteIngredient(IngredientValue ingredient) {
        ingredient.link("self").ifPresent(self -> rest().delete(self));
    }

    @Step("Delete ingredient {0} as ingredient manager")
    public void deleteIngredientAsIngredientManager(IngredientValue ingredient) {
        ingredient.link("self").ifPresent(self -> rest(INGREDIENT_MANAGER).delete(self));
    }

    @Step("Attempt deleting ingredient {0}")
    public void attemptDeletingIngredient(IngredientValue ingredient) {
        var existingIngredient = getFromIngredientsList(new IngredientValue(ingredient.name()));
        assertThat(existingIngredient).isNotPresent();
        rest().delete(generateNotFoundIngredientUrl());
    }

    @Step("Assert ingredient {0} has been successfully deleted")
    public void assertIngredientSuccessfullyDeleted(IngredientValue ingredient) {
        assertNoContent();
    }

    private String generateNotFoundIngredientUrl() {
        return ingredientsResourceUrl() + "/unknown";
    }

}
