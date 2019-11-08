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
package org.adhuc.cena.menu.port.adapter.rest;

import static java.util.stream.Collectors.joining;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import static org.adhuc.cena.menu.common.ExceptionCode.ENTITY_NOT_FOUND;
import static org.adhuc.cena.menu.port.adapter.rest.assertion.support.ErrorAssert.assertThat;

import java.util.List;
import java.util.UUID;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;

/**
 * The Open API validation test class for ingredients resources.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("restController")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Error controller should")
class CenaErrorControllerShould {

    private static final String INGREDIENT_BASE_URL = "/api/ingredients/%s";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("respond Not Found when requesting unknown ingredient")
    void respond404UnknownIngredient() throws Exception {
        var ingredientId = UUID.randomUUID();
        var url = String.format(INGREDIENT_BASE_URL, ingredientId);

        var error = given()
                .log().ifValidationFails()
                .accept(List.of(TEXT_HTML_VALUE, ALL_VALUE).stream().collect(joining(",")))
                .when()
                .get(url)
                .then()
                .statusCode(NOT_FOUND.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(ENTITY_NOT_FOUND)
                .hasMessage(String.format("Cannot find entity of type Ingredient with identity %s", ingredientId));
    }

}
