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
package org.adhuc.cena.menu.port.adapter.rest.assertion.support;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * Custom assertion definition for {@link Error}s.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class ErrorAssert extends AbstractAssert<ErrorAssert, Error> {

    public ErrorAssert(Error actual) {
        super(actual, ErrorAssert.class);
    }

    public static ErrorAssert assertThat(Error actual) {
        return new ErrorAssert(actual);
    }

    public ErrorAssert hasCode(ExceptionCode code) {
        isNotNull();
        if (code.code() != actual.getCode()) {
            failWithMessage("Expected error's code to be <%d> but was <%d>", code.code(), actual.getCode());
        }
        return this;
    }

    public ErrorAssert hasStatus(int status) {
        isNotNull();
        if (status != actual.getStatus()) {
            failWithMessage("Expected error's status to be <%d> but was <%d>", status, actual.getStatus());
        }
        return this;
    }

    public ErrorAssert hasMessage(String message) {
        isNotNull();
        if (!Objects.equals(message, actual.getMessage())) {
            failWithMessage("Expected error's message to be <%s> but was <%s>", message, actual.getMessage());
        }
        return this;
    }

    public ErrorAssert detailsContainsExactlyInAnyOrder(String... details) {
        isNotNull();
        Assertions.assertThat(actual.getDetails()).containsExactlyInAnyOrder(details);
        return this;
    }

}
