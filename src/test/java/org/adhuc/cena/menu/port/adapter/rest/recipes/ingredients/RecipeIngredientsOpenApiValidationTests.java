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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static java.lang.String.format;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.adhuc.cena.menu.common.ExceptionCode.INVALID_REQUEST;
import static org.adhuc.cena.menu.ingredients.IngredientMother.ingredient;
import static org.adhuc.cena.menu.port.adapter.rest.assertion.support.ErrorAssert.assertThat;
import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.builder;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.ingredients.IngredientRepository;
import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;
import org.adhuc.cena.menu.recipes.RecipeRepository;

/**
 * The Open API validation test class for recipe ingredients resources.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("openApiValidation")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Recipe ingredients resource should")
class RecipeIngredientsOpenApiValidationTests {

    private static final String RECIPE_INGREDIENTS_API_URL = "/api/recipes/{id}/ingredients";

    @LocalServerPort
    private int port;
    @Autowired
    private MenuGenerationProperties properties;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        recipeRepository.save(builder().withAuthorName(properties.getSecurity().getUser().getUsername()).build());
        ingredientRepository.save(ingredient());
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain id property")
    void respond400OnCreationWithoutId() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"mainIngredient\":true, \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("Object has missing required properties ([\"id\"])");
    }

    @Test
    @DisplayName("respond Bad Request with validation error on creation when request contains quantity but no measurement unit")
    void respond400OnCreationWithQuantityNoMeasurementUnit() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"mainIngredient\":true, \"quantity\":1}")
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(BAD_REQUEST.value())
                .log().everything()
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactlyInAnyOrder("Invalid request: should have either none or both properties ([\"measurementUnit\",\"quantity\"])");
    }

    @Test
    @DisplayName("respond Bad Request with validation error on creation when request contains measurement unit but no quantity")
    void respond400OnCreationWithMeasurementUnitNoQuantity() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"mainIngredient\":true, \"measurementUnit\":\"DOZEN\"}")
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactlyInAnyOrder("Invalid request: should have either none or both properties ([\"measurementUnit\",\"quantity\"])");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    @DisplayName("respond Bad Request with validation error on creation when request contains negative quantity")
    void respond400OnCreationWithNegativeQuantity(int quantity) {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(format("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"mainIngredient\":true, \"measurementUnit\":\"DOZEN\", \"quantity\": %d}", quantity))
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder(format("[Path '/quantity'] Numeric instance is lower than the required minimum (minimum: 1, found: %d)", quantity));
    }

    @Test
    @DisplayName("respond Created on creation when request does not contain main ingredient information")
    void respond400OnCreationWithoutMainIngredient() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(format("{\"id\":\"%s\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}", IngredientMother.ID))
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    @DisplayName("respond Created on creation when request does not contain quantity information")
    void respond400OnCreationWithoutQuantity() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(format("{\"id\":\"%s\", \"mainIngredient\":true}", IngredientMother.ID))
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(format("{\"id\":\"%s\", \"mainIngredient\":true, \"quantity\":1, \"measurementUnit\":\"DOZEN\", \"other\":\"some value\"}", IngredientMother.ID))
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(CREATED.value());
    }

}
