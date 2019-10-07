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
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The ingredient detail rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientDetailServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(ingredientsResourceUrl());
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate(ingredientsResourceUrl());

    @Step("Get ingredient from {0}")
    public IngredientValue getIngredientFromUrl(String ingredientDetailUrl) {
        fetchIngredient(ingredientDetailUrl);
        return assertOk().extract().as(IngredientValue.class);
    }

    @Step("Retrieve ingredient with name {0}")
    public IngredientValue retrieveIngredient(String ingredientName) {
        var ingredient = getFromIngredientsList(new IngredientValue(ingredientName));
        return ingredient.orElseGet(() -> fail("Unable to retrieve ingredient with name " + ingredientName));
    }

    @Step("Attempt retrieving ingredient with name {0}")
    public void attemptRetrievingIngredient(String ingredientName) {
        var ingredient = getFromIngredientsList(new IngredientValue(ingredientName));
        assertThat(ingredient).isNotPresent();
        fetchIngredient(generateNotFoundIngredientUrl());
    }

    @Step("Assert ingredient {0} is accessible")
    public void assertIngredientInfoIsAccessible(IngredientValue expected) {
        var actual = getIngredientFromUrl(expected.selfLink());
        actual.assertEqualTo(expected);
    }

    private void fetchIngredient(String ingredientDetailUrl) {
        rest().accept(HAL_JSON_VALUE).get(ingredientDetailUrl).andReturn();
    }

    private String generateNotFoundIngredientUrl() {
        return ingredientsResourceUrl() + "/unknown";
    }
}
