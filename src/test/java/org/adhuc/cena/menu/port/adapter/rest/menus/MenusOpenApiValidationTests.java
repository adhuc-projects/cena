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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.adhuc.cena.menu.common.ExceptionCode.INVALID_REQUEST;
import static org.adhuc.cena.menu.common.ExceptionCode.MENU_NOT_CREATABLE_WITH_UNKNOWN_RECIPE;
import static org.adhuc.cena.menu.port.adapter.rest.assertion.support.ErrorAssert.assertThat;
import static org.adhuc.cena.menu.recipes.RecipeMother.TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
import org.adhuc.cena.menu.menus.MenuRepository;
import org.adhuc.cena.menu.port.adapter.rest.assertion.support.Error;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeMother;
import org.adhuc.cena.menu.recipes.RecipeRepository;

/**
 * The Open API validation test class for menus resources.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("openApiValidation")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Menus resource should")
class MenusOpenApiValidationTests {

    private static final String MENUS_API_URL = "/api/menus";

    @LocalServerPort
    private int port;
    @Autowired
    private MenuGenerationProperties properties;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        menuRepository.deleteAll();
        recipeRepository.save(recipe());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020", "2020/01/01", "2020-02-30"})
    @DisplayName("respond Bad Request with OpenAPI validation error on menus list retrieval when request defines a since date query parameter with invalid value")
    void respond400OnListInvalidSinceDateQueryParam(String value) {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][since]", value)
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("String \"" + value + "\" is invalid against requested date format(s) yyyy-MM-dd");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020", "2020/01/01", "2020-02-30"})
    @DisplayName("respond Bad Request with OpenAPI validation error on menus list retrieval when request defines a until date query parameter with invalid value")
    void respond400OnListInvalidUntilDateQueryParam(String value) {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][until]", value)
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("String \"" + value + "\" is invalid against requested date format(s) yyyy-MM-dd");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on menus list retrieval when request defines since and until query parameters with invalid value")
    void respond400OnListInvalidSinceDateAndUntilDateQueryParam() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][since]", "since")
                .queryParam("filter[date][until]", "until")
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactlyInAnyOrder(
                        "String \"since\" is invalid against requested date format(s) yyyy-MM-dd",
                        "String \"until\" is invalid against requested date format(s) yyyy-MM-dd"
                );
    }

    @Test
    @DisplayName("respond Bad Request with validation error on menus list retrieval when request defines a since date higher than until date")
    void respond400OnListSinceDateQueryParamHigherThanUntilDateQueryParam() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][since]", LocalDate.now().toString())
                .queryParam("filter[date][until]", LocalDate.now().minusDays(1).toString())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactly("Invalid query parameters: lower bound filter[date][since] must be lower than or equal to upper bound filter[date][until]");
    }

    @Test
    @DisplayName("respond Bad Request with validation error on menus list retrieval when request defines until date before today")
    void respond400OnListUntilDateBeforeTodayQueryParam() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][until]", LocalDate.now().minusDays(1).toString())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("Validation error")
                .detailsContainsExactly("Invalid query parameters: lower bound filter[date][since] must be lower than or equal to upper bound filter[date][until]");
    }

    @Test
    @DisplayName("respond OK on menus list retrieval when request does not define date range")
    void respond200OnListWithNoDateRange() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond OK on menus list retrieval when request defines since date")
    void respond200OnListWithSinceDate() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][since]", LocalDate.now().toString())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond OK on menus list retrieval when request defines until date")
    void respond200OnListWithUntilDate() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][until]", LocalDate.now().toString())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond OK on menus list retrieval when request defines date range")
    void respond200OnListWithDateRange() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .queryParam("filter[date][since]", LocalDate.now().plusDays(1).toString())
                .queryParam("filter[date][until]", LocalDate.now().plusDays(2).toString())
                .when()
                .get(MENUS_API_URL)
                .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain date property")
    void respond400OnCreationWithoutDate() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}", RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("Object has missing required properties ([\"date\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains date property in wrong format")
    void respond400OnCreationWrongDateFormat() {
        var date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\", \"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        date, RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly(String.format("[Path '/date'] String \"%s\" is invalid against requested date format(s) yyyy-MM-dd", date));
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain mealType property")
    void respond400OnCreationWithoutMealType() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}", LocalDate.now(), RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("Object has missing required properties ([\"mealType\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains unknown mealType property")
    void respond400OnCreationWithUnknownMealType() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"UNKNOWN\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("[Path '/mealType'] Instance value (\"UNKNOWN\") not found in enum (possible values: [\"LUNCH\",\"DINNER\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain covers property")
    void respond400OnCreationWithoutCovers() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"mainCourseRecipes\":[\"%s\"]}", LocalDate.now(), RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("Object has missing required properties ([\"covers\"])");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains negative covers property")
    void respond400OnCreationWithNegativeCovers(int covers) {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":%d,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), covers, RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly(String.format("[Path '/covers'] Numeric instance is lower than the required minimum (minimum: 1, found: %d)", covers));
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request does not contain mainCourseRecipes property")
    void respond400OnCreationWithoutMainCourseRecipes() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2}", LocalDate.now()))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("Object has missing required properties ([\"mainCourseRecipes\"])");
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains unknown mainCourseRecipes")
    void respond400OnCreationWithUnknownMainCourseRecipes() {
        var anotherRecipeId = new RecipeId("2211b1fc-a5f3-42c1-b591-fb979e0449d1");
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\",\"%s\"]}",
                        LocalDate.now(), TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, anotherRecipeId))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(MENU_NOT_CREATABLE_WITH_UNKNOWN_RECIPE)
                .hasMessage(String.format("Menu scheduled at %s's lunch cannot be created with unknown recipes [%s, %s]",
                        LocalDate.now(), anotherRecipeId, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @Test
    @DisplayName("respond Bad Request with OpenAPI validation error on creation when request contains invalid mainCourseRecipes property")
    void respond400OnCreationWithInvalidMainCourseRecipes() {
        var error = given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                        properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"invalid\"]}", LocalDate.now()))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(BAD_REQUEST.value())
                .assertThat()
                .extract().jsonPath().getObject("", Error.class);
        assertThat(error)
                .hasCode(INVALID_REQUEST)
                .hasMessage("OpenAPI validation error")
                .detailsContainsExactly("[Path '/mainCourseRecipes/0'] Input string \"invalid\" is not a valid UUID");
    }

    @Test
    @DisplayName("respond Created on creation when request contains additional property")
    void respond201OnCreationWithAdditionalProperty() {
        given()
                .log().ifValidationFails()
                .auth().preemptive().basic(properties.getSecurity().getUser().getUsername(),
                properties.getSecurity().getUser().getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"],\"other\":\"some value\"}",
                        LocalDate.now(), RecipeMother.ID))
                .when()
                .post(MENUS_API_URL)
                .then()
                .statusCode(CREATED.value());
    }

}
