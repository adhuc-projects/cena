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

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import net.thucydides.core.annotations.Step;

/**
 * The ingredient creation rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientCreationServiceClientSteps extends AbstractIngredientServiceClientSteps {

    @Step("Create the ingredient")
    public void createIngredient() {
        createIngredient(ingredient());
    }

    @Step("Create the ingredient {0}")
    public void createIngredient(IngredientValue ingredient) {
        var ingredientsResourceUrl = getIngredientsResourceUrl();
        rest().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(ingredient).post(ingredientsResourceUrl).andReturn();
    }

    @Step("Create an ingredient without name")
    public void createIngredientWithoutName() {
        storeIngredient(new IngredientValue(null));
        createIngredient(ingredient());
    }

    @Step("Assert ingredient has been successfully created")
    public void assertIngredientSuccessfullyCreated() {
        var ingredientLocation = then().statusCode(CREATED.value()).extract().header(LOCATION);
        assertThat(ingredientLocation).isNotBlank();
        // TODO assert ingredient retrieved from the location URL is the same as the created one
    }

    @Step("Assert ingredient creation results in invalid request error")
    public void assertInvalidRequestError() {
        then().statusCode(BAD_REQUEST.value());
    }

}
