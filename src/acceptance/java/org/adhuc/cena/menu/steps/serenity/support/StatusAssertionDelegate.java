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
package org.adhuc.cena.menu.steps.serenity.support;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.springframework.http.HttpStatus.*;

import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.springframework.http.HttpStatus;

/**
 * A delegate providing convenient methods for response status assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.0.1
 */
public final class StatusAssertionDelegate {

    public ValidatableResponse assertOk() {
        return assertOk(then());
    }

    public ValidatableResponse assertOk(ValidatableResponse response) {
        return assertStatus(response, OK);
    }

    public ValidatableResponse assertCreated() {
        return assertCreated(then());
    }

    public ValidatableResponse assertCreated(ValidatableResponse response) {
        return assertStatus(response, CREATED);
    }

    public ValidatableResponse assertNoContent() {
        return assertNoContent(then());
    }

    public ValidatableResponse assertNoContent(ValidatableResponse response) {
        return assertStatus(response, NO_CONTENT);
    }

    public ValidatableResponse assertBadRequest() {
        return assertBadRequest(then());
    }

    public ValidatableResponse assertBadRequest(ValidatableResponse response) {
        return assertStatus(response, BAD_REQUEST);
    }

    public ValidatableResponse assertInvalidRequest() {
        var response = assertBadRequest();
        var jsonPath = response.extract().jsonPath();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(jsonPath.getInt("code")).isEqualTo(101000);
        });
        return response;
    }

    public ValidatableResponse assertUnauthorized() {
        return assertUnauthorized(then());
    }

    public ValidatableResponse assertUnauthorized(ValidatableResponse response) {
        return assertStatus(response, UNAUTHORIZED);
    }

    public ValidatableResponse assertNotFound() {
        return assertNotFound(then());
    }

    public ValidatableResponse assertNotFound(ValidatableResponse response) {
        return assertStatus(response, NOT_FOUND);
    }

    private ValidatableResponse assertStatus(ValidatableResponse response, HttpStatus status) {
        return response.statusCode(status.value());
    }

}
