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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static java.lang.String.format;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.adhuc.cena.menu.common.exception.ExceptionCode.INVALID_REQUEST;
import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.port.adapter.rest.assertion.support.ErrorAssert.assertThat;

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
import org.adhuc.cena.menu.ingredients.IngredientRepository;
import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;
import org.springframework.test.annotation.DirtiesContext;

/**
 * The Open API validation test class for recipes resources.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("openApiValidation")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Recipes resource should")
class RecipesOpenApiValidationTests {

    private static final String RECIPES_API_URL = "/api/recipes";

    @LocalServerPort
    private int port;
    @Autowired
    private MenuGenerationProperties properties;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        ingredientRepository.save(ingredient(ID, NAME, MEASUREMENT_TYPES));
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on recipes list retrieval when request defines an ingredient query parameter with invalid value")
    void respond400OnListInvalidIngredientQueryParam() {
        var error = given()
                .log().ifValidationFails()
                .queryParam("filter[ingredient]", "invalid uuid")
                .when()
                .get(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("Input string \"invalid uuid\" is not a valid UUID");
    }

    @Test
    @DisplayName("respond OK on recipes list retrieval when request defines no ingredient query parameter")
    void respond200OnListNoIngredientQueryParam() {
        given()
                .log().ifValidationFails()
                .when()
                .get(RECIPES_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond OK on recipes list retrieval when request defines an ingredient query parameter with valid value")
    void respond200OnListValidIngredientQueryParam() {
        given()
                .log().ifValidationFails()
                .queryParam("filter[ingredient]", ID.id().toString())
                .when()
                .get(RECIPES_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain name nor content properties")
    void respond400OnCreationWithoutNameNorContent() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("Object has missing required properties ([\"content\",\"name\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does contains blank name and content properties")
    void respond400OnCreationWithBlankNameAndContent() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"\",\"content\":\"\",\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder(
                        "[Path '/name'] String \"\" is too short (length: 0, required minimum: 1)",
                        "[Path '/content'] String \"\" is too short (length: 0, required minimum: 1)"
                );
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain name property")
    void respond400OnCreationWithoutName() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"content\":\"Cut everything into dices, mix it, dress it\",\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
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
    void respond400OnCreationWithoutContent() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
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
    @DisplayName("respond Created on creation when request does not contain servings property")
    void respond201OnCreationWithoutServings() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains negative or zero servings")
    void respond400OnCreationWithNegativeServings(int value) {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(format("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":%d,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}", value))
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder(format("[Path '/servings'] Numeric instance is lower than the required minimum (minimum: 1, found: %d)", value));
    }

    @Test
    @DisplayName("respond Created on creation when request does not contain courseTypes property")
    void respond201OnCreationWithoutCourseTypes() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\",\"servings\":2}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains unknown courseType")
    void respond400OnCreationWithUnknownCourseType() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\",\"UNKNOWN\"]}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder("[Path '/courseTypes/2'] Instance value (\"UNKNOWN\") not found in enum " +
                        "(possible values: [\"APERITIF\",\"STARTER\",\"MAIN_COURSE\",\"DESSERT\"])");
    }

    @Test
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"],\"other\":\"some value\"}")
                .when()
                .post(RECIPES_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

}
