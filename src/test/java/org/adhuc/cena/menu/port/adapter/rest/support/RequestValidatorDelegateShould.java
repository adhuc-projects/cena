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
package org.adhuc.cena.menu.port.adapter.rest.support;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

/**
 * The {@link RequestValidatorDelegate} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("restController")
@DisplayName("Request validator delegate should")
class RequestValidatorDelegateShould {

    private RequestValidatorDelegate validator;

    @BeforeEach
    void setUp() {
        this.validator = new RequestValidatorDelegate();
    }

    @Test
    @DisplayName("do nothing when validation returned empty errors")
    void doNothingNoError() {
        assertDoesNotThrow(() -> validator.validateRequest(buildEmptyErrors()));
    }

    @Test
    @DisplayName("throw InvalidRestRequestException when validation returned non empty errors")
    void throwInvalidRestRequestExceptionOnError() {
        assertThrows(InvalidRestRequestException.class, () -> validator.validateRequest(buildErrorsWithError()));
    }

    private Errors buildEmptyErrors() {
        return new MapBindingResult(Collections.emptyMap(), "test");
    }

    private Errors buildErrorsWithError() {
        var errors = new MapBindingResult(Collections.emptyMap(), "test");
        errors.addError(new ObjectError("test", "message"));
        return errors;
    }

}
