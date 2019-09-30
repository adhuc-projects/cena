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

import net.serenitybdd.core.Serenity;

import org.adhuc.cena.menu.steps.serenity.AbstractServiceClientSteps;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.IngredientNameComparator;

/**
 * An abstract ingredient rest-service client steps definition, providing convenient methods to store ingredient
 * information between steps and retrieving ingredients resource URL.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
abstract class AbstractIngredientServiceClientSteps extends AbstractServiceClientSteps {

    private static final String INGREDIENTS_LIST_CLIENT = "ingredientsListClient";
    private static final String INGREDIENTS_URL_SESSION_KEY = "ingredientsResourceUrl";
    static final String INGREDIENT_SESSION_KEY = "ingredient";
    static final IngredientNameComparator INGREDIENT_COMPARATOR = new IngredientNameComparator();

    final <I extends IngredientValue> I storeIngredient(I ingredient) {
        return storeIngredient(INGREDIENT_SESSION_KEY, ingredient);
    }

    final <I extends IngredientValue> I storeIngredient(String sessionKey, I ingredient) {
        Serenity.setSessionVariable(sessionKey).to(ingredient);
        return ingredient;
    }

    public final IngredientValue storedIngredient() {
        return ingredient(INGREDIENT_SESSION_KEY);
    }

    final IngredientValue ingredient(String sessionKey) {
        IngredientValue ingredient = Serenity.sessionVariableCalled(sessionKey);
        assertThat(ingredient).isNotNull();
        return ingredient;
    }

    final IngredientListClientDelegate listClient() {
        if (!Serenity.hasASessionVariableCalled(INGREDIENTS_LIST_CLIENT)) {
            Serenity.setSessionVariable(INGREDIENTS_LIST_CLIENT).to(new IngredientListClientDelegate(getIngredientsResourceUrl()));
        }
        return Serenity.sessionVariableCalled(INGREDIENTS_LIST_CLIENT);
    }

    final String getIngredientsResourceUrl() {
        if (!Serenity.hasASessionVariableCalled(INGREDIENTS_URL_SESSION_KEY)) {
            Serenity.setSessionVariable(INGREDIENTS_URL_SESSION_KEY).to(getApiClientResource().getIngredients());
        }
        return Serenity.sessionVariableCalled(INGREDIENTS_URL_SESSION_KEY);
    }

}
