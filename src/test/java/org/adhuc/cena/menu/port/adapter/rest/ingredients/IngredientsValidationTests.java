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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;

/**
 * The validation test class for ingredients resources disabling Open API validation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restValidation")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"cena.menu-generation.rest.openApiValidation.enabled=false", "spring.mvc.locale=en_UK"})
@DisplayName("Ingredients resource should")
class IngredientsValidationTests {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @LocalServerPort
    private int port;
    @Autowired
    private MenuGenerationProperties properties;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("respond Bad Request with validation error on creation when request does not contain name property")
    void respond400OnCreationWithoutName() {
        var error =
                given()
                        .log().ifValidationFails()
                        .auth().preemptive().basic(properties.getSecurity().getIngredientManager().getUsername(),
                        properties.getSecurity().getIngredientManager().getPassword())
                        .contentType(APPLICATION_JSON_VALUE)
                        .body("{}")
                        .when()
                        .post(INGREDIENTS_API_URL)
                        .then()
                        .statusCode(BAD_REQUEST.value())
                        .assertThat()
                        .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactlyInAnyOrder("Invalid request body property 'name': must not be blank. Actual value is <null>");
    }

    @Test
    @DisplayName("respond Bad Request with validation error on creation when request contains blank name property")
    void respond400OnCreationWithBlankName() {
        var error =
                given()
                        .log().ifValidationFails()
                        .auth().preemptive().basic(properties.getSecurity().getIngredientManager().getUsername(),
                        properties.getSecurity().getIngredientManager().getPassword())
                        .contentType(APPLICATION_JSON_VALUE)
                        .body("{\"name\":\"\"}")
                        .when()
                        .post(INGREDIENTS_API_URL)
                        .then()
                        .statusCode(BAD_REQUEST.value())
                        .assertThat()
                        .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactlyInAnyOrder("Invalid request body property 'name': must not be blank. Actual value is ''");
    }

    @Test
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getIngredientManager().getUsername(),
                properties.getSecurity().getIngredientManager().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Cucumber\",\"other\":\"some value\"}")
                .when()
                .post(INGREDIENTS_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

}
