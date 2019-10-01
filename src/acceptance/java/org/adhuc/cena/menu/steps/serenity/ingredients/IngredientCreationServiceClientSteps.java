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
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

/**
 * The ingredient creation rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientCreationServiceClientSteps extends AbstractIngredientServiceClientSteps {

    @Steps
    private IngredientDetailServiceClientSteps ingredientDetailServiceClient;

    @Step("Create the ingredient {0}")
    public void createIngredient(IngredientValue ingredient) {
        var ingredientsResourceUrl = getIngredientsResourceUrl();
        rest().contentType(HAL_JSON_VALUE).body(ingredient).post(ingredientsResourceUrl).andReturn();
    }

    @Step("Create an ingredient without name")
    public void createIngredientWithoutName() {
        var ingredient = storeIngredient(new IngredientValue(null));
        createIngredient(ingredient);
    }

    @Step("Assert ingredient has been successfully created")
    public void assertIngredientSuccessfullyCreated(IngredientValue ingredient) {
        var ingredientLocation = then().statusCode(CREATED.value()).extract().header(LOCATION);
        assertThat(ingredientLocation).isNotBlank();
        var retrievedIngredient = ingredientDetailServiceClient.getIngredientFromUrl(ingredientLocation);
        retrievedIngredient.assertEqualTo(ingredient);
    }

    @Step("Assert ingredient creation results in invalid request error")
    public void assertInvalidRequestError() {
        then().statusCode(BAD_REQUEST.value());
    }

}
