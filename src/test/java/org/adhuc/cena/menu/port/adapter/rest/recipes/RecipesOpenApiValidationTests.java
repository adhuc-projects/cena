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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.adhuc.cena.menu.common.ExceptionCode.INVALID_REQUEST;
import static org.adhuc.cena.menu.port.adapter.rest.assertion.support.ErrorAssert.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;

/**
 * The Open API validation test class for recipes resources.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("openApiValidation")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Recipes resource should")
class RecipesOpenApiValidationTests {

    private static final String RECIPES_API_URL = "/api/recipes";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain name property")
    void respond400OnCreationWithoutName() throws Exception {
        var error = given()
                .log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"content\":\"Cut everything into dices, mix it, dress it\"}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("Object has missing required properties ([\"name\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain content property")
    void respond400OnCreationWithoutContent() throws Exception {
        var error = given()
                .log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\"}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("Object has missing required properties ([\"content\"])");
    }

    @Test
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() throws Exception {
        given()
                .log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\",\"other\":\"some value\"}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

}
