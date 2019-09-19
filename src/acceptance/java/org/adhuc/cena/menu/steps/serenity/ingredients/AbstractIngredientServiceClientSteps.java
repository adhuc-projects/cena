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

/**
 * An abstract ingredient rest-service client steps definition, providing convenient methods to store ingredient
 * information between steps and retrieving ingredients resource URL.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
abstract class AbstractIngredientServiceClientSteps extends AbstractServiceClientSteps {

    static final String INGREDIENT_SESSION_KEY = "ingredient";

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

    final String getIngredientsResourceUrl() {
        return getApiClientResource().getIngredients();
    }

}
