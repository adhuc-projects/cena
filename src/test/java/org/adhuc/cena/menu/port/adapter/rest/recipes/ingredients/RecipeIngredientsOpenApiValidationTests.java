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
 * @version 0.2.0
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
    void respond400OnCreationWithoutId() throws Exception {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{}")
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
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() throws Exception {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"id\":\"%s\",\"other\":\"some value\"}", IngredientMother.ID))
                .when()
                .post(RECIPE_INGREDIENTS_API_URL, ID.toString())
                .then()
                .statusCode(CREATED.value());
    }

}
