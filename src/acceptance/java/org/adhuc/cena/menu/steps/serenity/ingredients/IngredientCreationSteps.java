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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.INGREDIENT_MANAGER;

import java.util.function.Supplier;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The ingredient creation rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
public class IngredientCreationSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();

    @Steps
    private IngredientDetailSteps ingredientDetail;

    @Step("Create the ingredient {0}")
    public void createIngredient(IngredientValue ingredient) {
        createIngredient(ingredient, this::rest);
    }

    @Step("Create the ingredient {0} as ingredient manager")
    void createIngredientAsIngredientManager(IngredientValue ingredient) {
        createIngredient(ingredient, () -> rest(INGREDIENT_MANAGER));
    }

    private void createIngredient(IngredientValue ingredient, Supplier<RequestSpecification> specificationSupplier) {
        var ingredientsResourceUrl = ingredientsResourceUrl();
        specificationSupplier.get().contentType(HAL_JSON_VALUE)
                .body(ingredient.withoutId())
                .post(ingredientsResourceUrl).andReturn();
    }

    @Step("Create an ingredient without name")
    public void createIngredientWithoutName() {
        var ingredient = storeIngredient(new IngredientValue(null));
        createIngredient(ingredient);
    }

    @Step("Assert ingredient {0} has been successfully created")
    public void assertIngredientSuccessfullyCreated(IngredientValue ingredient) {
        var ingredientLocation = assertCreated().extract().header(LOCATION);
        assertThat(ingredientLocation).isNotBlank();
        var retrievedIngredient = ingredientDetail.getIngredientFromUrl(ingredientLocation);
        retrievedIngredient.assertEqualTo(ingredient);
    }

    @Step("Assert ingredient name {0} already exists")
    public void assertIngredientNameAlreadyExists(String name) {
        var jsonPath = assertConflict().extract().jsonPath();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(jsonPath.getString("message")).contains("Ingredient name '" + name + "' already used by an existing ingredient");
            softly.assertThat(jsonPath.getInt("code")).isEqualTo(900000);
        });
    }

    public ValidatableResponse assertInvalidRequestConcerningUnknownMeasurementUnit(int position, String measurementType) {
        var response = assertBadRequest();
        var jsonPath = response.extract().jsonPath();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(jsonPath.getInt("code")).isEqualTo(101000);
            softly.assertThat(jsonPath.getList("details", String.class)).anyMatch(d ->
                    d.startsWith(String.format("[Path '/%s/%d'] Instance value (\"%s\") not found in enum",
                            IngredientValue.MEASUREMENT_TYPES_FIELD, position, measurementType))
            );
        });
        return response;
    }

}
