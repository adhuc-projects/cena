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
package org.adhuc.cena.menu.common.exception;

import static org.adhuc.cena.menu.common.exception.ExceptionCode.INTERNAL_ERROR;
import static org.adhuc.cena.menu.common.exception.ExceptionCode.INVALID_REQUEST;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The {@link CenaException} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Cena exception should")
class CenaExceptionShould {

    @Test
    @DisplayName("have default message and default code when creating an exception from null code")
    void haveDefaultMessageCreateWithNullCode() {
        var exception = new CenaException(null);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INTERNAL_ERROR.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isEqualTo(INTERNAL_ERROR);
        });
    }

    @Test
    @DisplayName("have code's message and specified code when creating an exception from non null code")
    void haveMessageCreateWithNonNullCode() {
        var exception = new CenaException(INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INVALID_REQUEST.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have default message and default code when creating an exception from null message and code")
    void haveDefaultMessageCreateWithNullMessageAndCode() {
        var exception = new CenaException(null, null);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INTERNAL_ERROR.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isEqualTo(INTERNAL_ERROR);
        });
    }

    @Test
    @DisplayName("have code's message and specified code when creating an exception from null message and non null code")
    void haveMessageCreateWithNullMessageNonNullCode() {
        var exception = new CenaException(null, INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INVALID_REQUEST.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have specified message and code when creating an exception from non null message and code")
    void haveMessageCreateWithNonNullMessageAndCode() {
        var exception = new CenaException("custom message", INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("custom message");
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have default message and default code when creating an exception from null message, cause and code")
    void haveDefaultMessageCreateWithNullMessageCauseAndCode() {
        var exception = new CenaException(null, null, null);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INTERNAL_ERROR.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isEqualTo(INTERNAL_ERROR);
        });
    }

    @Test
    @DisplayName("have code's message and specified code when creating an exception from null message and cause and non null code")
    void haveMessageCreateWithNullMessageAndCauseNonNullCode() {
        var exception = new CenaException(null, null, INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INVALID_REQUEST.description());
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have specified message and code when creating an exception from null cause and non null message and code")
    void haveMessageCreateWithNullCauseNonNullMessageAndCode() {
        var exception = new CenaException("custom message", null, INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("custom message");
            softAssertions.assertThat(exception.getCause()).isNull();
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have default message and default code when creating an exception from null message and code and non null cause")
    void haveDefaultMessageCreateWithNullMessageAndCodeNonNullCause() {
        var cause = new RuntimeException("cause");
        var exception = new CenaException(null, cause, null);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INTERNAL_ERROR.description());
            softAssertions.assertThat(exception.getCause()).isSameAs(cause);
            softAssertions.assertThat(exception.exceptionCode()).isEqualTo(INTERNAL_ERROR);
        });
    }

    @Test
    @DisplayName("have code's message and specified code when creating an exception from null message and non null cause and code")
    void haveMessageCreateWithNullMessageNonNullCauseAndCode() {
        var cause = new RuntimeException("cause");
        var exception = new CenaException(null, cause, INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo(INVALID_REQUEST.description());
            softAssertions.assertThat(exception.getCause()).isSameAs(cause);
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

    @Test
    @DisplayName("have specified message and code when creating an exception from non null message, cause and code")
    void haveMessageCreateWithNonNullMessageCauseAndCode() {
        var cause = new RuntimeException("cause");
        var exception = new CenaException("custom message", cause, INVALID_REQUEST);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("custom message");
            softAssertions.assertThat(exception.getCause()).isSameAs(cause);
            softAssertions.assertThat(exception.exceptionCode()).isNotNull().isEqualTo(INVALID_REQUEST);
        });
    }

}
