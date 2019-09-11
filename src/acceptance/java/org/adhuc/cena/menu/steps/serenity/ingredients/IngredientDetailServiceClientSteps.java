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

import static net.serenitybdd.rest.SerenityRest.rest;
import static org.assertj.core.api.Assertions.assertThat;

import net.thucydides.core.annotations.Step;

/**
 * The ingredient detail rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientDetailServiceClientSteps {

    @Step("Get ingredient from {0}")
    public IngredientValue getIngredientFromUrl(String ingredientDetailUrl) {
        return rest().get(ingredientDetailUrl).then().extract().as(IngredientValue.class);
    }

    @Step("Assert ingredient {1} corresponds to expected {0}")
    public void assertIngredientInfoIsEqualToExpected(IngredientValue expected, IngredientValue actual) {
        assertThat(actual.name()).isEqualTo(expected.name());
    }

}
